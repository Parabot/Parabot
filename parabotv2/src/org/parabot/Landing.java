package org.parabot;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.ui.ServerSelector;
import org.parabot.core.ui.utils.UILog;

/**
 * Parabot v2
 * 
 * @author Clisprail (Parnassian)
 * @author Matt, Dane
 * @version 2.0
 */
public final class Landing {

	public static void main(String... args) {
		parseArgs(args);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		Directories.validate();
		if(!Core.inDebugMode()) {
			UILog.log("Error", "You can only run parabot in debug mode.", JOptionPane.ERROR_MESSAGE);
			return;
		}
		ServerSelector.getInstance();
	}
	
	private static void parseArgs(String... args) {
		for(int i = 0; i < args.length; i++) {
			final String arg = args[i];
			switch(arg) {
			case "-createdirs":
				Directories.validate();
				System.out.println("Directories created, you can now run parabot.");
				System.exit(0);
				break;
			case "-debug":
				Core.setDebug(true);
				break;
			case "-server":
				ServerSelector.initServer = args[++i];
				break;
			}
			
		}
	}

}
