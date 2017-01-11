package org.parabot.core.lib.jpushbullet;

import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.build.BuildPath;
import org.parabot.core.lib.Library;

import java.io.File;
import java.net.URL;

/**
 * @author EmmaStone
 */
public class JPushBullet extends Library {
    private static boolean valid;

    @Override
    public void init() {
        if (!hasJar()) {
            System.err.println("Failed to load jpushbullet... [jar missing]");
            return;
        }
        Core.verbose("Adding jpushbullet jar file to build path: "
                + getJarFileURL().getPath());
        BuildPath.add(getJarFileURL());

        try {
            Class.forName("com.shakethat.jpushbullet.net.PushbulletClient");
            valid = true;
        } catch (ClassNotFoundException e) {
            System.err
                    .println("Failed to add jpushbullet to build path, or incorrupt download");
        }

        Core.verbose("JPushBullet initialized.");
    }

    @Override
    public boolean isAdded() {
        return valid;
    }

    @Override
    public File getJarFile() {
        return new File(Directories.getCachePath(), "jpushbullet.jar");
    }

    @Override
    public URL getDownloadLink() {
        try {
            return new URL("https://github.com/silk8192/jpushbullet/releases/download/1.0/jpushbullet-1.0.jar");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean requiresJar() {
        return false;
    }

    @Override
    public String getLibraryName() {
        return "JPushBullet";
    }

    public static boolean isValid() {
        return valid;
    }
}
