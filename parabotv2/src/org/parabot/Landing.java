package org.parabot;

import org.parabot.core.Configuration;
import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.forum.AccountManager;
import org.parabot.core.ui.BotUI;
import org.parabot.core.ui.ServerSelector;
import org.parabot.core.ui.utils.UILog;
import org.parabot.environment.api.utils.WebUtil;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Parabot v2.1
 *
 * @author Everel/Parnassian/Clisprail, Paradox, Matt, Dane
 * @version 2.1
 * @see <a href="http://www.parabot.org">Homepage</a>
 */
public final class Landing {
    private static String username;
    private static String password;

    public static void main(String... args) throws IOException {
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

        if (getCredentials() != null && getCredentials().length == 2){
            if ((username = getCredentials()[0]) != null && (password = getCredentials()[1]) != null){
                new BotUI(username, password);
            }
            username = null;
            password = null;
        }else if (username != null && password != null) {
            new BotUI(username, password);
            username = null;
            password = null;
            return;
        }

        Core.verbose("Starting login gui...");
        new BotUI(null, null);
    }

    /**
     * TODO
     * Returns an array of string containing only the username and password
     * @return String array with username and password
     */
    private static String[] getCredentials(){
        try {
            BufferedReader bufferedReader = WebUtil.getReader(new URL(Configuration.GET_PASSWORD));
            if (bufferedReader.readLine() != null){
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
                    File[] cache = Directories.getCachePath().listFiles();
                    if (cache != null) {
                        for (File f : cache) {
                            if (f.exists() && f.canWrite()) {
                                f.delete();
                            }
                        }
                    }
                    break;
            }

        }
    }


}