package org.parabot.environment.api.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.parabot.core.Directories;

import java.io.File;
import java.util.HashMap;

/**
 * Manages preferences in a local json file in the Parabot settings folder
 *
 * @author AlexanderBielen
 */
public class PBLocalPreferences {
    private static JSONParser parser = new JSONParser();
    private File settingsFile;


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
        } catch(Exception ex) {
            return null;
        }
    }

    /**
     * Convert a HashMap to json and writes it to the file
     *
     * @param settings HashMap<String, String>
     * @param append If true, append to existing settings in file
     */
    public void writeSettings(HashMap<String, String> settings, boolean append) {
        JSONObject existingSettings;
        if(append && (existingSettings = getSettings()) != null) {
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
     * @param key
     * @param value
     */
    public void addSetting(String key, String value) {
        HashMap<String, String> pair = new HashMap<>();
        pair.put(key, value);
        writeSettings(pair, true);
    }

    /**
     * Fetches a setting
     *
     * @param key
     * @return
     */
    public String getSetting(String key) {
        if(getSettings() == null) {
            return null;
        }

        return getSettings().get(key).toString();
    }

    /**
     * Adjusts an existing setting
     *
     * @param key
     * @param value
     */
    public void adjustSetting(String key, String value) {
        addSetting(key, value);
    }

    /**
     * Removes a setting
     *
     * @param key
     */
    public void removeSetting(String key) {
        JSONObject json = getSettings();
        json.remove(key);
        writeSettings(json, false);
    }

    private static String secureFileName(String fileName) {
        return fileName.replace("..", "");
    }
}
