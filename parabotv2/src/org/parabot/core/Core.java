
package org.parabot.core;

import org.parabot.environment.api.utils.WebUtil;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The core of parabot
 * 
 * @author Everel
 */
public class Core
{

	private static boolean debug;
	private static boolean verbose;
	private static boolean dump;
	private static boolean loadLocal; // Loads both local and public scripts/servers


	/**
	 * Enabled loadLocal mode
	 * 
	 * @param loadLocal
	 */
	public static void setLoadLocal( final boolean loadLocal )
	{
		Core.loadLocal = loadLocal;
	}


	/**
	 * @return if the client is in loadLocal mode.
	 */
	public static boolean inLoadLocal()
	{
		return loadLocal;
	}


	/**
	 * Enabled debug mode
	 * 
	 * @param debug
	 */
	public static void setDebug( final boolean debug )
	{
		Core.debug = debug;
	}


	/**
	 * Enables dump mode
	 * 
	 * @param dump
	 */
	public static void setDump( final boolean dump )
	{
		Core.dump = dump;
	}


	/**
	 * @return if the client is in debug mode.
	 */
	public static boolean inDebugMode()
	{
		return debug;
	}


	/**
	 * @return if the client is in verbose mode.
	 */
	public static boolean inVerboseMode()
	{
		return verbose;
	}


	/**
	 * @return if parabot should dump injected jar
	 */
	public static boolean shouldDump()
	{
		return dump;
	}


	/**
	 * Sets verbose mode
	 * 
	 * @param verbose
	 *            - enabled
	 */
	public static void setVerbose( final boolean verbose )
	{
		Core.verbose = verbose;
	}


	public static void verbose( final String line )
	{
		if( verbose ) {
			System.out.println( line );
		}
	}


	/**
	 * Checks for updates.
	 * 
	 * @return <b>true</b> if no update is required, otherwise <b>false</b>.
	 */
	public static boolean isValid()
	{
		Core.verbose( "Checking for updates..." );
		BufferedReader br = WebUtil.getReader( Configuration.GET_BOT_VERSION );
		try {
			double version = Double.parseDouble( br.readLine() );
			if( Configuration.BOT_VERSION >= version ) {
				Core.verbose( "No updates available." );
				return true;
			}
		} catch( NumberFormatException e ) {
			e.printStackTrace();
		} catch( IOException e ) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch( IOException e ) {
				e.printStackTrace();
			}
		}
		Core.verbose( "Updates available..." );
		return false;
	}


	public static void debug( int i )
	{
		System.out.println( "DEBUG: " + i );
	}
}
