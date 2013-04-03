package org.parabot.environment;

import java.lang.reflect.Constructor;
import java.net.URL;

import org.parabot.core.Context;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.ui.BotUI;
import org.parabot.core.ui.ServerSelector;
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
	 * @param url
	 */
	public static void load(final URL url, final String serverName) {
		ServerSelector.getInstance().dispose();
		if(!BotUI.getInstance().isVisible()) {
			BotUI.getInstance().setVisible(true);
		}
				
		final ClassPath classPath = new ClassPath();
		classPath.addJar(url.toString());

		final ServerLoader serverLoader = new ServerLoader(classPath);
		final String[] serverProviders = serverLoader.getServerClassNames();
		if (serverProviders == null) {
			throw new RuntimeException("No server provided.");
		}

		final String id = "tab" + Context.getID();
		final ThreadGroup bot = new ThreadGroup(id);
		new Thread(bot, new Runnable() {
			@Override
			public void run() {
				try {
					final Class<?> serverProviderClass = serverLoader.loadClass(serverProviders[0]);
					final Constructor<?> con = serverProviderClass.getConstructor();
					ServerProvider server = (ServerProvider) con.newInstance();
					server.context.setEnvironment(serverLoader);
					BotToolbar.getInstance().addTab(server.context, serverName);
					server.context.load();
				} catch (Throwable t) {
					throw new RuntimeException("Error while loading server. " + t.getMessage());
				}
			}
		}).start();
	}

}
