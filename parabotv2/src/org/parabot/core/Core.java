package org.parabot.core;

import org.parabot.environment.api.utils.WebUtil;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 
 * @author Everel
 * 
 */
public class Core {

	private static boolean debug = false;
	private static boolean verbose = false;

	/**
	 * Enabled debug mode
	 * 
	 * @param debug
	 */
	public static void setDebug(final boolean debug) {
		Core.debug = debug;
	}

	/**
	 * @return if the client is in debug mode.
	 */
	public static boolean inDebugMode() {
		return debug;
	}

	/**
	 * @return if the client is in verbose mode.
	 */
	public static boolean inVerboseMode() {
		return verbose;
	}

	/**
	 * Sets verbose mode
	 * 
	 * @param verbose
	 *            - enabled
	 */
	public static void setVerbose(final boolean verbose) {
		Core.verbose = verbose;
	}

	public static void verbose(final String line) {
		if (verbose) {
			System.out.println(line);
		}
	}

	/**
	 * Checks for updates.
	 * 
	 * @return <b>true</b> if no update is required, otherwise <b>false</b>.
	 */
	public static boolean isValid() {
		Core.verbose("Checking for updates...");
		BufferedReader br = WebUtil.getReader(Configuration.GET_BOT_VERSION);
		try {
			double version = Double.parseDouble(br.readLine());
			if (Configuration.BOT_VERSION == version) {
				Core.verbose("No updates available.");
				return true;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Core.verbose("Updates available...");
		return false;
	}

}
