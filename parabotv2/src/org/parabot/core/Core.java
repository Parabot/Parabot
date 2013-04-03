package org.parabot.core;

/**
 * 
 * @author Clisprail
 *
 */
public class Core {
	private static boolean devMode = false;
	
	/**
	 * Enables the developers mode
	 */
	public static void enableDevMode() {
		devMode = true;
	}
	
	/**
	 * Determines if bot is in developers mode
	 * @return <b>true</b> if bot is in developers mode
	 */
	public static boolean isDevMode() {
		return devMode;
	}
	

}
