package org.parabot;

import com.bugsnag.Client;
import org.parabot.core.Configuration;
import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.forum.AccountManager;
import org.parabot.core.network.NetworkInterface;
import org.parabot.core.network.proxy.ProxySocket;
import org.parabot.core.network.proxy.ProxyType;
import org.parabot.core.ui.BotTray;
import org.parabot.core.ui.BotUI;
import org.parabot.core.ui.ServerSelector;
import org.parabot.core.ui.utils.UILog;
import org.parabot.environment.api.utils.JavaUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Parabot v2.1
 *
 * @author Everel/Parnassian/Clisprail, Paradox/JKetelaar, Matt, Dane
 * @version 2.1
 * @see <a href="http://www.parabot.org">Homepage</a>
 */
public final class Landing {
	private static String username;
	private static String password;
	public static void main(String... args) throws IOException {
		Core.initiateBugsnagInstance();

		parseArgs(args);



		Core.verbose("Debug mode: " + Core.inDebugMode());

		try {
			Core.verbose("Setting look and feel: "
					+ UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable t) {
			t.printStackTrace();
		}

		if (!Core.inDebugMode() && !Core.isValid() && Core.hasValidation()) {
			UILog.log("Updates",
					"Please download the newest version of Parabot at "
							+ Configuration.DOWNLOAD_BOT,
					JOptionPane.INFORMATION_MESSAGE);
			URI uri = URI.create(Configuration.API_DOWNLOAD_BOT);
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Connection Error",
						"Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
			return;
		}

		Core.verbose("Validating directories...");
		Directories.validate();
		Core.verbose("Validating account manager...");
		AccountManager.validate();

		if (username != null && password != null) {
			new BotUI(username, password);
			username = null;
			password = null;
			return;
		}

		Core.verbose("Starting login gui...");
		new BotUI(null, null);
		new BotTray();
	}

	private static void parseArgs(String... args) {
		for (int i = 0; i < args.length; i++) {
			final String arg = args[i].toLowerCase();
			switch (arg.toLowerCase()) {
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
				case "-loadlocal":
					Core.setLoadLocal(true);
					break;
				case "-dump":
					Core.setDump(true);
					break;
				case "-scriptsbin":
					Directories.setScriptCompiledDirectory(new File(args[++i]));
					break;
				case "-serversbin":
					Directories.setServerCompiledDirectory(new File(args[++i]));
					break;
				case "-clearcache":
					Directories.clearCache();
					break;
				case "-mac":
					byte[] mac = new byte[6];
					String str = args[++i];
					if (str.toLowerCase().equals("random")) {
						new java.util.Random().nextBytes(mac);
					} else {
						i--;
						for (int j = 0; j < 6; j++) {
							mac[j] = Byte.parseByte(args[++i], 16); // parses a hex
							// number
						}
					}
					NetworkInterface.setMac(mac);
					break;
				case "-proxy":
					ProxyType type = ProxyType.valueOf(args[++i].toUpperCase());
					if (type == null) {
						System.err.println("Invalid proxy type:" + args[i]);
						System.exit(1);
						return;
					}
					ProxySocket.setProxy(type, args[++i],
							Integer.parseInt(args[++i]));
					break;
				case "-auth":
					ProxySocket.auth = true;
					ProxySocket.setLogin(args[++i], args[++i]);
					break;
				case "-no_sec":
					Core.disableSec();
					break;
				case "-no_validation":
					Core.disableValidation();
					break;
			}
		}
	}
}
