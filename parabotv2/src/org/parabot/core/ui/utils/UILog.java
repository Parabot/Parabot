package org.parabot.core.ui.utils;

import javax.swing.JOptionPane;

import org.parabot.core.ui.BotUI;

/**
 * 
 * Log messages to the log user interface which is attached to the bot user interface
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
		JOptionPane.showMessageDialog(BotUI.getInstance(), message, title,
				messageType);
	}

}
