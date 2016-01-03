package org.parabot;

import org.objectweb.asm.tree.MethodNode;
import org.parabot.core.Context;
import org.parabot.core.reflect.RefClass;
import org.parabot.core.reflect.RefMethod;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jeroen on 01/01/16.
 */
public class Test {

    private static Class noparams[] = {};
    private static Class[] paramShort = new Class[3];
    private static Class[] paramShort2 = new Class[2];

    static {
        paramShort[0] = int.class;
        paramShort2[0] = int.class;
        paramShort[1] = int.class;
        paramShort2[1] = long.class;
        paramShort[2] = int.class;
    }

    public static void main(String[] args) {
        System.out.println(C5("_aUm\u0017{\\xXzX|Vz"));
    }

    public static String C5(final String a) {
        final int n = 1 << 3;
        final int n2 = (0x2 ^ 0x5) << 3 ^ 0x1;
        final int length = a.length();
        final char[] array = new char[length];
        int n3;
        int i = n3 = length - 1;
        final char[] array2 = array;
        final char c = (char)n2;
        final int n4 = n;
        while (i >= 0) {
            final char[] array3 = array2;
            final int n5 = n3;
            final char char1 = a.charAt(n5);
            --n3;
            array3[n5] = (char)(char1 ^ n4);
            if (n3 < 0) {
                break;
            }
            final char[] array4 = array2;
            final int n6 = n3--;
            array4[n6] = (char)(a.charAt(n6) ^ c);
            i = n3;
        }
        return new String(array2);
    }

    public static String C4(final String a) {
        final int n = 5 << 4 ^ 3 << 1;
        final int n2 = (0x2 ^ 0x5) << 3 ^ (0x2 ^ 0x5);
        final int length = a.length();
        final char[] array = new char[length];
        int n3;
        int i = n3 = length - 1;
        final char[] array2 = array;
        final char c = (char)n2;
        final int n4 = n;
        while (i >= 0) {
            final char[] array3 = array2;
            final int n5 = n3;
            final char char1 = a.charAt(n5);
            --n3;
            array3[n5] = (char)(char1 ^ n4);
            if (n3 < 0) {
                break;
            }
            final char[] array4 = array2;
            final int n6 = n3--;
            array4[n6] = (char)(a.charAt(n6) ^ c);
            i = n3;
        }
        return new String(array2);
    }

    public static String C2(final String a) {
        final int n = 4;
        final int n2 = n << n ^ 2 << 1;
        final int n3 = 3;
        final int n4 = n3 << n3 ^ 0x3;
        final int length = a.length();
        final char[] array = new char[length];
        int n5;
        int i = n5 = length - 1;
        final char[] array2 = array;
        final char c = (char)n4;
        final int n6 = n2;
        while (i >= 0) {
            final char[] array3 = array2;
            final int n7 = n5;
            final char char1 = a.charAt(n7);
            --n5;
            array3[n7] = (char)(char1 ^ n6);
            if (n5 < 0) {
                break;
            }
            final char[] array4 = array2;
            final int n8 = n5--;
            array4[n8] = (char)(a.charAt(n8) ^ c);
            i = n5;
        }
        return new String(array2);
    }

    public static String C(final String a) {
        final int n = 2 << 3 ^ (0x2 ^ 0x5);
        final int n2 = (0x2 ^ 0x5) << 4;
        final int n3 = 2;
        final int n4 = n2 ^ (n3 << n3 ^ 0x1);
        final int length = a.length();
        final char[] array = new char[length];
        int n5;
        int i = n5 = length - 1;
        final char[] array2 = array;
        final char c = (char)n4;
        final int n6 = n;
        while (i >= 0) {
            final char[] array3 = array2;
            final int n7 = n5;
            final char char1 = a.charAt(n7);
            --n5;
            array3[n7] = (char)(char1 ^ n6);
            if (n5 < 0) {
                break;
            }
            final char[] array4 = array2;
            final int n8 = n5--;
            array4[n8] = (char)(a.charAt(n8) ^ c);
            i = n5;
        }
        return new String(array2);
    }

    public static String C3(final String a) {
        final int n = (0x3 ^ 0x5) << 4 ^ (0x2 ^ 0x5);
        final int n2 = (0x3 ^ 0x5) << 4 ^ 3 << 1;
        final int length = a.length();
        final char[] array = new char[length];
        int n3;
        int i = n3 = length - 1;
        final char[] array2 = array;
        final char c = (char)n2;
        final int n4 = n;
        while (i >= 0) {
            final char[] array3 = array2;
            final int n5 = n3;
            final char char1 = a.charAt(n5);
            --n3;
            array3[n5] = (char)(char1 ^ n4);
            if (n3 < 0) {
                break;
            }
            final char[] array4 = array2;
            final int n6 = n3--;
            array4[n6] = (char)(a.charAt(n6) ^ c);
            i = n3;
        }
        return new String(array2);
    }

    public static void invokeD(Class clazz, Object client) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Class<?> c = Context.getInstance().getASMClassLoader()
                .loadClass("a/a/c/q");
        Method method = c.getDeclaredMethod("d", noparams);
        if(method != null){
            method.invoke(c, null);
        }

        System.out.println("InvokeD");
    }

    public static void invokeQC(Class clazz, Object client) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Class<?> c = Context.getInstance().getASMClassLoader()
                .loadClass("a/a/c/q");
        Method method = c.getDeclaredMethod("C", noparams);
        if(method != null){
            method.invoke(c, null);
        }

        System.out.println("InvokeD");
    }

    public static void invokeU(Class clazz, Object client) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException {
        Method method = Context.getInstance().getClient().getClass().getDeclaredMethod("f", noparams);
        if(method != null){
            method.setAccessible(true);
            method.invoke(Context.getInstance().getClient(), null);
        }

        Field fieldEF = Context.getInstance().getClient().getClass().getDeclaredField("eF");
        Field fieldRC = Context.getInstance().getClient().getClass().getDeclaredField("Rc");
        Field fieldCD = Context.getInstance().getClient().getClass().getDeclaredField("cd");

        fieldEF.set(Context.getInstance().getClient(), 0);
        fieldRC.set(Context.getInstance().getClient(), 765);
        fieldCD.set(Context.getInstance().getClient(), 503);

        System.out.println("invokeU");
    }

    public static void invokeXD(Class clazz, Object client) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Class<?> c = Context.getInstance().getASMClassLoader()
                .loadClass("a/a/l/x");
        Method method = c.getDeclaredMethod("d", noparams);
        if(method != null){
            method.invoke(c, null);
        }


        System.out.println("invokeXD");
    }

    /**
     * Settings the size of the client
     * @param clazz
     * @param client
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public static void invokeTB(Class clazz, Object client) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Method method = Context.getInstance().getClient().getClass().getDeclaredMethod("d", paramShort);

        if(method != null){
            method.setAccessible(true);
            method.invoke(Context.getInstance().getClient(), 0, 765, 503);
        }

        System.out.println("invokeTB");
    }
}
