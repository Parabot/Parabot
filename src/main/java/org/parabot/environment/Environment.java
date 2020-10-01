package org.parabot.environment;

import org.parabot.core.Core;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.lib.Library;
import org.parabot.core.lib.javafx.JavaFX;
import org.parabot.core.parsers.servers.ServerParser;
import org.parabot.core.ui.components.VerboseLoader;
import org.parabot.environment.api.utils.WebUtil;

import java.util.LinkedList;

/**
 * Initializes the bot environment
 *
 * @author Everel, JKetelaar
 */
public class Environment extends org.parabot.api.io.libraries.Environment {

    private static final LinkedList<Library> libs = new LinkedList<>();

    static {
        libs.add(new JavaFX());
    }

    /**
     * Loads a new environment
     *
     * @param desc
     */
    public static void load(final ServerDescription desc) {
        for (Library lib : libs) {
            loadLibrary(lib, true);
        }

        Core.verbose("[Environment] Loading server: " + desc.toString() + "...");

        ServerParser.SERVER_CACHE.get(desc).run();
    }

    /**
     * Loads library into environment
     *
     * @param library
     * @param verboseLoader defines if verboseLoader should be enabled
     */
    public static void loadLibrary(Library library, boolean verboseLoader) {
        if (library.requiresJar()) {
            if (!library.hasJar()) {
                Core.verbose("Downloading " + library.getLibraryName() + "...");
                if (verboseLoader) {
                    VerboseLoader.setState("Downloading " + library.getLibraryName() + "...");
                }
                WebUtil.downloadFile(library.getDownloadLink(), library.getJarFile(), VerboseLoader.get());
                Core.verbose("Downloaded " + library.getLibraryName() + ".");
            }
            Core.verbose("Initializing " + library.getLibraryName());
            library.init();
        }
    }
}
