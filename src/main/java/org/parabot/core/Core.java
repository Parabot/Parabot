package org.parabot.core;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.parabot.Landing;
import org.parabot.api.translations.TranslationHelper;
import org.parabot.core.ui.utils.UILog;
import org.parabot.environment.api.utils.Version;
import org.parabot.environment.api.utils.WebUtil;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JOptionPane;

/**
 * The core of parabot
 *
 * @author Everel, JKetelaar
 */
@SuppressWarnings("Duplicates")
public class Core {

    private static final Version currentVersion = Configuration.BOT_VERSION;
    private static int quickLaunchByUuid = -1; // used like -server, but denoted by an Int rather than the server name
    private static boolean debug; // Debug mode is Offline Mode. No BDN connection for Servers/Scripts/User Login. Not related to debug messages.
    private static boolean verbose;
    private static boolean dump;
    private static boolean loadLocal; //Loads both local and public scripts/servers
    private static boolean validate = true;
    private static boolean secure = true;

    public static void disableValidation() {
        Core.validate = false;
    }

    public static boolean hasValidation() {
        return validate;
    }

    public static int getQuickLaunchByUuid() {
        return quickLaunchByUuid;
    }

    public static void setQuickLaunchByUuid(int quickLaunchByUuid) {
        Core.quickLaunchByUuid = quickLaunchByUuid;
    }

    /**
     * Enabled loadLocal mode
     *
     * @param loadLocal
     */
    public static void setLoadLocal(final boolean loadLocal) {
        Core.loadLocal = loadLocal;
    }

    /**
     * @return if the client is in loadLocal mode.
     */
    public static boolean inLoadLocal() {
        return loadLocal;
    }

    /**
     * Set debug mode AKA Offline Mode. If true, BDN login will be skipped, so BDN Servers or Scripts will be unavailable.
     *
     * @param debug
     */
    public static void setDebug(final boolean debug) {
        Core.debug = debug;
    }

    /**
     * Enables dump mode
     *
     * @param dump
     */
    public static void setDump(final boolean dump) {
        Core.dump = dump;
    }

    public static void disableSec() {
        UILog.log(
                "Security Warning",
                "Disabling the securty manager is ill advised.\n"
                        + " Only do so if the client fails to load, or functions incorrectly (freezes,crashes, etc.)\n"
                        + "The security manager protects you from malicous code within the client, without it you are exposed!\n"
                        + "\nPlease contact Parabot staff to resolve whatever problem you are having!");
        Core.secure = false;
    }

    public static boolean isSecure() {
        return secure;
    }

    /**
     * @return if the client is in debug mode AKA Offline Mode. BDN Servers and Scripts are unavailable.
     */
    public static boolean inDebugMode() {
        return debug;
    }

    /**
     * @return if the client is in verbose mode.
     */
    public static boolean inVerboseMode() {
        return verbose;
    }

    /**
     * @return if parabot should dump injected jar
     */
    public static boolean shouldDump() {
        return dump;
    }

    /**
     * Sets verbose mode
     *
     * @param verbose - enabled
     */
    public static void setVerbose(final boolean verbose) {
        Core.verbose = verbose;
    }

    public static void verbose(final String line) {
        if (verbose) {
            System.out.println(line);
        }
    }

    /**
     * Prints a debug line to the Logger and System PrintStream
     * Meant for the debug adapter within hooks
     *
     * @param line
     */
    public static void debug(final String line) {
        System.out.println(line);
    }

    /**
     * Compares the latest version from the BDN and the current version
     *
     * @return True if the current version is equal or higher than the latest version, false if lower than the latest version
     */
    public static boolean validVersion() {
        String url = String.format(Configuration.COMPARE_VERSION_URL, "client", currentVersion.get());

        BufferedReader br = WebUtil.getReader(url);
        try {
            if (br != null) {
                JSONObject object = (JSONObject) WebUtil.getJsonParser().parse(br);
                boolean latest = (Boolean) object.get("result");
                if (!latest) {
                    Directories.clearCache();
                }
                Core.verbose("Local version: " + currentVersion.get() + ". " + (latest ? "This is up to date." : "This is Out Of Date. Cache will be cleared."));
                return latest;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public static void downloadNewVersion() {
        UILog.log(TranslationHelper.translate("UPDATES"),
                TranslationHelper.translate("DOWNLOAD_UPDATE_PARABOT_AT")
                        + Configuration.DOWNLOAD_BOT + (currentVersion.isNightly() ? Configuration.NIGHTLY_APPEND : ""),
                JOptionPane.INFORMATION_MESSAGE);
        URI uri = URI.create(Configuration.API_DOWNLOAD_BOT + (currentVersion.isNightly() ? Configuration.NIGHTLY_APPEND : ""));
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(null, TranslationHelper.translate("CONNECTION_ERROR"),
                    TranslationHelper.translate("ERROR"), JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
        }
    }

    /**
     * Checks for updates.
     *
     * @return <b>true</b> if no update is required, otherwise <b>false</b>.
     */
    public static boolean isValid() {
        Core.verbose("Checking for updates...");
        validateCache();

        if (validate) {
            if (validVersion() && checksumValid()) {
                Core.verbose("No updates available.");
                return true;
            } else {
                Core.verbose("Updates available...");
                return false;
            }
        } else {
            Core.verbose("Validation disabled");
            return true;
        }
    }

    /**
     * Alerts the user that there is a new version
     */
    public static int newVersionAlert() {
        return UILog.alert("Parabot Update", "There's a new version of Parabot! \nDo you wish to download it?\n\nThe current version could have some problems!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Checks the version of the bot using a checksum of the jar comparison against checksum given by the website
     *
     * @return <b>true</b> if no new version is found, otherwise <b>false</b>.
     */
    private static boolean checksumValid() {
        File f = new File(Landing.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        if (f.isFile()) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                File location = new File(Landing.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
                if (location.exists()) {
                    FileInputStream fis = new FileInputStream(location);
                    byte[] dataBytes = new byte[1024];

                    int nread;

                    while ((nread = fis.read(dataBytes)) != -1) {
                        md.update(dataBytes, 0, nread);
                    }

                    byte[] mdbytes = md.digest();

                    StringBuilder sb = new StringBuilder();
                    for (byte mdbyte : mdbytes) {
                        sb.append(Integer.toString((mdbyte & 0xff) + 0x100, 16).substring(1));
                    }

                    String result;
                    if ((result = WebUtil.getContents(String.format(Configuration.COMPARE_CHECKSUM_URL, "client", currentVersion.get()), "checksum=" + URLEncoder.encode(sb.toString(), "UTF-8"))) != null) {
                        JSONObject object = (JSONObject) WebUtil.getJsonParser().parse(result);
                        boolean upToDate = (boolean) object.get("result");
                        Core.verbose("Local checksum: " + URLEncoder.encode(sb.toString(), "UTF-8") + ". " + (upToDate ? "This matches BDN and is up to date." : "BDN mismatch, must be Out Of Date."));
                        return upToDate;
                    }
                }
            } catch (NoSuchAlgorithmException | ParseException | IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Method that removes the cache contents after 3 days
     */
    private static void validateCache() {
        // Already handled by Directories initiating
        // Method will be used once BDN V3 has a functionality for this
    }
}