package org.parabot.environment.servers.executers;

import com.google.inject.Inject;
import org.parabot.api.io.Directories;
import org.parabot.api.io.WebUtil;
import org.parabot.api.io.build.BuildPath;
import org.parabot.core.Core;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.ui.components.DialogHelper;
import org.parabot.core.user.SharedUserAuthenticator;
import org.parabot.environment.servers.ServerProvider;
import org.parabot.environment.servers.loader.ServerLoader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URI;

/**
 * Fetches a server provider from the Parabot BDN
 *
 * @author Everel, JKetelaar
 */
public class LocalServerExecutor extends ServerExecutor {

    private ServerDescription description;

    @Inject
    private SharedUserAuthenticator userAuthenticator;

    public LocalServerExecutor setServerDescription(final ServerDescription description) {
        this.description = description;

        return this;
    }

    @Override
    public void run() {
        try {
            File destination = new File(Directories.getCachePath(),
                    description.getServerName() + "-local-provider" + ".jar");

            Core.verbose("Downloading provider...");

            URI u = new URI(description.getDetail("provider"));
            boolean isWeb = "http".equalsIgnoreCase(u.getScheme())
                    || "https".equalsIgnoreCase(u.getScheme());

            if (isWeb) {
                WebUtil.downloadFile(u.toURL(), destination);
            } else {
                destination = new File(u.toString());
            }

            Core.verbose("Server provider downloaded...");

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
