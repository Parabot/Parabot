package org.parabot.core.lib.jython;

import java.io.File;
import java.net.URL;

import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.build.BuildPath;
import org.parabot.core.lib.Library;

/**
 * 
 * Jython util class
 * 
 * @author Everel
 * 
 */
public class Jython extends Library {
	private static boolean valid;


	@Override
	public void init() {
		if (!hasJar()) {
			System.err.println("Failed to load jython... [jar missing]");
			return;
		}
		Core.verbose("Adding jyton jar file to build path: "
				+ getJarFileURL().getPath());
		BuildPath.add(getJarFileURL());

		try {
			Class.forName("org.python.Version");
			valid = true;
		} catch (ClassNotFoundException e) {
			System.err
					.println("Failed to add jython to build path, or incorrupt download");
		}

		Core.verbose("Jython initialized.");
	}

	@Override
	public boolean isAdded() {
		return valid;
	}

	@Override
	public File getJarFile() {
		return new File(Directories.getCachePath(), "jython.jar");
	}

	@Override
	public URL getDownloadLink() {
		try {
			return new URL("http://bot.parabot.org/libs/jython.jar");
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	@Override
	public String getLibraryName() {
		return "Jython";
	}
	
	public static boolean isValid() {
		return valid;
	}

}
