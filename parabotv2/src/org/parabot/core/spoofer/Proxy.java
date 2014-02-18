package org.parabot.core.spoofer;

/**
 * User: Jeroen
 * Date: 18/02/14
 * Time: 16:54
 */
public class Proxy {
    public static void setProxy(String host, int port){
        System.getProperties().put("proxyHost", host);
        System.getProperties().put("proxyPort", port);
    }
}
