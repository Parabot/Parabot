package org.parabot;

import org.matt123337.proxy.ProxySocket;
import org.matt123337.proxy.ProxyType;
import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.ui.ServerSelector;
import org.parabot.core.ui.utils.UILog;

import javax.swing.*;

import java.io.IOException;

/**
 * Parabot v2
 * 
 * @author Everel/Parnassian/Clisprail
 * @author Matt, Dane
 * @version 2.04
 */
public final class Landing {

	public static void main(String... args) throws IOException {
		parseArgs(args);

		try {
			Core.verbose("Setting look and feel: "
					+ UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable t) {
			t.printStackTrace();
		}

		if (!Core.isValid()) {
			System.out
					.println("New version of mainstream parabot out, better merge!");
		}

		Core.verbose("Validating directories...");
		Directories.validate();

		ServerSelector.getInstance();
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
			case "-v":
			case "-verbose":
				Core.setVerbose(true);
				break;
			case "-server":
				ServerSelector.initServer = args[++i];
				break;
			case "-proxy":
				String type = args[++i];
				String ip = args[++i];
				String port = args[++i];
				if (!handleProxy(type, ip, port)) {
					System.exit(1);
				}
				break;
			}

		}
	}

	private static boolean handleProxy(String type, String ip, String port) {
		ProxyType proxyType = null;
		for (ProxyType pt : ProxyType.values()) {
			if (pt.name().equalsIgnoreCase(type)) {
				proxyType = pt;
				break;
			}
		}
		if (proxyType == null) {
			System.err.println("Unknown proxy type:" + type);
			return false;
		}
		try {
			int p = Integer.parseInt(port);
			ProxySocket.setProxy(proxyType, ip, p);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
