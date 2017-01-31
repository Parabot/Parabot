package org.parabot.core.asm.redirect;

import org.parabot.core.asm.RedirectClassAdapter;

import java.io.IOException;

public class RuntimeRedirect {

    public static Runtime getRuntime() {
        return Runtime.getRuntime();
    }

    public static int availableProcessors(Runtime r) {
        return 2;
    }

    public static long totalMemory(Runtime runtime) {
        return (long) 1024;
    }

    public static long freeMemory(Runtime runtime) {
        return (long) 1024;
    }

    public static Process exec(Runtime r, String s) {
        if (s.contains("ping")) {
            System.out.println("Faked attempted command: " + s);
            try {
                return r.exec("ping 127.0.0.1");
            } catch (IOException e) {
                throw RedirectClassAdapter.createSecurityException();
            }
        } else {
            System.out.println("Blocked attempted command: " + s);
            throw RedirectClassAdapter.createSecurityException();
        }
    }

}
