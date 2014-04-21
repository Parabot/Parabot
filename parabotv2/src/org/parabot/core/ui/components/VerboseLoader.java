
package org.parabot.core.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.parabot.core.io.ProgressListener;
import org.parabot.core.ui.images.Images;

/**
 * An informative JPanel which tells the user what bot is doing
 * 
 * @author Everel
 */
public class VerboseLoader extends JPanel implements ProgressListener
{

	private static final long serialVersionUID = 7412412644921803896L;
	private static VerboseLoader current;
	private static String state = "Initializing loader...";

	private FontMetrics fontMetrics;
	private BufferedImage bot_image;
	private ProgressBar p;


	public VerboseLoader()
	{
		this.bot_image = Images.getResource( "/org/parabot/core/ui/images/para.png" );
		this.p = new ProgressBar( 400, 20 );
		setSize( 775, 510 );
		setPreferredSize( new Dimension( 775, 510 ) );
		setBackground( Color.black );
		setDoubleBuffered( true );
		setOpaque( false );
	}


	/**
	 * Paints on this panel
	 */
	@Override
	public void paint( Graphics g )
	{
		p.draw( g, ( getWidth() / 2 ) - 200, 220 );
		g.setFont( new Font( "Courier New", Font.PLAIN, 14 ) );
		if( fontMetrics == null ) {
			fontMetrics = g.getFontMetrics();
		}
		g.setColor( Color.white );
		int x = ( getWidth() / 2 ) - ( fontMetrics.stringWidth( state ) / 2 );
		g.drawString( state, x, 200 );
		g.setFont( new Font( "Calibri", Font.PLAIN, 12 ) );
		final String version = "v2.0";
		g.drawString( version,
				getWidth() - g.getFontMetrics().stringWidth( version ) - 10,
				getHeight() - 12 );
		x = ( getWidth() / 2 ) - ( bot_image.getWidth() / 2 );
		g.drawImage( bot_image, x, 80, null );
	}


	/**
	 * Gets instance of this panel
	 * 
	 * @return instance of this panel
	 */
	public static VerboseLoader get()
	{
		return current == null ? current = new VerboseLoader(): current;
	}


	/**
	 * Updates the status message and repaints the panel
	 * 
	 * @param message
	 */
	public static void setState( final String message )
	{
		state = message;
		current.repaint();
	}


	@Override
	public void onProgressUpdate( double value )
	{
		p.setValue( value );
		this.repaint();
	}


	@Override
	public void updateDownloadSpeed( double mbPerSecond )
	{
		p.setText( String.format( "(%.2fMB/s)", mbPerSecond ) );
	}

}
