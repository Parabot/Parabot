package org.parafork.environment;

import org.parafork.core.Context;
import org.parafork.core.Core;
import org.parafork.core.desc.ServerDescription;
import org.parafork.core.parsers.servers.ServerParser;

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
