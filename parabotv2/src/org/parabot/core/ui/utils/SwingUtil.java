package org.parabot.core.ui.utils;

import javax.swing.JFrame;

/**
 * 
 * Holds various swing util based methods
 * 
 * @author Dane
 *
 */
public class SwingUtil {

	/**
	 * Packs, centers, and shows the frame.
	 * 
	 * @param f
	 */
	public static void finalize(JFrame f) {
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

}
