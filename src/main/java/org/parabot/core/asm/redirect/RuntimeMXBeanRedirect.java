package org.parabot.core.asm.redirect;

import org.parabot.core.Core;

import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;

public class RuntimeMXBeanRedirect {
    public static List getInputArguments(RuntimeMXBean runtimeMXBean) {
        Core.verbose("Faking RuntimeMXBean#getInputArguments");

        List<Object> list = new ArrayList<>();
        list.add("-Dsun.java2d.noddraw=true");
        list.add("-Xmx420m");

        return list;
    }
}
