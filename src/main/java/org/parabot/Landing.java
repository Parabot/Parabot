package org.parabot;

import org.parabot.api.translations.TranslationHelper;
import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.forum.AccountManager;
import org.parabot.core.network.NetworkInterface;
import org.parabot.core.network.proxy.ProxySocket;
import org.parabot.core.network.proxy.ProxyType;
import org.parabot.core.ui.BotUI;
import org.parabot.core.ui.ServerSelector;
import org.parabot.core.ui.utils.UILog;

import javax.swing.*;
import java.io.File;

/**
 * @author Everel, JKetelaar, Matt, Dane
 * @version 2.8.1
 * @see <a href="https://www.parabot.org">Homepage</a>
 */
public final class Landing {
    private static String username;
    private static String password;

    public static void main(String... args) {
//        Thread.setDefaultUncaughtExceptionHandler(new FileExceptionHandler(ExceptionHandler.ExceptionType.CLIENT));

        if (Context.getJavaVersion() >= 9) {
            UILog.log("Parabot", "Parabot doesn't support Java 9+ currently. Please downgrade to Java 8 to ensure Parabot is working correctly.");
        }

        if (!System.getProperty("os.arch").contains("64")) {
            UILog.log("Parabot", "You are not running a 64-bit version of Java, this might cause the client to lag or crash unexpectedly.\r\n" +
                    "It is recommended to upgrade to a 64-bit version.");
        }

        parseArgs(args);

        Directories.validate();

        Core.verbose(TranslationHelper.translate("DEBUG_MODE") + Core.inDebugMode());

        try {
            Core.verbose(TranslationHelper.translate("SETTING_LOOK_AND_FEEL")
                    + UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        if (!Core.inDebugMode() && Core.hasValidation() && !Core.isValid()) {
            if (Core.newVersionAlert() == JOptionPane.YES_OPTION) {
                Core.downloadNewVersion();
                return;
            }
        }

        Core.verbose(TranslationHelper.translate("VALIDATION_ACCOUNT_MANAGER"));
        AccountManager.validate();

        if (username != null && password != null) {
            new BotUI(username, password);
            username = null;
            password = null;
            return;
        }

        Core.verbose(TranslationHelper.translate("STARTING_LOGIN_GUI"));
        new BotUI(null, null);
    }

    private static void parseArgs(String... args) {
        for (int i = 0; i < args.length; i++) {
            final String arg = args[i].toLowerCase();
            switch (arg.toLowerCase()) {
                case "-createdirs":
                    Directories.validate();
                    System.out.println(TranslationHelper.translate(("DIRECTORIES_CREATED")));
                    System.exit(0);
                    break;
                case "-debug":
                    Core.setDump(true);
                case "-offlinemode":
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
                            mac[j] = (byte) Integer.parseInt(args[++i], 16); // parses a hex
                            // number
                        }
                    }
                    NetworkInterface.setMac(mac);
                    break;
                case "-proxy":
                    ProxyType type = ProxyType.valueOf(args[++i].toUpperCase());
                    ProxySocket.setProxy(type, args[++i], Integer.parseInt(args[++i]));
                    break;
                case "-proxy_auth":
                case "-auth":
                    ProxySocket.auth = true;
                    ProxySocket.setLogin(args[++i], args[++i]);
                    break;
                case "-no_sec":
                    Core.disableSec();
                    break;
                case "-no_validation":
                case "-ignore_updates":
                    Core.disableValidation();
                    break;
                case "-uuid":
                    Core.setQuickLaunchByUuid(Integer.parseInt(args[++i]));
                    break;
                default:
                    System.err.println(String.format("Unknown argument given: %s", arg.toLowerCase()));
                    break;
            }
        }
    }
}
