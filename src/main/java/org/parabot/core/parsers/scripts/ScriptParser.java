package org.parabot.core.parsers.scripts;

import org.parabot.core.Core;
import org.parabot.core.desc.ScriptDescription;
import org.parabot.environment.scripts.executers.ScriptExecuter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Abstract class for parsing scripts
 *
 * @author Everel
 */
public abstract class ScriptParser {

    public static final Map<ScriptDescription, ScriptExecuter> SCRIPT_CACHE = new HashMap<ScriptDescription, ScriptExecuter>();

    public static ScriptDescription[] getDescriptions() {
        SCRIPT_CACHE.clear();
        final ArrayList<ScriptParser> parsers = new ArrayList<ScriptParser>();

        if (Core.inLoadLocal()) {
            parsers.add(new LocalJavaScripts());
        }
        if (Core.loadBdn()) {
            parsers.add(new BDNScripts());
        }

        Core.verbose("Parsing scripts...");
        for (final ScriptParser parser : parsers) {
            parser.execute();
        }

        if (Core.inVerboseMode()) {
            for (final ScriptDescription desc : SCRIPT_CACHE.keySet()) {
                Core.verbose(desc.toString());
            }
            Core.verbose("Scripts parsed.");
        }

        Map<ScriptDescription, ScriptExecuter> SORTED_SCRIPT_CACHE = new TreeMap<ScriptDescription, ScriptExecuter>(SCRIPT_CACHE);

        return SORTED_SCRIPT_CACHE.keySet().toArray(new ScriptDescription[SORTED_SCRIPT_CACHE.size()]);
    }

    public abstract void execute();

}