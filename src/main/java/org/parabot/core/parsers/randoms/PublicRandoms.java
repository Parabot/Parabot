package org.parabot.core.parsers.randoms;

import org.parabot.api.io.WebUtil;
import org.parabot.core.Configuration;
import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.io.NoProgressListener;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author JKetelaar
 */
public class PublicRandoms extends RandomParser {

    private final String fileName = ((Configuration.BOT_VERSION.isNightly()) ? "randoms-nightly.jar" : "randoms.jar");

    @Override
    public void parse() {

        final File destination = new File(Directories.getCachePath() + File.separator + fileName);
        final URL overrideDownload = Context.getInstance().getServerProviderInfo().getRandoms();
        if (overrideDownload == null) {
            throw new NullPointerException("Unable to grab URL for Randoms jar. Default URL for BDN randoms must have changed!");
        }

        Core.verbose(String.format("[%s] Destination: %s | dl: %s", getClass().getSimpleName(), destination, overrideDownload));

        if (!destination.exists() || !destination.canRead()) {
            Core.verbose(String.format("[%s] Missing %s - downloading from %s...", getClass().getSimpleName(), destination.getAbsolutePath(), overrideDownload));
            download(destination, overrideDownload);
        }
        try {
            URL url = destination.toURI().toURL();
            URL[] urls = new URL[]{ url };
            String server = Context.getInstance().getServerProviderInfo().getServerName();

            URLClassLoader child = new URLClassLoader(urls, this.getClass().getClassLoader());
            Class<?> classToLoad = Class.forName("org.parabot.randoms.Core", true, child);
            Method method = classToLoad.getDeclaredMethod("init", String.class);
            Object instance = classToLoad.newInstance();
            Core.verbose(String.format("[%s] %s %s", getClass().getSimpleName(), "Initing core Randoms for", server));
            method.invoke(instance, server);
            Core.verbose("Successfully parsed public random!");
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException | MalformedURLException e) {
            e.printStackTrace();
            Core.verbose("Failed to parse random...");
        }
    }

    private void download(final File destination, URL downloadLink) {
        try {
            if (destination.exists()) {
                Core.verbose("Public random dependency already exists, no need to download it...");
                return;
            }

            WebUtil.downloadFile(downloadLink, destination, new NoProgressListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
