
package org.parabot;

import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.forum.AccountManager;
import org.parabot.core.ui.LoginUI;
import org.parabot.core.ui.ServerSelector;
import org.parabot.core.ui.utils.UILog;

import javax.swing.*;

import java.io.File;
import java.io.IOException;

/**
 * Parabot v2
 * 
 * @author Everel/Parnassian/Clisprail
 * @author Matt, Dane
 * @version 2.04
 */
public final class Landing
{

	// forum account
	private static String username;
	private static String password;


	public static void main( String... args ) throws IOException
	{
		parseArgs( args );

		Core.verbose( "Debug mode: " + Core.inDebugMode() );

		try {
			Core.verbose( "Setting look and feel: "
					+ UIManager.getSystemLookAndFeelClassName() );
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		} catch( Throwable t ) {
			t.printStackTrace();
		}

		if( ! Core.inDebugMode() && ! Core.isValid() ) {
			UILog.log(
					"Updates",
					"Please download the newest version of parabot at http://www.parabot.org/",
					JOptionPane.INFORMATION_MESSAGE );
			return;
		}

		Core.verbose( "Validating directories..." );
		Directories.validate();
		Core.verbose( "Validating account manager..." );
		AccountManager.validate();

		if( username != null && password != null ) {
			new LoginUI( username, password );
			username = null;
			password = null;
			return;
		}

		Core.verbose( "Starting login gui..." );
		new LoginUI().setVisible( true );
	}


	private static void parseArgs( String... args )
	{
		for( int i = 0; i < args.length; i ++ ) {
			final String arg = args[i].toLowerCase();
			switch( arg.toLowerCase() ) {
				case "-createdirs":
					Directories.validate();
					System.out
							.println( "Directories created, you can now run parabot." );
					System.exit( 0 );
					break;
				case "-debug":
					Core.setDebug( true );
					break;
				case "-v":
				case "-verbose":
					Core.setVerbose( true );
					break;
				case "-server":
					ServerSelector.initServer = args[ ++ i];
					break;
				case "-login":
					username = args[ ++ i];
					password = args[ ++ i];
					break;
				case "-loadlocal":
					Core.setLoadLocal( true );
					break;
				case "-dump":
					Core.setDump( true );
					break;
				case "-scriptsbin":
					Directories.setScriptCompiledDirectory( new File( args[ ++ i] ) );
					break;
				case "-serversbin":
					Directories.setServerCompiledDirectory( new File( args[ ++ i] ) );
					break;
			}

		}
	}

}
