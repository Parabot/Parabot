package org.parabot.core.arguments.landing;

import org.parabot.api.translations.TranslationHelper;
import org.parabot.core.arguments.LandingArgument;
import org.parabot.core.network.proxy.ProxySocket;
import org.parabot.core.network.proxy.ProxyType;

/**
 * @author EmmaStone
 */
public class Proxy implements LandingArgument {

	@Override
	public String[] getArguments() {
		return new String[]{"proxy"};
	}

	@Override
	public void has(Object value) {
		String[] values = value.toString().split(" ");
		int i = 0;

		try{
			ProxyType type = ProxyType.valueOf(values[++i].toUpperCase());
			ProxySocket.setProxy(type, values[++i], Integer.parseInt(values[++i]));
		}catch (IllegalArgumentException e){
			System.err.println(TranslationHelper.translate("INVALID_PROXY_TYPE") + values[i]);
			System.exit(1);
		}

	}
}
