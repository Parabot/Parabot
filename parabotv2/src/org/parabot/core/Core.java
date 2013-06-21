package org.parabot.core;

/**
 * 
 * @author Clisprail
 * 
 */
public class Core {

	private static boolean debug = false;

	public static void setDebug(boolean debug) {
		Core.debug = debug;
	}

	/**
	 * @return if the botclient is in debug mode.
	 */
	public static boolean inDebugMode() {
		return debug;
	}

}
