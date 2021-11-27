package org.parabot.environment.api.utils;

import java.math.BigInteger;

/**
 * Helper class for calculating setters for clients that uses multipliers
 *
 * @author Everel
 */
public class Multipliers {

    /**
     * @param multiplier the multiplier
     * @param set        the value you want to set
     *
     * @return the correct setter value
     */
    public static int getIntSetter(int multiplier, int set) {
        int bits = 32;
        BigInteger quotient = new BigInteger(Integer.toString(multiplier));
        BigInteger shift = BigInteger.ONE.shiftLeft(bits);
        int value = quotient.modInverse(shift).intValue();
        return value * set;
    }

    /**
     * @param multiplier the multiplier
     * @param set        the value you want to set
     *
     * @return the correct setter value
     */
    public static long getLongSetter(long multiplier, long set) {
        int bits = 64;
        BigInteger quotient = new BigInteger(Long.toString(multiplier));
        BigInteger shift = BigInteger.ONE.shiftLeft(bits);
        long value = quotient.modInverse(shift).longValue();
        return value * set;
    }

}
