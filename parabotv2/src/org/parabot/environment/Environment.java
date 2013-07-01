package org.parabot.environment;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;

import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.build.BuildPath;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.parsers.ServerManifestParser;
import org.parabot.core.parsers.ServerManifestParser.ServerCache;
import org.parabot.core.ui.BotUI;
import org.parabot.core.ui.components.BotToolbar;
import org.parabot.environment.servers.ServerProvider;
import org.parabot.environment.servers.loader.ServerLoader;

/**
 * 
 * @author Clisprail
 * 
 */
public class Environment {

	/**
	 * Loads a new environment
	 * 
	 * @param url
	 */
	public static void load(final ServerDescription desc, final String serverName) {
		if (!BotUI.getInstance().isVisible()) {
			BotUI.getInstance().setVisible(true);
		}

		final ClassPath classPath = Core.inDebugMode() ? null : new ClassPath();
		final ServerCache cache = Core.inDebugMode() ? ServerManifestParser.cache.get(desc) : null;
		
		// buildpath
		if(cache != null) {
			if(cache.getLoader().getClassPath().isJar()) {
				cache.getLoader().getClassPath().addToBuildPath();
			} else {
				try {
					BuildPath.add(Directories.getServerPath().toURI().toURL());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		
		final ServerLoader serverLoader = Core.inDebugMode() ? cache.getLoader() : new ServerLoader(classPath);
		String[] serverProviders = null;
		if (!Core.inDebugMode()) {
			serverProviders = serverLoader.getServerClassNames();
			if (serverProviders == null) {
				throw new RuntimeException("No server provided.");
			}
		}
		final String id = "tab" + Context.getID();
		final ThreadGroup bot = new ThreadGroup(id);
		new Thread(bot, new Runnable() {
			@Override
			public void run() {
				try {
					final ServerProvider server = !Core.inDebugMode() ? fetchServerProvider(serverLoader) : cache.getProviders()[desc.providerIndex];
					final Context context = new Context(server);
					context.setEnvironment(serverLoader);
					BotToolbar.getInstance().addTab(context, serverName);
					context.load();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}).start();
	}

	private static ServerProvider fetchServerProvider(ServerLoader loader) {
		try {
			final String[] serverProviders = loader.getServerClassNames();
			if (serverProviders == null) {
				throw new RuntimeException("No server provided.");
			}
			final Class<?> serverProviderClass = loader.loadClass(serverProviders[0]);
			final Constructor<?> con = serverProviderClass.getConstructor();
			return (ServerProvider) con.newInstance();
		} catch (Throwable t) {
			throw new RuntimeException("Error while loading server. " + t.getMessage());
		}
	}

}
