package org.parabot.core.arguments.landing;

import com.sun.istack.internal.Nullable;
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
	public void has(@Nullable Object value) {
		String[] values = value.toString().split(" ");
		int i = 0;

		ProxyType type = ProxyType.valueOf(values[++i].toUpperCase());
		if (type == null) {
			System.err.println(TranslationHelper.translate("INVALID_PROXY_TYPE") + values[i]);
			System.exit(1);
			return;
		}

		ProxySocket.setProxy(type, values[++i], Integer.parseInt(values[++i]));
	}
}
