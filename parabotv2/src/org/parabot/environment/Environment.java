package org.parabot.environment;

import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.parsers.servers.ServerParser;

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
		Core.verbose("Loading server: " + desc.toString());

		final String id = "tab" + Context.getID();
		final ThreadGroup bot = new ThreadGroup(id);

		ServerParser.SERVER_CACHE.get(desc).run(bot);
	}
}
