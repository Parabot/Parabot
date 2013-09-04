package org.parabot;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.forum.AccountManager;
import org.parabot.core.ui.LoginUI;
import org.parabot.core.ui.ServerSelector;
import org.parabot.core.ui.utils.UILog;

/**
 * Parabot v2
 * 
 * @author Everel/Parnassian
 * @author Matt, Dane
 * @version 2.01
 */
public final class Landing {
	// forum account
	private static String username = null;
	private static String password = null;

	public static void main(String... args) {
		parseArgs(args);

		Core.verbose("Debug mode: " + Core.inDebugMode());

		try {
			Core.verbose("Setting look and feel: "
					+ UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable t) {
			t.printStackTrace();
		}

		if (!Core.inDebugMode() && !Core.isValid()) {
			UILog.log(
					"Updates",
					"Please download the newest version of parabot at http://www.parabot.org/",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		Core.verbose("Validating directories...");
		Directories.validate();
		Core.verbose("Validating account manager...");
		AccountManager.validate();

		if (username != null && password != null) {
			new LoginUI(username, password);
			username = null;
			password = null;
			return;
		}

		Core.verbose("Starting login gui...");
		new LoginUI().setVisible(true);
	}

	private static void parseArgs(String... args) {
		for (int i = 0; i < args.length; i++) {
			final String arg = args[i].toLowerCase();
			switch (arg) {
			case "-createdirs":
				Directories.validate();
				System.out
						.println("Directories created, you can now run parabot.");
				System.exit(0);
				break;
			case "-debug":
				Core.setDebug(true);
				break;
			case "-v":
			case "-verbose":
				Core.setVerbose(true);
				break;
			case "-server":
				ServerSelector.initServer = args[++i];
				break;
			case "-login":
				username = args[++i];
				password = args[++i];
				break;
			}

		}
	}

}
