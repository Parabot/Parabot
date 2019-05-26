package org.parabot;

import org.junit.Assert;
import org.junit.Test;
import org.parabot.core.network.NetworkInterface;

/**
 * @author JKetelaar
 */
public class MacAddressUnitTest {

    @Test
    public void test() {
        String[] macString = new String[]{
                "19",
                "5C",
                "11",
                "19",
                "5C",
                "11"
        };
        NetworkInterface networkInterface = new NetworkInterface();

        byte[] mac = new byte[6];
        for (int j = 0; j < 6; j++) {
            mac[j] = Byte.parseByte(macString[j], 16); // parses a hex number
        }
        NetworkInterface.setMac(mac);

        Assert.assertArrayEquals(networkInterface.getHardwareAddress(), mac);
    }
}
