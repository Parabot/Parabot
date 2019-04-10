package org.parabot;

import org.junit.Test;
import org.parabot.core.asm.adapters.AddInterfaceAdapter;
import org.parabot.core.parsers.hooks.XMLHookParser;

import java.util.HashMap;

public class ClassNameTest {

    @Test
    public void name() {
        AddInterfaceAdapter.setAccessorPackage("org.parabot.novea.");
        String x1, s;

        x1 = "%sRenderable";
        s  = XMLHookParser.resolveDesc(x1);
        System.out.println(s);

        x1 = "[[%sRenderable";
        s  = XMLHookParser.resolveDesc(x1);
        System.out.println(s);

        x1 = "[[I";
        s  = XMLHookParser.resolveDesc(x1);
        System.out.println(s);
        System.out.println();

        x1 = "%sRenderable";
        s  = resolveRealFromInter(x1);
        System.out.println(s);

        x1 = "[[%sRenderable";
        s  = resolveRealFromInter(x1);
        System.out.println(s);

        x1 = "[[I";
        s  = resolveRealFromInter(x1);
        System.out.println(s);

    }

    public static String resolveRealFromInter(String returnDesc) {
        String array = "";
        if (returnDesc != null && returnDesc.contains("%s")) {
            StringBuilder str = new StringBuilder();
            if (returnDesc.startsWith("[")) {
                for (int i = 0; i < returnDesc.length(); i++) {
                    if (returnDesc.charAt(i) == '[') {
                        array += '[';
                    }
                }
                returnDesc = returnDesc.replaceAll("\\[", "");
            }
            String key = returnDesc.substring(returnDesc.indexOf("%s") + 2);
            returnDesc = returnDesc.replaceAll(key, "");
            str.append(array)
                    .append('L')
                    .append(String.format(returnDesc,
                            getInterMapValue2(key)))
                    .append(";");
            returnDesc = str.toString();
        }
        return returnDesc;
    }

    private static HashMap<String, String> interfaceMap;

    public static String getInterMapValue2(String key) {
        if (!interfaceMap.containsKey(key))
            throw new RuntimeException("no interface by name: "+key);
        return interfaceMap.get(key);
    }

    static {
        interfaceMap = new HashMap<>(0);
        interfaceMap.put("Renderable", "bL");
    }
}
