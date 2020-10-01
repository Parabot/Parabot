package org.parabot.core.asm.redirect;

import org.parabot.core.asm.RedirectClassAdapter;

public class ThreadRedirect {

    private static final int count = 0;

    public static void start(Thread t) {
        t.start();
    }

    public static void setPriority(Thread t, int i) {
        t.setPriority(i);
    }

    public static void setDaemon(Thread t, boolean b) {
        t.setDaemon(b);
    }

    public static void interrupt(Thread t) {
        t.interrupt();
    }

    public static Thread currentThread() {
        return new Thread();
    }

    public static void join(Thread t) throws InterruptedException {
        t.join();
    }

    public static void join(Thread t, long l) throws InterruptedException {
        t.join(l);
    }

    public static void join(Thread t, long l, int i) throws InterruptedException {
        t.join(l, i);
    }

    public static ClassLoader getContextClassLoader(Thread t) {
        return null;
    }

    public static ThreadGroup getThreadGroup(Thread t) {
        throw RedirectClassAdapter.createSecurityException();
    }

    public static void setName(Thread t, String name) {
        t.setName(name);
    }

    public static String getName(Thread t) {
        return t.getName();
    }

    public static void sleep(long time) throws InterruptedException {
        Thread.sleep(time);
    }

    public static void setUncaughtExceptionHandler(Thread t, Thread.UncaughtExceptionHandler handler) {
        t.setUncaughtExceptionHandler(handler);
    }

    public static boolean isInterrupted(Thread thread) {
        return thread.isInterrupted();
    }

    public static long getId(Thread thread) {
        return thread.getId();
    }
}
