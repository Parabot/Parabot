package org.parabot.core.proxy;

import java.util.HashMap;

import org.objectweb.asm.commons.Remapper;

public class ClassRemapper extends Remapper {
	private static HashMap<String, String> remapNames = new HashMap<String, String>();
	static {
		remapNames.put("java/net/Socket", "org/matt123337/proxy/ProxySocket");
		remapNames.put("java/net/NetworkInterface", "org/matt123337/spoofer/NetworkInterface");
		remapNames.put("java/lang/Runtime", "org/matt123337/spoofer/Runtime");
	}

	@Override
	public String map(String str) {
		String s = remapNames.get(str);
		if (s != null) {
			return s;
		} else {
			return str;
		}
	}
}