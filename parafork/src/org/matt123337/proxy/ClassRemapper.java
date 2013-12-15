package org.matt123337.proxy;

import java.util.HashMap;

import org.objectweb.asm.commons.Remapper;

public class ClassRemapper extends Remapper {
	private static HashMap<String, String> remapNames = new HashMap<String, String>();
	static {
		remapNames.put("java/net/Socket", "org/matt123337/proxy/ProxySocket");
	}

	@Override
	public String map(String str) {
		String s = remapNames.get(str);
		if (s != null) {
			System.out.println("[CLASS_REMAP] " + str + " => " + s);
			return s;
		} else {
			return str;
		}
	}
}