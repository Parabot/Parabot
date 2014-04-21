
package org.parabot.core.ui;

import org.parabot.core.desc.ServerDescription;
import org.parabot.core.parsers.servers.ServerParser;
import org.parabot.core.ui.images.Images;
import org.parabot.core.ui.utils.SwingUtil;
import org.parabot.core.ui.widgets.ServerWidget;
import org.parabot.environment.Environment;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Shows a list of every supported server which can be started
 * 
 * @author Dane, Everel
 */

public class ServerSelector extends JFrame
{

	public static String initServer;
	private static final long serialVersionUID = 5238720307271493899L;
	private static ServerSelector instance;

	private JPanel panel;


	public static ServerSelector getInstance()
	{
		if( instance == null ) {
			instance = new ServerSelector();
		}
		return instance;
	}


	public ServerSelector()
	{

		Queue<ServerWidget> widgets = getServers();
		if( initServer != null ) {
			if( runServer( widgets ) ) {
				initServer = null;
				return;
			}
		}

		this.setTitle( "Servers" );
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setResizable( false );
		this.setIconImage( Images.getResource( "/org/parabot/core/ui/images/icon.png" ) );

		this.panel = new JPanel( new BorderLayout() );
		this.panel.setPreferredSize( new Dimension( 600, 400 ) );

		JPanel interior = new JPanel( null );

		int i = 0;
		int y = 0;
		while( widgets != null && ! widgets.isEmpty() ) {
			final ServerWidget w = widgets.poll();
			w.setSize( 300, 100 );
			if( i % 2 == 0 && i != 0 ) {
				y += 100;
			}
			w.setLocation( i % 2 == 0 ? 0: 300, y );
			interior.add( w );
			i ++ ;
		}
		y += 100;
		interior.setPreferredSize( new Dimension( 300, y ) );

		JScrollPane scrlInterior = new JScrollPane( interior );
		scrlInterior
				.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
		scrlInterior
				.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

		this.panel.add( scrlInterior, BorderLayout.CENTER );
		this.add( panel );

		SwingUtil.finalize( this );

	}


	/**
	 * This method is called when -server argument is given
	 * 
	 * @param widgets
	 */
	private boolean runServer( Queue<ServerWidget> widgets )
	{
		// TODO: test this method
		if( widgets == null || widgets.isEmpty() ) {
			return false;
		}
		final String serverName = initServer.toLowerCase();
		for( ServerWidget widget: widgets ) {
			if( widget.desc.getServerName().toLowerCase().equals( serverName ) ) {
				Environment.load( widget.desc );
				return true;
			}
		}
		return false;
	}


	/**
	 * Fetches array of server widgets
	 * 
	 * @return widgets array
	 */
	public Queue<ServerWidget> getServers()
	{
		final Queue<ServerWidget> widgets = new LinkedList<ServerWidget>();
		ServerDescription[] servers = ServerParser.getDescriptions();
		if( servers != null ) {
			for( ServerDescription desc: servers ) {
				widgets.add( new ServerWidget( desc ) );
			}
		}
		return widgets;
	}

}
