package org.parabot.core.asm.redirect;

import org.parabot.core.Core;
import org.parabot.core.asm.RedirectClassAdapter;
import org.parabot.environment.scripts.Script;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassRedirect {

    public static Object newInstance(Class<?> c) throws IllegalAccessException, InstantiationException {
        if (validStack()) {
            return c.newInstance();
        }

        System.err.println(c.getName() + ".newInstance() Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Field getDeclaredField(Class<?> c, String s) throws NoSuchFieldException, SecurityException {
        if (validStack()) {
            return c.getDeclaredField(s);
        }

        System.err.println(c.getName() + "." + c.getDeclaredField(s) + " Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Method getDeclaredMethod(Class<?> c, String name, Class<?>... params) throws NoSuchMethodException, SecurityException {
        if (validStack()) {
            return c.getDeclaredMethod(name, params);
        }

        System.err.println(c.getName() + "#" + c.getDeclaredMethod(name, params) + " Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Class<?> forName(String name) throws ClassNotFoundException {
        if (name.contains("parabot")) {
            throw new ClassNotFoundException();
        }
        Core.verbose("Received #forName(" + name + ") call");
        return Class.forName(name);
    }

    public static ClassLoader getClassLoader(Class<?> c) {
        System.err.println(c.getName() + "#getClassLoader()" + " Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Field[] getDeclaredFields(Class<?> c) {
        if (validStack()) {
            return c.getDeclaredFields();
        }
        System.err.println(c.getName() + "#getDeclaredFields()" + " Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Method[] getDeclaredMethods(Class<?> c) {
        if (validStack()) {
            return c.getDeclaredMethods();
        }
        System.err.println(c.getName() + "#getDeclaredMethods()" + " Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Field[] getFields(Class<?> c) {
        if (validStack()) {
            return c.getFields();
        }
        System.err.println(c.getName() + "#getFields()" + " Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Annotation[] getAnnotations(Class<?> c) {
        if (validStack()) {
            return c.getAnnotations();
        }
        System.err.println(c.getName() + "#getFields()" + " Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Method[] getMethods(Class<?> c) {
        if (validStack()) {
            return c.getMethods();
        }
        System.err.println(c.getName() + "#getMethods()" + " Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Method getMethod(Class<?> c, String name, Class<?>... params) throws NoSuchMethodException, SecurityException {
        if (validStack()) {
            return c.getMethod(name, params);
        }
        System.err.println(c.getName() + "#getMethod()" + " Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Field getField(Class<?> c, String name)
            throws NoSuchFieldException, SecurityException {
        if (validStack()) {
            return c.getField(name);
        }
        System.err.println(c.getName() + "#getField()" + " Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static String getName(Class<?> c) {
        if (c.getName().contains("parabot")) {
            return "java.lang.String";
        }
        return c.getName();
    }

    public static InputStream getResourceAsStream(Class<?> c, String res) {
        if (validStack()) {
            return c.getResourceAsStream(res);
        }
        if (res.contains(".class")) {
            throw RedirectClassAdapter.createSecurityException();
        }
        return c.getResourceAsStream(res);
    }

    public static boolean desiredAssertionStatus(Class<?> c) {
        if (validStack()) {
            return c.desiredAssertionStatus();
        }

        return !c.getName().contains("parabot") && c.desiredAssertionStatus();
    }

    private static boolean validStack() {
        Exception e = new Exception();
        for (StackTraceElement elem : e.getStackTrace()) {
            if (elem.getClassName().equals(Script.class.getName())) {
                return true;
            }
        }
        return false;
    }
}
