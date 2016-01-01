package org.parabot.core;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.parabot.environment.OperatingSystem;
import org.parabot.environment.api.utils.StringUtils;
import org.parabot.environment.api.utils.WebUtil;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Holds parabot's used directories
 *
 * @author Everel
 * @author Matt
 */
public class Directories {
    private static Map<String, File> cached;
    private static String tempDir;

    static {
        cached = new HashMap<>();
        switch (OperatingSystem.getOS()) {
            case WINDOWS:
                cached.put("Root", new JFileChooser().getFileSystemView().getDefaultDirectory());
                break;
            default:
                cached.put("Root", new File(System.getProperty("user.home")));
        }

        Core.verbose("Caching directories...");
        cached.put("Root", getDefaultDirectory());
        cached.put("Workspace", new File(cached.get("Root"), "/Parabot/"));
        cached.put("Sources", new File(cached.get("Root"), "/Parabot/scripts/sources/"));
        cached.put("Compiled", new File(cached.get("Root"), "/Parabot/scripts/compiled/"));
        cached.put("Resources", new File(cached.get("Root"), "/Parabot/scripts/resources/"));
        cached.put("Settings", new File(cached.get("Root"), "/Parabot/settings/"));
        cached.put("Servers", new File(cached.get("Root"), "/Parabot/servers/"));
        cached.put("Cache", new File(cached.get("Root"), "/Parabot/cache/"));
        Core.verbose("Directories cached.");

        clearCache(259200);
        if (Core.isSecure()) {
            setHomeDirectory();
        }
    }

    private static void setHomeDirectory(){
        File cache;
        tempDir = StringUtils.randomString(12);
        try {
            if ((cache = new File(Directories.getCachePath(), "cache.json")).exists()){
                JSONObject object = (JSONObject) WebUtil.getJsonParser().parse(new FileReader(cache));
                String temp;
                if ((temp = (String) object.get("homedir")) != null){
                    cached.put("Home", new File(cached.get("Root"), "/" + temp + "/"));
                }
            }else{
                cache.createNewFile();
                JSONObject object = new JSONObject();
                object.put("homedir", tempDir);
                FileWriter file = new FileWriter(cache);
                file.write(object.toJSONString());
                file.flush();
                file.close();
                cached.put("Home", new File(cached.get("Root"), "/" + tempDir + "/"));
            }
        } catch (IOException | ParseException ignored) {
            cached.put("Home", new File(cached.get("Root"), "/" + tempDir + "/"));
        }
        System.out.println("Setting server cache directory to: " + cached.get("Home"));
        cached.get("Home").mkdirs();
    }

    /**
     * Set script bin folder
     *
     * @param f
     */
    public static void setScriptCompiledDirectory(File f) {
        if (!f.isDirectory()) {
            throw new IllegalArgumentException(f + "is not a directory.");
        }
        cached.put("Compiled", f);
    }

    /**
     * Set server bin folder
     *
     * @param f
     */
    public static void setServerCompiledDirectory(File f) {
        if (!f.isDirectory()) {
            throw new IllegalArgumentException(f + "is not a directory.");
        }
        cached.put("Servers", f);
    }


    /**
     * Returns the root directory outside of the main Parabot folder.
     *
     * @return
     */
    public static File getDefaultDirectory() {
        return cached.get("Root");
    }

    /**
     * Returns the Parabot folder.
     *
     * @return
     */
    public static File getWorkspace() {
        return cached.get("Workspace");
    }

    /**
     * Returns the script sources folder.
     *
     * @return
     */
    public static File getScriptSourcesPath() {
        return cached.get("Sources");
    }

    /**
     * Returns the compiled scripts folder.
     *
     * @return
     */
    public static File getScriptCompiledPath() {
        return cached.get("Compiled");
    }

    /**
     * Returns the scripts resources folder.
     *
     * @return
     */
    public static File getResourcesPath() {
        return cached.get("Resources");
    }

    /**
     * Returns the Parabot settings folder.
     *
     * @return
     */
    public static File getSettingsPath() {
        return cached.get("Settings");
    }

    /**
     * Returns the Parabot servers folder.
     *
     * @return
     */
    public static File getServerPath() {
        return cached.get("Servers");
    }

    /**
     * Returns the Parabot cache folder.
     *
     * @return
     */
    public static File getCachePath() {
        return cached.get("Cache");
    }
    
    /**
     * Returns the redirected Home Directory
     * @return
     */
	public static File getHomeDir() {
		return cached.get("Home");
	}

    /**
     * Validates all directories and makes them if necessary
     */
    public static void validate() {
        final File defaultPath = getDefaultDirectory();
        if (defaultPath == null || !defaultPath.exists()) {
            throw new RuntimeException("Default path not found");
        }
        final Queue<File> files = new LinkedList<File>();
        files.addAll(cached.values());
        while (files.size() > 0) {
            final File file = files.poll();
            if (!file.exists()) {
                Core.verbose("Generating directory: " + file.getAbsolutePath());
                file.mkdirs();
                if (!file.exists()) {
                    System.err.println("Failed to make directory: " + file.getAbsolutePath());
                }
            }
        }
    }

    private static File temp = null;

    public static File getTempDirectory() {
        if (temp != null) {
            return temp;
        }
        int randomNum = new Random().nextInt(999999999);
        temp = new File(getResourcesPath(), randomNum + "/");
        temp.mkdirs();
        temp.deleteOnExit();
        return temp;
    }

    /**
     * Clears the cache based on the latest modification
     *
     * @param remove A long that represents the amount of seconds that a file may have since the latest modification
     */
    private static void clearCache(int remove) {
        File[] cache = getCachePath().listFiles();
        if (cache != null) {
            for (File f : cache) {
                if (f != null && System.currentTimeMillis() / 1000 - f.lastModified() / 1000 > remove) {
                    Core.verbose("Clearing " + f.getName() + " from cache...");
                    f.delete();
                }
            }
        }
    }

    public static void clearCache() {
        File[] cache = getCachePath().listFiles();
        if (cache != null) {
            for (File f : cache) {
                Core.verbose("Clearing " + f.getName() + " from cache...");
                f.delete();
            }
        }
    }
}
