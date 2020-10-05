package org.parabot.environment.servers.executers;

import org.parabot.core.Configuration;
import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.build.BuildPath;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.desc.ServerProviderInfo;
import org.parabot.core.forum.AccountManager;
import org.parabot.core.forum.AccountManagerAccess;
import org.parabot.core.ui.components.VerboseLoader;
import org.parabot.core.ui.utils.UILog;
import org.parabot.environment.api.utils.PBLocalPreferences;
import org.parabot.environment.api.utils.WebUtil;
import org.parabot.environment.servers.ServerProvider;
import org.parabot.environment.servers.loader.ServerLoader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;

import javax.swing.JOptionPane;

/**
 * Fetches a server provider from the Parabot BDN
 *
 * @author Everel
 */
public class PublicServerExecuter extends ServerExecuter {
    private static AccountManager manager;
    public static final AccountManagerAccess MANAGER_FETCHER = new AccountManagerAccess() {

        @Override
        public final void setManager(AccountManager manager) {
            PublicServerExecuter.manager = manager;
        }

    };
    private final String cacheVersionKey = "cachedProviderVersion";
    private final String serverName;
    private PBLocalPreferences settings;

    public PublicServerExecuter(final String serverName) {
        this.serverName = serverName;
    }

    @Override
    public void run() {
        try {
            ServerProviderInfo serverProviderInfo = new ServerProviderInfo(new URL(Configuration.GET_SERVER_PROVIDER_INFO
                    + this.serverName), manager.getAccount().getURLUsername(), manager.getAccount().getURLPassword());

            final File destination = new File(Directories.getCachePath(),
                    serverProviderInfo.getCRC32() + ".jar");
            final String jarUrl = String.format(Configuration.GET_SERVER_PROVIDER, Configuration.BOT_VERSION.isNightly(), serverName);

            Core.verbose("Downloading: " + jarUrl + " ...");

            String providerVersion = serverProviderInfo.getProviderVersion();
            if (providerVersion == null) {
                providerVersion = "error";
            }

            settings = new PBLocalPreferences(serverProviderInfo.getClientCRC32() + ".json");
            if (settings.getSetting(cacheVersionKey) != null) {
                Core.verbose(String.format("Latest provider version: %s, local provider version: %s", settings.getSetting(cacheVersionKey), providerVersion));
                if (!settings.getSetting(cacheVersionKey).equals(providerVersion)) {
                    Core.verbose("Local provider outdated, clearing cache.");
                    Directories.clearCache();
                }
            } else {
                Core.verbose("No local provider version in settings, adding to settings file");
            }

            settings.addSetting(cacheVersionKey, providerVersion);

            if (destination.exists()) {
                Core.verbose("Found cached server provider [CRC32: " + serverProviderInfo.getCRC32() + "]");
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
            if (classNames == null || classNames.length == 0) {
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
