package org.parabot.core.ui.utils;


import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * 
 * @author Clisprail
 *
 */
public final class Center {
	
	/**
	 * Centers a JFrame
	 * @param frame
	 * @param width
	 * @param height
	 */
	public static final void centerFramea(final JFrame f, final int width, final int height) {
		final Dimension fscreen = Toolkit.getDefaultToolkit().getScreenSize();
		final int x = (fscreen.width - width) / 2;
		final int y = (fscreen.height - height) / 2;
		f.setBounds(x, y, width, height);
	}

}

