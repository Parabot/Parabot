package org.parabot.core;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.parabot.Landing;
import org.parabot.core.ui.utils.UILog;
import org.parabot.environment.api.utils.Version;
import org.parabot.environment.api.utils.WebUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
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
	public static boolean mDebug;
    private static boolean debug;
    private static boolean verbose;
    private static boolean dump;
    private static boolean loadLocal; //Loads both local and public scripts/servers

    private static boolean validate = true;
    private static boolean secure = true;

    private static Version currentVersion = Configuration.BOT_VERSION;
    private static Version latestVersion;

    public static void disableValidation() {
        Core.validate = false;
    }

    public static boolean hasValidation() {
        return validate;
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
     * Enabled debug mode
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
    
    public static void disableSec(){
		UILog.log(
				"Security Warning",
				"Disabling the securty manager is ill advised.\n"
						+ " Only do so if the client fails to load, or functions incorrectly (freezes,crashes, etc.)\n"
						+ "The security manager protects you from malicous code within the client, without it you are exposed!\n"
						+ "\nPlease contact Parabot staff to resolve whatever problem you are having!");
    	Core.secure = false;
    }
    
    public static boolean isSecure(){
    	return secure;
    }

    /**
     * @return if the client is in debug mode.
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
     * Checks the version of the bot using a checksum of the jar comparison against checksum given by the website
     * @return <b>true</b> if no new version is found, otherwise <b>false</b>.
     */
	private static boolean checksumValid(){
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
                    for (int i = 0; i < mdbytes.length; i++) {
                        sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
                    }

                    String result;
                    if ((result = WebUtil.getContents("http://bdn.parabot.org/api/v2/bot/checksum", "checksum=" + URLEncoder.encode(sb.toString(), "UTF-8"))) != null) {
                        JSONObject object = (JSONObject) WebUtil.getJsonParser().parse(result);
                        if (!(boolean) object.get("result")){
                            Core.verbose("Latest checksum: " + sb.toString());
                            Core.verbose("Latest checksum: " + object.get("current"));
                            return false;
                        }
                    }
                }
            } catch (NoSuchAlgorithmException | ParseException | IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * @Deprecated use #validVersion instead
     *
     * Checks the version of the bot using a variable comparison from the bot code and the Parabot website
     *
     * @return <b>true</b> if no new version is found, otherwise <b>false</b>.
     */
    private static boolean versionValid(){
        BufferedReader br = WebUtil.getReader(Configuration.GET_BOT_VERSION);
        try {
            String version = null;
            if (br != null) {
                JSONObject object = (JSONObject) WebUtil.getJsonParser().parse(br);
                version = (String) object.get("result");
            }
            if (version != null) {
                if (!Configuration.BOT_VERSION.equals(version)) {
                    Core.verbose("Our version: " + Configuration.BOT_VERSION);
                    Core.verbose("Latest version: " + version);
                    return false;
                }
            }
        } catch (NumberFormatException | IOException | ParseException e) {
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
     * Compares the latest version from the BDN and the current version
     *
     * @return True if the current version is equal or higher than the latest version, false if lower than the latest version
     */
    public static boolean validVersion() {
        BufferedReader br = WebUtil.getReader(Configuration.GET_BOT_VERSION);
        try {
            latestVersion = null;
            if (br != null) {
                JSONObject object = (JSONObject) WebUtil.getJsonParser().parse(br);
                latestVersion = new Version((String) object.get("version"));
            }
            if (latestVersion != null) {
                if (Configuration.BOT_VERSION.compareTo(latestVersion) < 0) {
                    Core.verbose("Our version: " + Configuration.BOT_VERSION.get());
                    Core.verbose("Latest version: " + latestVersion.get());
                    return false;
                }
            }
        } catch (NumberFormatException | IOException | ParseException e) {
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
     * Validates the cache and removes the cache contents if required
     */
    private static void validateCache(){
        File[] cache = Directories.getCachePath().listFiles();
        Integer lowest = null;
        if (cache != null) {
            for (File f : cache) {
                int date = (int) (f.lastModified()/ 1000);
                if (lowest == null || date < lowest){
                    lowest = date;
                }
            }
        }

        try {
            JSONObject object = (JSONObject) WebUtil.getJsonParser().parse(WebUtil.getContents("http://bdn.parabot.org/api/v2/bot/cache", "date=" + lowest));
            if ((boolean) object.get("result")){
                Core.verbose("Making space for the latest cache files");
                Directories.clearCache();
            }else{
                Core.verbose("Cache is up to date");
            }
        } catch (MalformedURLException | ParseException e) {
            e.printStackTrace();
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

        if ((validVersion() && checksumValid()) || (!checksumValid() && currentVersion.compareTo(latestVersion) >= 0)){
            Core.verbose("No updates available.");
            return true;
        }else{
            Core.verbose("Updates available...");
            return false;
        }
    }

    public static void debug(int i) {
    	if(mDebug) {
    		System.out.println("DEBUG: " + i);
    	}
    }
}