package org.parabot.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.parabot.Landing;
import org.parabot.api.io.Directories;
import org.parabot.api.translations.TranslationHelper;
import org.parabot.core.bdn.api.APIConfiguration;
import org.parabot.core.di.injector.AppInjector;
import org.parabot.core.settings.Configuration;
import org.parabot.core.ui.utils.UILog;
import org.parabot.environment.api.utils.WebUtil;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The core of parabot
 *
 * @author Everel, JKetelaar
 */
@SuppressWarnings("Duplicates")
public class Core {

    private static final Injector injector = Guice.createInjector(new AppInjector());
    private static LaunchMode mode;

    private static boolean verbose;
    private static boolean dump;
    private static boolean validate = true;
    private static boolean secure = true;


    public static Injector getInjector() {
        return injector;
    }

    public static void disableValidation() {
        Core.validate = false;
    }

    public static boolean hasValidation() {
        return validate;
    }

    public static LaunchMode getMode() {
        return mode;
    }

    public static void setMode(LaunchMode mode) {
        Core.mode = mode;
    }

    public static boolean isMode(LaunchMode mode) {
        return Core.mode != null && Core.mode.equals(mode);
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

                    StringBuilder sb = new StringBuilder("");
                    for (byte mdbyte : mdbytes) {
                        sb.append(Integer.toString((mdbyte & 0xff) + 0x100, 16).substring(1));
                    }

                    String result;
                    if ((result = WebUtil.getContents(String.format(Configuration.COMPARE_CHECKSUM_URL, "client", Configuration.BOT_VERSION.get()), "checksum=" + URLEncoder.encode(sb.toString(), "UTF-8"))) != null) {
                        JSONObject object = (JSONObject) WebUtil.getJsonParser().parse(result);
                        return (boolean) object.get("result");
                    }
                }
            } catch (NoSuchAlgorithmException | ParseException | IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Compares the latest version from the BDN and the current version
     *
     * @return True if the current version is equal or higher than the latest version, false if lower than the latest version
     */
    public static boolean validVersion() {
        String url = String.format(Configuration.COMPARE_VERSION_URL, "client", Configuration.BOT_VERSION.get());

        BufferedReader br = WebUtil.getReader(url);
        try {
            if (br != null) {
                JSONObject object = (JSONObject) WebUtil.getJsonParser().parse(br);
                boolean latest = (Boolean) object.get("result");
                if (!latest) {
                    Directories.clearCache();
                }
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

    /**
     * Method that removes the cache contents after 3 days
     */
    private static void validateCache() {
        // Already handled by Directories initiating
        // Method will be used once BDN V3 has a functionality for this
    }

    public static void downloadNewVersion() {
        String url = String.format(APIConfiguration.DOWNLOAD_BOT, Configuration.BOT_VERSION.isNightly());
        UILog.log(TranslationHelper.translate("UPDATES"),
                TranslationHelper.translate("DOWNLOAD_UPDATE_PARABOT_AT") + url,
                JOptionPane.INFORMATION_MESSAGE);
        URI uri = URI.create(url);
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

    public enum LaunchMode {
        INCLUDE_LOCAL("Include local", "-withlocal"),
        LOCAL_ONLY("Local only", "-local");

        private String name;
        private String arg;

        LaunchMode(String name, String arg) {
            this.name = name;
            this.arg = arg;
        }

        public String getName() {
            return name;
        }

        public String getArg() {
            return arg;
        }
    }
}