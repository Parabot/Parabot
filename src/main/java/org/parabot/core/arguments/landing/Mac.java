package org.parabot.core.arguments.landing;

import org.parabot.core.arguments.LandingArgument;
import org.parabot.core.network.NetworkInterface;

/**
 * @author EmmaStone
 */
public class Mac implements LandingArgument {

	@Override
	public String[] getArguments() {
		return new String[]{"mac"};
	}

	@Override
	public void has(Object value) {
		String[] values = value.toString().split(" ");

		//TODO I don't even know if this works

		byte[] mac = new byte[6];
		String str = values[0];
		if (str.toLowerCase().equals("random")) {
			new java.util.Random().nextBytes(mac);
		} else {
			int i = 0;
			for (int j = 0; j < 6; j++) {
				mac[j] = Byte.parseByte(values[++i], 16); // parses a hex number
			}
		}

		NetworkInterface.setMac(mac);
	}
}
