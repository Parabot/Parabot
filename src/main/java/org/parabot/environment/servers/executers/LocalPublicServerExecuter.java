package org.parabot.environment.servers.executers;

import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.build.BuildPath;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.desc.ServerProviderInfo;
import org.parabot.core.ui.components.VerboseLoader;
import org.parabot.core.ui.utils.UILog;
import org.parabot.environment.api.utils.FileUtil;
import org.parabot.environment.api.utils.WebUtil;
import org.parabot.environment.servers.ServerProvider;
import org.parabot.environment.servers.loader.ServerLoader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;

import javax.swing.JOptionPane;

/**
 * Fetches a server provider from the local config file
 *
 * @author JKetelaar
 */
public class LocalPublicServerExecuter extends ServerExecuter {
    private final String serverName;
    private final String serverUrl;
    private final String providerUrl;
    private final ServerProviderInfo serverProviderInfo;

    public LocalPublicServerExecuter(final String serverName, final ServerProviderInfo serverProviderInfo, String serverUrl, String providerUrl) {
        this.serverName = serverName;
        this.serverUrl = serverUrl;
        this.providerUrl = providerUrl;
        this.serverProviderInfo = serverProviderInfo;
    }

    @Override
    public void run() {
        try {
            final File destination = new File(Directories.getCachePath(),
                    serverProviderInfo.getCRC32() + ".jar");

            Core.verbose("Downloading: " + providerUrl + " ...");

            if (destination.exists()) {
                Core.verbose("Found cached server provider [CRC32: " + serverProviderInfo.getCRC32() + "]");
            } else {
                File local;
                if ((local = new File(providerUrl)).exists()) {
                    FileUtil.copyFile(local, destination);
                    Core.verbose("Server provider copied...");
                } else {
                    WebUtil.downloadFile(new URL(providerUrl), destination, VerboseLoader.get());
                    Core.verbose("Server provider downloaded...");
                }
            }

            final File clientDestination = new File(Directories.getCachePath(),
                    serverProviderInfo.getClientCRC32() + ".jar");

            Core.verbose("Downloading: " + serverUrl + " ...");

            if (clientDestination.exists()) {
                Core.verbose("Found cached client [CRC32: " + serverProviderInfo.getClientCRC32() + "]");
            } else {
                File local;
                if ((local = new File(serverUrl)).exists()) {
                    FileUtil.copyFile(local, clientDestination);
                    Core.verbose("Server client copied...");
                } else {
                    WebUtil.downloadFile(new URL(serverUrl), clientDestination, VerboseLoader.get());
                    Core.verbose("Server client downloaded...");
                }
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
                Context.getInstance(serverProvider).setProviderInfo(serverProviderInfo);
                super.finalize(serverProvider, this.serverName);
            } catch (NoClassDefFoundError | ClassNotFoundException ignored) {
                UILog.log(
                        "Error",
                        "Failed to load server provider, error: [This server provider is not compatible with this version of parabot]",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Throwable t) {
                t.printStackTrace();
                UILog.log(
                        "Error",
                        "Failed to load server provider.",
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
