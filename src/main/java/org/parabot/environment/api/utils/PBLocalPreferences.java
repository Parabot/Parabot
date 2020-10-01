package org.parabot.environment.api.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.parabot.core.Directories;

import java.io.File;
import java.util.HashMap;

/**
 * Manages preferences in a local file in JSON format in the Parabot settings folder
 *
 * @author AlexanderBielen
 */
public class PBLocalPreferences {
    private static final JSONParser parser = new JSONParser();
    private final File settingsFile;

    public PBLocalPreferences(String fileName) {
        settingsFile = new File(Directories.getSettingsPath() + "/" + secureFileName(fileName));
    }

    /**
     * Gets all settings inside the file
     *
     * @return JSONObject or null if anything went wrong
     */
    public JSONObject getSettings() {
        try {
            String stringContents = FileUtil.getFileContents(settingsFile);
            return (JSONObject) parser.parse(stringContents);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Convert a HashMap to json and writes it to the file
     *
     * @param settings HashMap<String, String>
     * @param append   If true, append to existing settings in file
     */
    public void writeSettings(HashMap<String, String> settings, boolean append) {
        JSONObject existingSettings;
        if (append && (existingSettings = getSettings()) != null) {
            existingSettings.putAll(settings);
            settings = existingSettings;
        }

        try {
            if (!settingsFile.exists()) {
                settingsFile.createNewFile();
            }
            FileUtil.writeFileContents(settingsFile, new JSONObject(settings).toJSONString());

        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

    }

    /**
     * Adds a setting, or overwrites it if it exists
     *
     * @param key   key of the setting
     * @param value value of the setting
     */
    public void addSetting(String key, String value) {
        HashMap<String, String> pair = new HashMap<>();
        pair.put(key, value);
        writeSettings(pair, true);
    }

    /**
     * Fetches a setting
     *
     * @param key key to get the value for
     *
     * @return value that belongs to given key or null if non-existent
     */
    public String getSetting(String key) {
        if (getSettings() == null) {
            return null;
        }

        return getSettings().get(key).toString();
    }

    /**
     * Adjusts an existing setting
     *
     * @param key   key to adjust the value for
     * @param value value for the key
     */
    public void adjustSetting(String key, String value) {
        addSetting(key, value);
    }

    /**
     * Removes a setting
     *
     * @param key key to remove
     */
    public void removeSetting(String key) {
        JSONObject json = getSettings();
        json.remove(key);
        writeSettings(json, false);
    }

    /**
     * Replaces all double dots to make sure the link does not leave the settings folder
     *
     * @param filePath path to secure
     *
     * @return secured string
     */
    private static String secureFileName(String filePath) {
        return filePath.replace("..", "");
    }
}
