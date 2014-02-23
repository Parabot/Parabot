package org.parabot.core.lib;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
 * @author Everel
 *
 */
public abstract class Library {
	
	/**
	 * Determines if this library jar has already been downloaded.
	 * @return <b>false</b> if library jar has not been downloaded, otherwise <b>true</b>
	 */
	public boolean hasJar() {
		return getJarFile().exists();
	}
	
	/**
	 * Adds the library to the buildpath and validates if it has been added
	 */
	public abstract void init();
	
	/**
	 * Determines if library has been added to the buildpath
	 * @return <b>true</b> if library has been added to the buildpath, otherwise <b>false</b>.
	 */
	public abstract boolean isAdded();
	
	/**
	 * Gets the local file target/location of the jar file
	 * @return local file (target) to library 
	 */
	public abstract File getJarFile();
	
	/**
	 * Gets download url to the library
	 * @return url
	 */
	public abstract URL getDownloadLink();
	
	
	/**
	 * Fetches URL from {@link Library#getJarFile()}
	 * @return URL to local library jar file
	 */
	public URL getJarFileURL() {
		try {
			return getJarFile().toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public abstract String getLibraryName();

}
