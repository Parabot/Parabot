package org.parabot.core;

import org.parabot.Landing;
import org.parabot.environment.api.utils.WebUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The core of parabot
 *
 * @author Everel
 */
public class Core {
	public static boolean mDebug;
    private static boolean debug;
    private static boolean verbose;
    private static boolean dump;
    private static boolean loadLocal; //Loads both local and public scripts/servers

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
        String checksum = "";

        File f = new File(Landing.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        if (f.isFile()) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                try (InputStream is = Files.newInputStream(Paths.get(f.getAbsolutePath()))) {
                    DigestInputStream dis = new DigestInputStream(is, md);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] digest = md.digest();

                for (byte aDigest : digest) {
                    checksum += Integer.toString((aDigest & 0xff) + 0x100, 16).substring(1);
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Checks the version of the bot using a variable comparison from the bot code and the Parabot website
     * @return <b>true</b> if no new version is found, otherwise <b>false</b>.
     */
    private static boolean versionValid(){
        BufferedReader br = WebUtil.getReader(Configuration.GET_BOT_VERSION);
        try {
            double version = Double.parseDouble(br.readLine());
            if (Configuration.BOT_VERSION < version) {
                return false;
            }
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    /**
     * Checks for updates.
     *
     * @return <b>true</b> if no update is required, otherwise <b>false</b>.
     */
    public static boolean isValid() {
        Core.verbose("Checking for updates...");

        if (versionValid() && checksumValid()){
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