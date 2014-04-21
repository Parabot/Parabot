
package org.parabot.core.parsers.scripts;

import org.parabot.core.Configuration;
import org.parabot.core.desc.ScriptDescription;
import org.parabot.core.forum.AccountManager;
import org.parabot.core.forum.AccountManagerAccess;
import org.parabot.environment.api.utils.WebUtil;
import org.parabot.environment.scripts.SDNScriptExecuter;

import java.io.BufferedReader;
import java.net.URL;

/**
 * Parses scripts stored at parabot�s sdn
 * 
 * @author Everel
 */
public class SDNScripts extends ScriptParser
{

	private static AccountManager manager;

	public static final AccountManagerAccess MANAGER_FETCHER = new AccountManagerAccess()
	{

		@Override
		public final void setManager( AccountManager manager )
		{
			SDNScripts.manager = manager;
		}

	};


	@Override
	public void execute()
	{
		if( ! manager.isLoggedIn() ) {
			System.err.println( "Not logged in..." );
			return;
		}
		try {
			BufferedReader br = WebUtil.getReader( new URL( String.format( Configuration.SDN_SCRIPTS, manager.getAccount()
					.getUsername() ) ) );
			int count = 0;
			String line;

			String jarName = null;
			int sdnId = - 1;
			String scriptName = null;
			String author = null;
			double version = 0D;
			String category = null;
			String description = null;
			String[] servers = null;
			while( ( line = br.readLine() ) != null ) {
				count ++ ;

				switch( count % 8 ) {
					case 1:
						// jarname
						jarName = line;
						break;
					case 2:
						// sdn id
						sdnId = Integer.parseInt( line );
						break;
					case 3:
						scriptName = line;
						break;
					case 4:
						author = line;
						break;
					case 5:
						version = Double.parseDouble( line );
						break;
					case 6:
						category = line;
						break;
					case 7:
						description = line;
						break;
					case 0:
						if( line.contains( ", " ) ) {
							servers = line.split( ", " );
						} else {
							servers = new String[] { line };
						}
						final ScriptDescription desc = new ScriptDescription( jarName, scriptName,
								author, category, version, description,
								servers, sdnId );
						SCRIPT_CACHE.put( desc, new SDNScriptExecuter( sdnId ) );
				}
			}

			br.close();

		} catch( Throwable t ) {
			t.printStackTrace();
		}
	}

}
