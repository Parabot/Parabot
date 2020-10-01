package org.parabot.core.asm.redirect;

import org.parabot.core.Core;

import java.io.InputStream;
import java.io.PrintStream;

public class SystemRedirect {

    public static PrintStream out = System.out;
    public static PrintStream err = System.err;
    public static InputStream in = System.in;

    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static void arraycopy(Object o1, int i1, Object o2, int i2, int i3) {
        System.arraycopy(o1, i1, o2, i2, i3);
    }

    public static void exit(int i) {
        System.exit(i);
    }

    public static String getProperty(String s) {
        String value;
        switch (s) {
            case "java.class.path":
                value = getClassPath();
                break;
            default:
                value = System.getProperty(s);
                break;
        }

        Core.verbose(String.format("GetSystemProp %s = %s", s, value));
        return value;
    }

    public static String getProperty(String s, String s2) {
        String value = null;
        switch (s2) {
            case "java.class.path":
                value = getClassPath();
                break;
        }

        if (value == null) {
            switch (s) {
                case "java.class.path":
                    value = getClassPath();
                    break;
                default:
                    value = System.getProperty(s);
                    break;
            }
        }

        Core.verbose(String.format("GetSystemProp %s = %s", s, value));
        return value;
    }

    public static void gc() {
        System.gc();
    }

    public static String setProperty(String s1, String s2) {
        Core.verbose(String.format("SetSystemProp %s = %s", s1, s2));
        return System.setProperty(s1, s2);
    }

    public static String getenv(String string) {
        Core.verbose(String.format("getEnv %s = %s", string, System.getenv(string)));
        return System.getenv(string);
    }

    public static void setOut(PrintStream printStream) {

    }

    public static void setErr(PrintStream printStream) {

    }

    public static void setIn(PrintStream printStream) {

    }

    public static void runFinalization() {

    }

    public static long nanoTime() {
        return System.nanoTime();
    }

    private static String getClassPath() {
        String classPath = System.getProperty("java.class.path");
        StringBuilder finalClassPath = new StringBuilder();
        for (String path : classPath.split(":")) {
            if (!path.toLowerCase().contains("parabot")) {
                finalClassPath.append(path).append(":");
            }
        }

        return finalClassPath.toString();
    }

}
