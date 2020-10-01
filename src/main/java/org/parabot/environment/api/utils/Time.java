package org.parabot.environment.api.utils;

import org.parabot.environment.scripts.framework.SleepCondition;

/**
 * Holds various Time utilities
 *
 * @author Everel
 */
public final class Time {

    /**
     * Sleeps for a given amount of time
     *
     * @param ms
     */
    public static void sleep(final int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param minumum
     * @param maximum
     */
    public static void sleep(final int minumum, final int maximum) {
        try {
            Thread.sleep(Random.between(minumum, maximum));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sleeps until the SleepCondition is valid.
     *
     * @param conn    the condition.
     * @param timeout the time in miliseconds before it stops sleeping.
     *
     * @return whether it ran successfully without timing out.
     */
    public static boolean sleep(SleepCondition conn, int timeout) {
        long start = System.currentTimeMillis();
        while (!conn.isValid()) {
            if (start + timeout < System.currentTimeMillis()) {
                return false;
            }
            Time.sleep(50);
        }
        return true;
    }

    /**
     * Sleeps until SleepCondition is valid, but with a minimum timeout.
     *
     * @param conn           the condition.
     * @param timeout        the time in milliseconds before it stops sleeping.
     * @param minimumTimeout the minimum time to sleep.
     *
     * @return whether it ran successfully without timing out.
     */
    public static boolean sleep(SleepCondition conn, int timeout, int minimumTimeout) {
        long start = System.currentTimeMillis();

        if (!sleep(conn, timeout)) {
            return false;
        }

        long t;
        if ((t = System.currentTimeMillis() - start) < minimumTimeout) {
            Time.sleep((int) (minimumTimeout - t));
        }

        return true;
    }

    /**
     * Gets current time in milliseconds
     *
     * @return time in ms
     */
    public static long get() {
        return System.currentTimeMillis();
    }

}
