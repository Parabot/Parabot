package org.parabot.core.classpath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.parabot.core.Directories;

/**
 * 
 * @author Clisprail
 * @author Matt
 */
public class Resources {
	
	/**
	 * Dumps a resource from a input stream
	 * @param classPath
	 * @param name
	 * @param inputstream
	 * @throws IOException
	 */
	public static void loadResource(final ClassPath classPath, final String name, final InputStream in)
			throws IOException {
		File f = File.createTempFile("bot", ".tmp", Directories.getTempDirectory());
		f.deleteOnExit();
		try (OutputStream out = new FileOutputStream(f)) {
			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read(buffer)) != -1)
				out.write(buffer, 0, len);
		} catch (IOException e) {
		}
		classPath.resources.put(name, f.toURI().toURL());
	}

}
