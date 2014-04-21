
package org.parabot.core.ui.utils;

import javax.swing.JFrame;

/**
 * Holds various swing util based methods
 * 
 * @author Dane
 */
public class SwingUtil
{

	/**
	 * Centers the frame.
	 * 
	 * @param f
	 */
	public static void center( JFrame f )
	{
		f.setLocation( ( AwtUtil.getScreenWidth() - f.getWidth() ) / 2, ( AwtUtil.getScreenHeight() - f.getHeight() ) / 2 );
	}


	/**
	 * Packs, centers, and shows the frame.
	 * 
	 * @param f
	 */
	public static void finalize( JFrame f )
	{
		f.pack();
		center( f );
		f.setVisible( true );
	}

}
