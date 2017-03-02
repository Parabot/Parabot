package org.parabot.core.parsers.servers;

import org.parabot.core.Core;
import org.parabot.core.desc.ServerDescription;
import org.parabot.environment.servers.executers.ServerExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Abstract class for parsing server providers
 *
 * @author Everel
 */
public abstract class ServerParser {
    public static final Map<ServerDescription, ServerExecutor> SERVER_CACHE = new HashMap<>();

    public static final ServerDescription[] getDescriptions() {
        SERVER_CACHE.clear();
        final ArrayList<ServerParser> parsers = new ArrayList<>();
        if (Core.isMode(Core.LaunchMode.INCLUDE_LOCAL)) {
            // TODO: Load local servers
            parsers.add(Core.getInjector().getInstance(PublicServers.class));
        } else if (Core.isMode(Core.LaunchMode.LOCAL_ONLY)) {
            // TODO: Load local servers
        } else {
            parsers.add(Core.getInjector().getInstance(PublicServers.class));
        }

        Core.verbose("Parsing server providers...");
        for (final ServerParser parser : parsers) {
            parser.execute();
        }

        if (Core.inVerboseMode()) {
            for (final ServerDescription desc : SERVER_CACHE.keySet()) {
                Core.verbose(desc.toString());
            }
            Core.verbose("Server providers parsed.");
        }

        Map<ServerDescription, ServerExecutor> SORTED_SERVER_CACHE = new TreeMap<>(SERVER_CACHE);

        return SORTED_SERVER_CACHE.keySet().toArray(new ServerDescription[SORTED_SERVER_CACHE.size()]);
    }

    public abstract void execute();

}