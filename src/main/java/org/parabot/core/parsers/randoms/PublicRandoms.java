package org.parabot.core.parsers.randoms;

import org.parabot.core.Configuration;
import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.io.NoProgressListener;
import org.parabot.environment.api.utils.WebUtil;

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

    @Override
    public void parse() {
        File myJar = new File(Directories.getCachePath() + "/randoms.jar");
        if (!myJar.exists() || !myJar.canRead()) {
            download();
        }
        try {
            URL url = myJar.toURI().toURL();
            URL[] urls = new URL[]{url};
            String server = Context.getInstance().getServerProviderInfo().getServerName();

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
            File random = new File(Directories.getCachePath() + File.separator + "randoms.jar");
            if (random.exists()) {
                Core.verbose("Public random dependency already exists, no need to download it...");
                return;
            }

            String downloadLink = Configuration.GET_RANDOMS;
            if (Configuration.BOT_VERSION.isNightly()) {
                downloadLink = Configuration.GET_RANDOMS + "?stable=false";
            }

            WebUtil.downloadFile(new URL(downloadLink), random, new NoProgressListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
