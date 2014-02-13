package org.parabot.core.parsers.scripts;

import org.parabot.core.Core;
import org.parabot.core.desc.ScriptDescription;
import org.parabot.core.jython.Jython;
import org.parabot.environment.scripts.ScriptExecuter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Abstract class for parsing scripts
 * 
 * @author Everel
 *
 */
public abstract class ScriptParser {
	
	public static final Map<ScriptDescription, ScriptExecuter> SCRIPT_CACHE = new HashMap<ScriptDescription, ScriptExecuter>();
	
	public abstract void execute();
	
	public static ScriptDescription[] getDescriptions() {
		SCRIPT_CACHE.clear();
		final ArrayList<ScriptParser> parsers = new ArrayList<ScriptParser>();
		if(Core.inLoadLocal()) {
			parsers.add(new LocalJavaScripts());
			if(Jython.isValid()) {
				parsers.add(new LocalPythonScripts());
			}
            parsers.add(new SDNScripts());
		}else if (Core.inDebugMode()){
            parsers.add(new LocalJavaScripts());
            if(Jython.isValid()) {
                parsers.add(new LocalPythonScripts());
            }
        }else{
			parsers.add(new SDNScripts());
		}
		
		Core.verbose("Parsing scripts...");
		for(final ScriptParser parser : parsers) {
			parser.execute();
		}
		
		if(Core.inVerboseMode()) {
			for(final ScriptDescription desc : SCRIPT_CACHE.keySet()) {
				Core.verbose(desc.toString());
			}
			Core.verbose("Scripts parsed.");
		}
		
		return SCRIPT_CACHE.keySet().toArray(new ScriptDescription[SCRIPT_CACHE.size()]);
	}

}
