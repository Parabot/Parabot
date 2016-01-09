package org.parabot.core.lib.javafx;

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
public class JavaFX extends Library {
	private static boolean valid;

	@Override
	public void init() {
		if (!hasJar()) {
			System.err.println("Failed to load javafx... [jar missing]");
			return;
		}
		Core.verbose("Adding javafx jar file to build path: "
				+ getJarFileURL().getPath());
		BuildPath.add(getJarFileURL());

		try {
			Class.forName("javafx.application.Application");
			valid = true;
		} catch (ClassNotFoundException e) {
			System.err
					.println("Failed to add javafx to build path, or incorrupt download");
		}

		Core.verbose("JavaFX initialized.");
	}

	@Override
	public boolean isAdded() {
		return valid;
	}

	@Override
	public File getJarFile() {
		return new File(Directories.getCachePath(), "javafx.jar");
	}

	@Override
	public URL getDownloadLink() {
		try {
			return new URL("http://bot.parabot.org/libs/jfxrt.jar");
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	@Override
	public String getLibraryName() {
		return "JavaFX";
	}
	
	public static boolean isValid() {
		return valid;
	}
}
