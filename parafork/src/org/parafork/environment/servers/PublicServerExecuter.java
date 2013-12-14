package org.parafork.environment.servers;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.JOptionPane;

import org.parafork.core.Configuration;
import org.parafork.core.Core;
import org.parafork.core.Directories;
import org.parafork.core.build.BuildPath;
import org.parafork.core.classpath.ClassPath;
import org.parafork.core.ui.components.VerboseLoader;
import org.parafork.core.ui.utils.UILog;
import org.parafork.environment.api.utils.WebUtil;
import org.parafork.environment.servers.loader.ServerLoader;

/**
 * 
 * Fetches a server provider from the parabot sdn
 * 
 * @author Everel
 * 
 */
public class PublicServerExecuter extends ServerExecuter {
	private String serverName = null;
	private String jarName = null;

	public PublicServerExecuter(final String serverName, final String jarName) {
		this.serverName = serverName;
		this.jarName = jarName;
	}

	@Override
	public void run(ThreadGroup tg) {
		try {
			final File destination = new File(Directories.getCachePath(),
					this.jarName);
			final String jarUrl = String.format(
					Configuration.GET_SERVER_PROVIDER,
					URLEncoder.encode(this.jarName, "UTF-8"));
			
			Core.verbose("Downloading: " + jarUrl + " ...");

			WebUtil.downloadFile(new URL(jarUrl), destination,
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
