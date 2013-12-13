package org.parabot.environment;

import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.jython.Jython;
import org.parabot.core.parsers.servers.ServerParser;
import org.parabot.core.ui.components.VerboseLoader;
import org.parabot.environment.api.utils.WebUtil;

/**
 * 
 * Initiliazes the bot environment
 * 
 * @author Everel
 * 
 */
public class Environment {

	/**
	 * Loads a new environment
	 * 
	 * @param url
	 */
	public static void load(final ServerDescription desc) {
		if (!Jython.hasJar()) {
			Core.verbose("Downloading jython...");
			VerboseLoader.setState("Downloading jython...");
			WebUtil.downloadFile(Jython.getDownloadLink(), Jython.getJarFile(),
					VerboseLoader.get());
			Core.verbose("Downloaded jython.");
		}
		Core.verbose("Initializing jython...");
		Jython.init();
		
		Core.verbose("Loading server: " + desc.toString());

		final String id = "tab" + Context.getID();
		final ThreadGroup bot = new ThreadGroup(id);

		ServerParser.SERVER_CACHE.get(desc).run(bot);
		
	}
}
