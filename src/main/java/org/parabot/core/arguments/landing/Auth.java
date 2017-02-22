package org.parabot.core.arguments.landing;

import org.parabot.core.arguments.LandingArgument;
import org.parabot.core.network.proxy.ProxySocket;

/**
 * @author EmmaStone
 */
public class Auth implements LandingArgument {

	@Override
	public String[] getArguments() {
		return new String[]{"auth"};
	}

	@Override
	public void has(Object value) {
		String[] values = value.toString().split(" ");
		ProxySocket.auth = true;
		ProxySocket.setLogin(values[0], values[1]);
	}
}
