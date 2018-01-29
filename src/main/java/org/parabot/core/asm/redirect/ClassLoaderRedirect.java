package org.parabot.core.asm.redirect;

import org.parabot.core.Core;
import org.parabot.core.asm.RedirectClassAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

public class ClassLoaderRedirect extends ClassLoader {

    static int count = 0;

    public static Class<?> loadClass(ClassLoader c, String name) {
        throw RedirectClassAdapter.createSecurityException();
    }

    public static ClassLoader getParent(ClassLoader c) {
        throw RedirectClassAdapter.createSecurityException();
    }

    public static URL getResource(ClassLoader classLoader, String name) {
        Core.verbose("#getResource requested for ClassLoaderRedirect (" + name + ")");
        return classLoader.getResource(name);
    }

    public static Enumeration<URL> getResources(ClassLoader classLoader, String name) throws IOException {
        Core.verbose("#getResource requested for ClassLoaderRedirect (" + name + ")");
        return classLoader.getResources(name);
    }

    public static InputStream getResourceAsStream(ClassLoader classLoader, String name) {
        Core.verbose("#getResourceAsStream requested for ClassLoaderRedirect (" + name + ")");
        return classLoader.getResourceAsStream(name);
    }

    public static void setDefaultAssertionStatus(ClassLoader classLoader, boolean enabled) {

    }

    public static void setPackageAssertionStatus(ClassLoader classLoader, String packageName, boolean enabled) {

    }

    public static void setClassAssertionStatus(ClassLoader classLoader, String className, boolean enabled) {

    }

    public static void clearAssertionStatus(ClassLoader classLoader) {

    }
}
