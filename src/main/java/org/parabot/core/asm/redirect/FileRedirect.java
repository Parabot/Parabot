package org.parabot.core.asm.redirect;

import org.parabot.core.Core;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

/**
 * @author JKetelaar
 */
public class FileRedirect extends File {

    private static final ArrayList<String> cachedFiles = new ArrayList<>();

    public FileRedirect(String pathname) {
        super(pathname);
        System.out.println(pathname);
    }

    public FileRedirect(String parent, String child) {
        super(parent, child);
        sout(parent);
        sout(child);
    }

    public FileRedirect(File parent, String child) {
        super(parent, child);
        sout(parent.toString());
        sout(child);
    }

    public FileRedirect(URI uri) {
        super(uri);
        sout(uri.toString());
    }

    public static boolean exists(File file) {
        sout(file.toString());
        return file.exists();
    }

    public static boolean isFile(File file) {
        sout(file.toString());
        return file.isFile();
    }

    public static long length(File file) {
        sout(file.toString());
        return file.length();
    }

    public static boolean mkdirs(File file) {
        sout(file.toString());
        return file.mkdirs();
    }

    public static boolean mkdir(File file) {
        sout(file.toString());
        return file.mkdir();
    }

    public static boolean isDirectory(File file) {
        sout(file.toString());
        return file.isDirectory();
    }

    public static String getAbsolutePath(File file) {
        sout(file.toString());
        return file.getAbsolutePath();
    }

    public static File getAbsoluteFile(File file) {
        sout(file.toString());
        return file.getAbsoluteFile();
    }

    public static File[] listFiles(File file) {
        sout(file.toString());
        return file.listFiles();
    }

    public static String getName(File file) {
        sout(file.getName());
        return file.getName();
    }

    private static void sout(String s) {
        if (!cachedFiles.contains(s)) {
            Core.verbose("Server requested file: " + s);
            cachedFiles.add(s);
        }
    }

}
