package org.parabot.environment.servers;

import org.parabot.core.Configuration;
import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.build.BuildPath;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.ui.components.VerboseLoader;
import org.parabot.core.ui.utils.UILog;
import org.parabot.environment.api.utils.WebUtil;
import org.parabot.environment.servers.loader.ServerLoader;

import javax.swing.*;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;

/**
 * 
 * Fetches a server provider from the parabot sdn
 * 
 * @author Everel
 * 
 */
public class PublicServerExecuter extends ServerExecuter {
	private String serverName = null;
	private String serverID = null;

	public PublicServerExecuter(final String serverName, final String serverID) {
		this.serverName = serverName;
		this.serverID = serverID;
	}

	@Override
	public void run(ThreadGroup tg) {
		try {
			final File destination = new File(Directories.getCachePath(),
					this.serverID);
			final String jarUrl = Configuration.GET_SERVER_PROVIDER + this.serverID;
            final String providerInfo = Configuration.GET_SERVER_PROVIDER_INFO + this.serverID;

            try{
                Integer.parseInt(this.serverID);
            }catch(NumberFormatException e){
                UILog.log(
                        "Error",
                        "Failed to parse the server ID for the server provider, error: [Server ID is not an integer.]",
                        JOptionPane.ERROR_MESSAGE);
            }

			Core.verbose("Downloading: " + jarUrl + " ...");

			WebUtil.downloadFile(new URL(jarUrl), destination,
					VerboseLoader.get());

            WebUtil.downloadFile(new URL(providerInfo),
                    new File(System.getProperty("user.home") + "/serverProvider.pb"),
                    VerboseLoader.get());
			
			Core.verbose("Server provider downloaded...");
			
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
				super.finalize(tg, serverProvider, this.serverName);
			} catch (NoClassDefFoundError ignored) {
				UILog.log(
						"Error",
						"Failed to load server provider, error: [This server provider is not compitable with this version of parabot]",
						JOptionPane.ERROR_MESSAGE);
			} catch (ClassNotFoundException ignored) {
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
