package org.parabot.core.ui.utils;

import javax.swing.JOptionPane;

/**
 * 
 * @author Everel
 *
 */
public class UILog {

	public static void log(final String title, final String message) {
		log(title, message, JOptionPane.INFORMATION_MESSAGE);
	}

	public static void log(final String title, final String message,
			int messageType) {
		JOptionPane.showMessageDialog(null, message, title,
				messageType);
	}

}
