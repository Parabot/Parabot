package org.parabot.environment.servers.executers;

import com.google.inject.Inject;
import org.parabot.api.io.Directories;
import org.parabot.api.io.build.BuildPath;
import org.parabot.core.Core;
import org.parabot.core.bdn.api.APICaller;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.ui.newui.components.DialogHelper;
import org.parabot.core.user.SharedUserAuthenticator;
import org.parabot.environment.api.utils.StringUtils;
import org.parabot.environment.servers.ServerProvider;
import org.parabot.environment.servers.loader.ServerLoader;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;

/**
 * Fetches a server provider from the Parabot BDN
 *
 * @author Everel, JKetelaar
 */
public class PublicServerExecutor extends ServerExecutor {

    private ServerDescription description;

    @Inject
    private SharedUserAuthenticator userAuthenticator;

    public PublicServerExecutor setServerDescription(final ServerDescription description) {
        this.description = description;

        return this;
    }

    @Override
    public void run() {
        try {
            String cachedServerProviderName = StringUtils.toMD5(description.getDetail("provider"));

            final File destination = new File(Directories.getCachePath(),
                    cachedServerProviderName + ".jar");

            Core.verbose("Downloading provider...");

            if (destination.exists()) {
                Core.verbose("Found cached server provider [MD5: " + cachedServerProviderName + "]");
            } else {
                APICaller.APIPoint point       = APICaller.APIPoint.DOWNLOAD_PROVIDER.setPointParams(description.getId());
                InputStream        inputStream = (InputStream) APICaller.callPoint(point, userAuthenticator);

                APICaller.downloadFile(inputStream, destination);
                Core.verbose("Server provider downloaded...");
            }

            final ClassPath classPath = new ClassPath();
            classPath.addJar(destination);

            BuildPath.add(destination.toURI().toURL());

            ServerLoader   serverLoader = new ServerLoader(classPath);
            final String[] classNames   = serverLoader.getServerClassNames();
            if (classNames.length == 0) {
                DialogHelper.showError(
                        "Parabot",
                        "Error loading provider",
                        "Failed to load server provider, error: [No provider found in jar file.]");
                return;
            } else if (classNames.length > 1) {
                DialogHelper.showError(
                        "Parabot",
                        "Error loading provider",
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
                DialogHelper.showError(
                        "Parabot",
                        "Error loading provider",
                        "Failed to load server provider, error: [This server provider is not compitable with this version of parabot]");
            } catch (Throwable t) {
                t.printStackTrace();
                DialogHelper.showError(
                        "Parabot",
                        "Error loading provider",
                        "Failed to load server provider, post the stacktrace/error on the parabot forums.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.showError(
                    "Parabot",
                    "Error loading provider",
                    "Failed to load server provider, post the stacktrace/error on the parabot forums.");
        }

    }
}
