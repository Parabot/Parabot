package org.parabot.core.lib.naga;

import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.build.BuildPath;
import org.parabot.core.lib.Library;

import java.io.File;
import java.net.URL;

/**
 * @author JKetelaar
 */
public class Naga extends Library {

    private static boolean valid;

    @Override
    public void init() {
        if (!hasJar()) {
            System.err.println("Failed to load javafx... [jar missing]");
            return;
        }
        Core.verbose("Adding javafx jar file to build path: "
                + getJarFileURL().getPath());
        BuildPath.add(getJarFileURL());

        try {
            Class.forName("javafx.application.Application");
            valid = true;
        } catch (ClassNotFoundException e) {
            System.err
                    .println("Failed to add javafx to build path, or incorrupt download");
        }

        Core.verbose("JavaFX initialized.");
    }

    @Override
    public boolean isAdded() {
        return valid;
    }

    @Override
    public File getJarFile() {
        return new File(Directories.getCachePath(), "naga.jar");
    }

    @Override
    public URL getDownloadLink() {
        try {
            return new URL("http://bdn.parabot.org/api/v2/data/dependencies/naga");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    @Override
    public String getLibraryName() {
        return "Naga";
    }

    public static boolean isValid() {
        return valid;
    }
}
