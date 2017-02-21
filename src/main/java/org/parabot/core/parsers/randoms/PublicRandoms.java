package org.parabot.core.parsers.randoms;

import org.parabot.api.io.Directories;
import org.parabot.api.io.WebUtil;
import org.parabot.core.settings.Configuration;
import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.core.bdn.api.APIConfiguration;
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

    private String fileName = ((Configuration.BOT_VERSION.isNightly()) ? "randoms-nightly.jar" : "randoms.jar");

    @Override
    public void parse() {
        File myJar = new File(Directories.getCachePath() + File.separator + fileName);
        if (!myJar.exists() || !myJar.canRead()) {
            download();
        }
        try {
            URL url = myJar.toURI().toURL();
            URL[] urls = new URL[]{url};
            String server = Core.getInjector().getInstance(Context.class).getServerProvider().getServerDescription().getServerName();

            URLClassLoader child = new URLClassLoader(urls, this.getClass().getClassLoader());
            Class<?> classToLoad = Class.forName("org.parabot.randoms.Core", true, child);
            Method method = classToLoad.getDeclaredMethod("init", String.class);
            Object instance = classToLoad.newInstance();
            System.out.println(server);
            method.invoke(instance, server);
            Core.verbose("Successfully parsed public random!");
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException | MalformedURLException e) {
            e.printStackTrace();
            Core.verbose("Failed to parse random...");
        }
    }

    private void download() {
        try {
            File random = new File(Directories.getCachePath() + File.separator + fileName);
            if (random.exists()) {
                Core.verbose("Public random dependency already exists, no need to download it...");
                return;
            }

            String downloadLink = String.format(APIConfiguration.DOWNLOAD_RANDOMS, Configuration.BOT_VERSION.isNightly());

            WebUtil.downloadFile(new URL(downloadLink), random, new NoProgressListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
