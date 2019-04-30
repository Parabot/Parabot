package org.parabot;

import org.junit.Test;
import org.parabot.core.asm.adapters.AddInterfaceAdapter;
import org.parabot.core.parsers.hooks.XMLHookParser;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ClassNameTest {

    @Test
    public void name() {
        AddInterfaceAdapter.setAccessorPackage("org.parabot.novea.");
        String x1, s;

        // Here we simply insert the Accessor Package to string, no swapping Accessor for Gamepack Class
        x1 = "%sRenderable";
        s  = XMLHookParser.resolveDesc(x1);
        assertEquals(s, "Lorg.parabot.novea.Renderable;");

        x1 = "[[%sRenderable";
        s  = XMLHookParser.resolveDesc(x1);
        assertEquals(s, "[[Lorg.parabot.novea.Renderable;");

        x1 = "[[I";
        s  = XMLHookParser.resolveDesc(x1);
        assertEquals(s, "[[I");

        // Here we want to get the real client's class name that implements our Accessor
        x1 = "%sRenderable";
        s  = resolveRealFromInter(x1);
        assertEquals(s, "LbL;");

        x1 = "[[%sRenderable";
        s  = resolveRealFromInter(x1);
        assertEquals(s, "[[LbL;");

        x1 = "[[I";
        s  = resolveRealFromInter(x1);
        assertEquals(s, "[[I");

        x1 = "[[%sRenderable";
        s  = resolveRealFromInter(x1, true);
        assertEquals(s, "[[bL");

        x1 = "[[%sRenderable";
        s  = resolveDesc(x1);
        assertEquals(s, "[[Lorg.parabot.novea.Renderable;");
    }

    public static String resolveRealFromInter(String returnDesc) {
        return resolveRealFromInter(returnDesc, false);
    }

    public static String resolveRealFromInter(String returnDesc, boolean ignoreClassPrefix) {
        String array = "";
        final String old = returnDesc;
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
            str.append(array);
            if (!ignoreClassPrefix)
                str.append('L');
            str.append(String.format(returnDesc, getInterMapValue2(key)));
            if (!ignoreClassPrefix)
                str.append(";");
            returnDesc = str.toString();
            System.out.println("[resolveReal] "+old+" -> "+returnDesc);
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

    public static String resolveDesc(String returnDesc) {
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
            str.append(array)
                    .append('L')
                    .append(String.format(returnDesc,
                            AddInterfaceAdapter.getAccessorPackage()))
                    .append(";");
            returnDesc = str.toString();
        }
        return returnDesc;
    }
}
