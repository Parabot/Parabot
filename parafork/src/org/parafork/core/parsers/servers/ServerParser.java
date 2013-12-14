package org.parafork.core.parsers.servers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.parafork.core.Core;
import org.parafork.core.desc.ServerDescription;
import org.parafork.environment.servers.ServerExecuter;

/**
 * 
 * Abstract class for parsing server providers
 * 
 * @author Everel
 *
 */
public abstract class ServerParser {
	public static final Map<ServerDescription, ServerExecuter> SERVER_CACHE = new HashMap<ServerDescription, ServerExecuter>();
	
	public abstract void execute();
	
	public static final ServerDescription[] getDescriptions() {
		SERVER_CACHE.clear();
		final ArrayList<ServerParser> parsers = new ArrayList<ServerParser>();
			parsers.add(new LocalServers());
			parsers.add(new PublicServers());
		
		Core.verbose("Parsing server providers...");
		for(final ServerParser parser : parsers) {
			parser.execute();
		}
		
		if(Core.inVerboseMode()) {
			for(final ServerDescription desc : SERVER_CACHE.keySet()) {
				Core.verbose(desc.toString());
			}
			Core.verbose("Server providers parsed.");
		}
		
		
		return SERVER_CACHE.keySet().toArray(new ServerDescription[SERVER_CACHE.size()]);
	}

}
