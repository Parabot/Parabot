package org.parabot.core.jython;

import java.io.File;
import java.net.URL;

import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.build.BuildPath;

/**
 * 
 * Jython util class
 * 
 * @author Everel
 *
 */
public class Jython {
	private static boolean valid;
	
	/**
	 * Determines if jython jar has been downloaded.
	 * @return <b>false</b> if jython jar has not been downloaded.....
	 */
	public static final boolean hasJar() {
		return getJarFile().exists();
	}
	
	/**
	 * Adds the jython jar to the build path
	 */
	public static void init() {
		if(!hasJar()) {
			System.err.println("Failed to load jython... [jar missing]");
			return;
		}
		Core.verbose("Adding jyton jar file to build path: " + getLocalJarFile().getPath());
		BuildPath.add(getLocalJarFile());
		
		try {
			Class.forName("org.python.Version");
			valid = true;
		} catch (ClassNotFoundException e) {
			System.err.println("Failed to add jython to build path, or incorrupt download");
		}
		
		Core.verbose("Jython initialized.");
	}
	
	/**
	 * Determines if jython jar has been successfully added to the buildpath
	 * @return if jython jar was successfully added to the buildpath
	 */
	public static boolean isValid() {
		return valid;
	}
	
	/**
	 * Gets jython's local jar file
	 * @return file
	 */
	public static final File getJarFile() {
		return new File(Directories.getCachePath(), "jython.jar");
	}
	
	/**
	 * Gets jython's local jar file in URL format
	 * @return URL
	 */
	public static final URL getLocalJarFile() {
		try {
			return getJarFile().toURI().toURL();
		} catch (Throwable t ) {
			t.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Location where jython jar is located on the parabot site
	 * @return URL
	 */
	public static final URL getDownloadLink() {
		try {
			return new URL("http://bot.parabot.org/libs/jython.jar");
		} catch (Throwable t ) {
			t.printStackTrace();
		}
		return null;
	}

}
