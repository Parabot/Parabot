
package org.parabot.core.ui.utils;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Holds various awt util based methods
 * 
 * @author Dane
 */
public class AwtUtil
{

	private static Toolkit toolkit;


	public static Toolkit getToolkit()
	{
		if( toolkit == null ) {
			toolkit = Toolkit.getDefaultToolkit();
		}
		return toolkit;
	}


	public static Dimension getScreenSize()
	{
		return AwtUtil.getToolkit().getScreenSize();
	}


	public static int getScreenWidth()
	{
		return AwtUtil.getScreenSize().width;
	}


	public static int getScreenHeight()
	{
		return AwtUtil.getScreenSize().height;
	}

}
