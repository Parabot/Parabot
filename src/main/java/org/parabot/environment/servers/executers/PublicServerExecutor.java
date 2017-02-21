package org.parabot.environment.servers.executers;

import com.google.inject.Inject;
import org.parabot.api.io.Directories;
import org.parabot.api.io.WebUtil;
import org.parabot.core.Core;
import org.parabot.core.bdn.api.APIConfiguration;
import org.parabot.core.build.BuildPath;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.settings.Configuration;
import org.parabot.core.ui.components.VerboseLoader;
import org.parabot.core.ui.utils.UILog;
import org.parabot.core.user.SharedUserAuthenticator;
import org.parabot.environment.api.utils.StringUtils;
import org.parabot.environment.servers.ServerProvider;
import org.parabot.environment.servers.loader.ServerLoader;

import javax.swing.*;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;

/**
 * Fetches a server provider from the Parabot BDN
 *
 * @author Everel
 */
public class PublicServerExecutor extends ServerExecuter {

    private ServerDescription description;

    @Inject
    private SharedUserAuthenticator userAuthenticator;

    public PublicServerExecutor(final ServerDescription description) {
        this.description = description;
    }

    @Override
    public void run() {
        try {
            final File destination = new File(Directories.getCachePath(),
                    StringUtils.toMD5(description.getDetail("provider")) + ".jar");
            final String jarUrl = String.format(APIConfiguration.DOWNLOAD_SERVER_PROVIDER, Configuration.BOT_VERSION.isNightly());

            Core.verbose("Downloading provider...");

            if (destination.exists()) {
                Core.verbose("Found cached server provider [MD5: " + StringUtils.toMD5(description.getDetail("provider")) + "]");
            } else {
                WebUtil.downloadFile(new URL(jarUrl), destination,
                        VerboseLoader.get());
                Core.verbose("Server provider downloaded...");
            }

            final ClassPath classPath = new ClassPath();
            classPath.addJar(destination);

            BuildPath.add(destination.toURI().toURL());

            ServerLoader serverLoader = new ServerLoader(classPath);
            final String[] classNames = serverLoader.getServerClassNames();
            if (classNames.length == 0) {
                UILog.log(
                        "Error",
                        "Failed to load server provider, error: [No provider found in jar file.]",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if (classNames.length > 1) {
                UILog.log(
                        "Error",
                        "Failed to load server provider, error: [Multiple providers found in jar file.]");
                return;
            }

            final String className = classNames[0];
            try {
                final Class<?> providerClass = serverLoader
                        .loadClass(className);
                final Constructor<?> con = providerClass.getConstructor();
                final ServerProvider serverProvider = (ServerProvider) con
                        .newInstance();
                serverProvider.setServerDescription(description);
                super.finalize(serverProvider);
            } catch (NoClassDefFoundError | ClassNotFoundException ignored) {
                UILog.log(
                        "Error",
                        "Failed to load server provider, error: [This server provider is not compitable with this version of parabot]",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Throwable t) {
                t.printStackTrace();
                UILog.log(
                        "Error",
                        "Failed to load server provider, post the stacktrace/error on the parabot forums.",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            UILog.log(
                    "Error",
                    "Failed to load server provider, post the stacktrace/error on the parabot forums.",
                    JOptionPane.ERROR_MESSAGE);
        }

    }
}
