package org.parabot.environment.api.utils;

/**
 * 
 * Holds various Time utilities
 * 
 * @author Everel
 *
 */
public class Time {
	
	/**
	 * Sleeps for a given amount of time
	 * @param ms
	 */
	public static void sleep(final int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void sleep(final int minumum, final int maximum) {
		try {
			Thread.sleep(Random.between(minumum, maximum));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static long get() {
		return System.currentTimeMillis();
	}

}
