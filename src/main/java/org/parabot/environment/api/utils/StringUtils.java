package org.parabot.environment.api.utils;

import org.parabot.api.misc.StringUtil;

/**
 * @author mkyong, JKetelaar
 */
public class StringUtils extends StringUtil {
    public static String toMD5(String md5) {
        try {
            java.security.MessageDigest md    = java.security.MessageDigest.getInstance("MD5");
            byte[]                      array = md.digest(md5.getBytes());
            StringBuilder               sb    = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
