package org.parabot.core.asm.redirect;

import org.parabot.core.Core;
import org.parabot.core.asm.RedirectClassAdapter;
import org.parabot.environment.scripts.Script;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;

public class ClassRedirect {

    public static ProtectionDomain getProtectionDomain(final Class<?> clazz) {
        System.err.println(clazz.getName() + " getProtectionDomain request granted.");

        return AccessController.doPrivileged(new PrivilegedAction<ProtectionDomain>() {
            public ProtectionDomain run() {
                return clazz.getProtectionDomain();
            }
        });
    }

    public static Object newInstance(Class<?> c) throws IllegalAccessException, InstantiationException {
        if (validStack() || validRequest(c)) {
            return c.newInstance();
        }

        System.err.println(c.getName() + ".newInstance() Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Field getDeclaredField(Class<?> c, String s) throws NoSuchFieldException, SecurityException {
        if (validStack() || validRequest(c)) {
            return c.getDeclaredField(s);
        }

        System.err.println(c.getName() + "." + c.getDeclaredField(s) + " Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Method getDeclaredMethod(Class<?> c, String name, Class<?>... params) throws
            NoSuchMethodException,
            SecurityException {
        if (validStack() || validRequest(c)) {
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

    public static URL getResource(Class<?> c, String path) {
        if (validStack() || validRequest(c)) {
            return c.getResource(path);
        }

        System.err.println(c.getName() + "#getResource(" + path + ") Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static ClassLoader getClassLoader(Class<?> c) {
        System.err.println(c.getName() + "#getClassLoader()" + " Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Field[] getDeclaredFields(Class<?> c) {
        if (validStack() || validRequest(c)) {
            return c.getDeclaredFields();
        }
        System.err.println(c.getName() + "#getDeclaredFields()" + " Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Method[] getDeclaredMethods(Class<?> c) {
        if (validStack() || validRequest(c)) {
            return c.getDeclaredMethods();
        }
        System.err.println(c.getName() + "#getDeclaredMethods()" + " Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Field[] getFields(Class<?> c) {
        if (validStack() || validRequest(c)) {
            return c.getFields();
        }
        System.err.println(c.getName() + "#getFields()" + " Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Annotation[] getAnnotations(Class<?> c) {
        if (validStack() || validRequest(c)) {
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

    public static Method getMethod(Class<?> c, String name, Class<?>... params) throws
            NoSuchMethodException,
            SecurityException {
        if (validStack() || validRequest(c)) {
            return c.getMethod(name, params);
        }
        System.err.println(c.getName() + "#getMethod()" + " Blocked.");
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Field getField(Class<?> c, String name)
            throws NoSuchFieldException, SecurityException {
        if (validStack() || validRequest(c)) {
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

    public static Type getGenericSuperclass(Class c) {
        return c.getGenericSuperclass();
    }

    public static boolean isArray(Class c) {
        return c.isArray();
    }

    public static int getModifiers(Class c) {
        return c.getModifiers();
    }

    public static Class getEnclosingClass(Class c) {
        return c.getEnclosingClass();
    }

    public static boolean isPrimitive(Class c) {
        return c.isPrimitive();
    }

    public static boolean isAssignableFrom(Class c1, Class c2) {
        return c1.isAssignableFrom(c2);
    }

    public static boolean isAnonymousClass(Class c) {
        return c.isAnonymousClass();
    }

    public static boolean isLocalClass(Class c) {
        return c.isLocalClass();
    }

    public static boolean isInterface(Class c) {
        return c.isInterface();
    }

    public static Class[] getInterfaces(Class c) {
        return c.getInterfaces();
    }

    public static Type[] getGenericInterfaces(Class c) {
        return c.getGenericInterfaces();
    }

    public static TypeVariable[] getTypeParameters(Class c) {
        return c.getTypeParameters();
    }

    public static Annotation getAnnotation(Class c, Class annotationClass) {
        return c.getAnnotation(annotationClass);
    }

    public static Constructor getDeclaredConstructor(Class c, Class[] parameterTypes) throws
            NoSuchMethodException,
            SecurityException {
        return c.getDeclaredConstructor(parameterTypes);
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

    private static boolean validRequest(Class c) {
        Core.verbose("Got request for class: " + c.getName());
        return !c.getName().toLowerCase().contains("parabot");
    }
}
