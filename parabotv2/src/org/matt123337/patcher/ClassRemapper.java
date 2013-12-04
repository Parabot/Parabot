package org.matt123337.patcher;

import org.objectweb.asm.commons.Remapper;

import java.util.HashMap;

public class ClassRemapper extends Remapper {
    private static HashMap<String, String> remapNames = new HashMap<String, String>();
    static {
        remapNames.put("java/net/Socket", "org/matt123337/proxy/ProxySocket");
        remapNames.put("java/net/NetworkInterface",
                "org/matt123337/spoofer/NetworkInterface");
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