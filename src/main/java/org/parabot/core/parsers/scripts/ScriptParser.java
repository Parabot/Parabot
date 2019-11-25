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

    public static final Map<ScriptDescription, ScriptExecuter> SCRIPT_CACHE = new HashMap<>();

    private static final ArrayList<ScriptParser> parsers = new ArrayList<>();

    public static ScriptDescription[] getDescriptions() {
        SCRIPT_CACHE.clear();
        parsers.clear();
        if (Core.inLoadLocal()) {
            parsers.add(new LocalJavaScripts());
            parsers.add(new BDNScripts());
        } else if (Core.inDebugMode()) {
            parsers.add(new LocalJavaScripts());
        } else {
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

        Map<ScriptDescription, ScriptExecuter> SORTED_SCRIPT_CACHE = new TreeMap<>(SCRIPT_CACHE);

        return SORTED_SCRIPT_CACHE.keySet().toArray(new ScriptDescription[SORTED_SCRIPT_CACHE.size()]);
    }

    public static final void addParser(ScriptParser parser) {
        parsers.add(parser);
    }

    public abstract void execute();
}