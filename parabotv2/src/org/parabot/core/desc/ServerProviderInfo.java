
package org.parabot.core.desc;

import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.parabot.environment.api.utils.WebUtil;

/**
 * @author Everel
 */
public class ServerProviderInfo
{

	private Properties properties;


	public ServerProviderInfo( URL providerInfo, String username, String password )
	{
		this.properties = new Properties();
		try {
			String line;
			BufferedReader br = WebUtil.getReader( providerInfo, username, password );
			while( ( line = br.readLine() ) != null ) {
				if( line.contains( ": " ) ) {
					properties.put( line.substring( 0, line.indexOf( ": " ) ), line.substring( line.indexOf( ": " ) + 2, line.length() ) );
				}
			}
			br.close();
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}


	public URL getClient()
	{
		try {
			return new URL( properties.getProperty( "client" ) );
		} catch( MalformedURLException e ) {
			e.printStackTrace();
		}
		return null;
	}


	public URL getHookFile()
	{
		try {
			return new URL( properties.getProperty( "hooks" ) );
		} catch( MalformedURLException e ) {
			e.printStackTrace();
		}
		return null;
	}


	public String getClientClass()
	{
		return properties.getProperty( "clientClass" );
	}


	public String getServerName()
	{
		return properties.getProperty( "serverName" );
	}


	public long getCRC32()
	{
		return Long.parseLong( properties.getProperty( "crc32" ) );
	}


	public long getClientCRC32()
	{
		return Long.parseLong( properties.getProperty( "clientCrc32" ) );
	}


	public int getBankTabs()
	{
		return Integer.parseInt( properties.getProperty( "bankTabs" ) );
	}


	public Properties getProperties()
	{
		return this.properties;
	}

}
