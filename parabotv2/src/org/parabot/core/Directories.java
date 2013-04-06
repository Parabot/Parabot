package org.parabot.core;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import org.parabot.environment.OperatingSystem;

/**
 * 
 * @author Clisprail
 * @author Matt
 * 
 */
public class Directories {

	/**
	 * Gets default user directory
	 * @return default user director
	 */
	public static File getDefaultDirectory() {
		switch (OperatingSystem.getOS()) {
		case WINDOWS:
			JFileChooser fr = new JFileChooser();
			FileSystemView fw = fr.getFileSystemView();
			return fw.getDefaultDirectory();
		default:
			return new File(System.getProperty("user.home"));
		}
	}
	
	/**
	 * Gets bot workspace
	 * @return workspace of bot
	 */
	public static File getWorkspace() {
		return new File(getDefaultDirectory(), "/Parabot/");
	}
	
	/**
	 * Get script sources path
	 * @return script sources path
	 */
	public static File getScriptSourcesPath() {
		return new File(getDefaultDirectory(), "/Parabot/scripts/sources/");
	}

	/**
	 * Get script compiled path
	 * @return script compiled path
	 */
	public static File getScriptCompiledPath() {
		return new File(getDefaultDirectory(), "/Parabot/scripts/compiled/");
	}
	
	/**
	 * Gets settings directory
	 * @return settings directory
	 */
	public static File getSettingsPath() {
		return new File(getDefaultDirectory(), "/Parabot/settings/");
	}
	
	/**
	 * Gets servers directory
	 * @return servers directory
	 */
	public static File getServerPath() {
		return new File(getDefaultDirectory(), "/Parabot/servers/");
	}
	
	/**
	 * Validates all directories and makes them if necessary
	 */
	public static void validate() {
		final File defaultPath = getDefaultDirectory();
		if(defaultPath == null || !defaultPath.exists()) {
			throw new RuntimeException("Default path not found");
		}
		final Queue<File> files = new LinkedList<File>();
		files.add(getWorkspace());
		files.add(getServerPath());
		files.add(getSettingsPath());
		files.add(getScriptSourcesPath());
		files.add(getScriptCompiledPath());
		while(files.size() > 0) {
			final File file = files.poll();
			if(!file.exists()) {
				file.mkdirs();
			}
		}
	}

}
