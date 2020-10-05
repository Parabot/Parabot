package org.parabot.core.asm;

import org.objectweb.asm.commons.Remapper;

import java.util.HashMap;

public class ClassRemapper extends Remapper {
    private static final HashMap<String, String> remapNames = new HashMap<String, String>();

    static {
        remapNames.put("java/net/Socket", "org/parabot/core/network/proxy/ProxySocket");
        remapNames.put("java/net/NetworkInterface", "org/parabot/core/network/NetworkInterface");
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