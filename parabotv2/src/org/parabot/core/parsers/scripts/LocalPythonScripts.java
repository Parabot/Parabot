
package org.parabot.core.parsers.scripts;

import org.parabot.core.Directories;
import org.parabot.core.desc.ScriptDescription;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.LocalScriptExecuter;
import org.parabot.environment.scripts.framework.PythonScript;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;

/**
 * Parses python scripts
 * 
 * @author Everel
 */
public class LocalPythonScripts extends ScriptParser
{

	private static final FilenameFilter PYTHON_SCRIPT_FILTER = new FilenameFilter()
	{

		@Override
		public boolean accept( File dir, String name )
		{
			return name.endsWith( ".py" );
		}

	};

	private PythonInterpreter interpreter;


	public LocalPythonScripts()
	{
		this.interpreter = new PythonInterpreter();
	}


	/**
	 * @param name
	 *            - local var name
	 * @return script instance
	 */
	public PythonScript getScript( String name )
	{
		PyObject clazz = interpreter.get( name );
		if( clazz.toString().startsWith( "<class '__main__." ) ) {
			final PyObject instanceClass = clazz.__call__();
			final Object javaInstance = instanceClass
					.__tojava__( PythonScript.class );
			if( javaInstance instanceof PythonScript ) {
				return ( PythonScript )javaInstance;
			}
		}
		return null;
	}


	@Override
	public void execute()
	{
		for( final File scriptFile: Directories.getScriptSourcesPath()
				.listFiles( PYTHON_SCRIPT_FILTER ) ) {
			try {
				interpreter.execfile( new FileInputStream( scriptFile ) );
				final String name = interpreter.get( "__scriptname__" )
						.asString();
				final String author = interpreter.get( "__author__" ).asString();
				final Category cat = ( Category )interpreter.get( "__category__" )
						.__tojava__( Category.class );
				final double version = interpreter.get( "__version__" )
						.asDouble();
				final String description = interpreter.get( "__description__" )
						.asString();
				final String[] servers = ( String[] )interpreter.get(
						"__servers__" ).__tojava__( String[].class );

				Object ob;

				String vip = "no";
				if( ( ob = interpreter.get( "__vip__" ) ) != null ) {
					final String isVip = ob.toString();
					vip = isVip.equals( "True" ) ? "yes": "no";
				}

				String prem = "no";
				if( ( ob = interpreter.get( "__premium__" ) ) != null ) {
					final String isPrem = ob.toString();
					prem = isPrem.equals( "True" ) ? "yes": "no";
				}

				final ScriptDescription desc = new ScriptDescription( name,
						author, cat.toString(), version, description, servers, vip, prem );
				for( final PyObject o: interpreter.getLocals().asIterable() ) {
					PythonScript script = getScript( o.asString() );
					if( script != null ) {
						SCRIPT_CACHE.put( desc, new LocalScriptExecuter( script ) );
						break;
					}
				}
				interpreter.cleanup();
			} catch( Throwable t ) {
				t.printStackTrace();
			}
		}

	}

}
