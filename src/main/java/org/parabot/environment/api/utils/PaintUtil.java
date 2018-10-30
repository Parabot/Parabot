package org.parabot.environment.api.utils;

import java.text.DecimalFormat;

/**
 * Functions frequently used for displaying data on paints
 *
 * @author AlexanderBielen
 */
public class PaintUtil {

    /**
     * Calculates how much given variable has gained per hour
     *
     * @param currentAmount total gained amount
     * @param start         time from which to start counting in milliseconds
     * @return              rate per hour
     */
    public static long calculatePerHour(int currentAmount, long start) {
        return calculatePerHour(currentAmount, 0, start);
    }

    /**
     * Calculates how much given variable has gained per hour, with variable start amount
     *
     * @param currentAmount total gained amount
     * @param startAmount   start amount
     * @param start         time from which to start counting in milliseconds
     * @return              rate per hour
     */
    public static long calculatePerHour(int currentAmount, int startAmount, long start) {
        return (int)(((double)(currentAmount - startAmount) * 3600000D) / (double)(System.currentTimeMillis() - start));
    }

    /**
     * Formats a time difference from given timestamp till present into readable hh:mm:ss format
     *
     * @param start start time in milliseconds
     * @return      readable timestamp
     */
    public static String formatRunTime(long start) {
        DecimalFormat df = new DecimalFormat("00");
        long currentTime = System.currentTimeMillis() - start;
        long hours = currentTime / (3600000);
        currentTime -= hours * (3600000);
        long minutes = currentTime / (60000);
        currentTime -= minutes * (60000);
        long seconds = currentTime / (1000);
        return df.format(hours) + ":" + df.format(minutes) + ":" + df.format(seconds);
    }
}