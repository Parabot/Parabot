package org.parabot.core.spoofing;

/**
 * User: Jeroen
 * Date: 27/11/13
 * Time: 16:04
 */
public class Ip {
    public static void spoofIP(String host, String port) {
        System.getProperties().setProperty("socksProxySet", "true");
        System.getProperties().setProperty("socksProxyHost", host);
        System.getProperties().setProperty("socksProxyPort", port);
    }
}