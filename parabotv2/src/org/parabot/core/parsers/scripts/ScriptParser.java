
package org.parabot.core.parsers.scripts;

import org.parabot.core.Core;
import org.parabot.core.desc.ScriptDescription;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.lib.jython.Jython;
import org.parabot.environment.scripts.ScriptExecuter;
import org.parabot.environment.servers.ServerExecuter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Abstract class for parsing scripts
 * 
 * @author Everel
 */
public abstract class ScriptParser
{

	public static final Map<ScriptDescription, ScriptExecuter> SCRIPT_CACHE = new HashMap<ScriptDescription, ScriptExecuter>();


	public abstract void execute();


	public static ScriptDescription[] getDescriptions()
	{
		SCRIPT_CACHE.clear();
		final ArrayList<ScriptParser> parsers = new ArrayList<ScriptParser>();
		if( Core.inLoadLocal() ) {
			parsers.add( new LocalJavaScripts() );
			if( Jython.isValid() ) {
				parsers.add( new LocalPythonScripts() );
			}
			parsers.add( new SDNScripts() );
		} else if( Core.inDebugMode() ) {
			parsers.add( new LocalJavaScripts() );
			if( Jython.isValid() ) {
				parsers.add( new LocalPythonScripts() );
			}
		} else {
			parsers.add( new SDNScripts() );
		}

		Core.verbose( "Parsing scripts..." );
		for( final ScriptParser parser: parsers ) {
			parser.execute();
		}

		if( Core.inVerboseMode() ) {
			for( final ScriptDescription desc: SCRIPT_CACHE.keySet() ) {
				Core.verbose( desc.toString() );
			}
			Core.verbose( "Scripts parsed." );
		}

		Map<ScriptDescription, ScriptExecuter> SORTED_SCRIPT_CACHE = new TreeMap<ScriptDescription, ScriptExecuter>( SCRIPT_CACHE );

		return SORTED_SCRIPT_CACHE.keySet().toArray( new ScriptDescription[SORTED_SCRIPT_CACHE.size()] );
	}

}
