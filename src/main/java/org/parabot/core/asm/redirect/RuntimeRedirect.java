package org.parabot.core.asm.redirect;

import org.parabot.api.calculations.Random;
import org.parabot.core.Core;
import org.parabot.core.asm.RedirectClassAdapter;

import java.io.IOException;
import java.util.Arrays;

public class RuntimeRedirect {

    public static Runtime getRuntime() {
        return Runtime.getRuntime();
    }

    public static int availableProcessors(Runtime r) {
        return Random.between(1, 4);
    }

    public static long totalMemory(Runtime runtime) {
        return Random.between(1024, 4096);
    }

    public static long freeMemory(Runtime runtime) {
        return Random.between(1024, 4096);
    }

    public static Process exec(Runtime r, String[] s) {
        Core.verbose("Blocked attempted command: " + Arrays.toString(s));
        throw RedirectClassAdapter.createSecurityException();
    }

    public static Process exec(Runtime r, String s) {
        if (s.contains("ping")) {
            Core.verbose("Faked attempted command: " + s);
            try {
                return r.exec("ping 8.8.8.8");
            } catch (IOException e) {
                throw RedirectClassAdapter.createSecurityException();
            }
        } else {
            Core.verbose("Blocked attempted command: " + s);
            throw RedirectClassAdapter.createSecurityException();
        }
    }

}
