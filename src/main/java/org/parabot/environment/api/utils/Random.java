package org.parabot.environment.api.utils;

/**
 * A random class is used for generating random numbers
 *
 * @author Everel
 */
public class Random {
    private final static java.util.Random RANDOM = new java.util.Random();

    /**
     * Randomizes a number between minimum and maximum
     *
     * @param min
     * @param max
     * @return randomized number
     */
    public static int between(final int min, final int max) {
        try {
            return min + (max == min ? 0 : RANDOM.nextInt(max - min));
        } catch (Exception e) {
            return min + (max - min);
        }
    }

}
