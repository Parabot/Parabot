package org.parabot.core.proxy;

import socks.Socks4Proxy;
import socks.Socks5Proxy;

import java.net.UnknownHostException;

/**
 * User: Jeroen
 * Date: 18/02/14
 * Time: 16:54
 */
public class Proxy {
    public static Socks5Proxy proxy5;
    public static Socks4Proxy proxy4;

    public static void setProxy4() throws UnknownHostException {

    }

    public static void setProxy5() throws UnknownHostException {
        proxy5 = new Socks5Proxy("80.63.56.146", 1080);
    }

    public static void main(String[] args) throws UnknownHostException {
        setProxy5();
    }
}
