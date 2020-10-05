package org.parabot.environment.api.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.parabot.core.forum.AccountManager;
import org.parabot.core.forum.AccountManagerAccess;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;

/**
 * @author JKetelaar
 */
public class PBPreferences {

    private static AccountManager manager;
    public static final AccountManagerAccess MANAGER_FETCHER = new AccountManagerAccess() {
        @Override
        public final void setManager(AccountManager manager) {
            PBPreferences.manager = manager;
        }
    };
    private final int scriptID;
    private Properties properties;

    public PBPreferences(int scriptID) {
        this.scriptID = scriptID;
        this.updateSettings();
    }

    /**
     * Change a setting
     *
     * @param key
     * @param value
     */
    public void adjustSettings(Object key, Object value) {
        this.addSetting(key, value);
    }

    /**
     * Get a setting value
     *
     * @param key
     *
     * @return
     */
    public Object getSetting(Object key) {
        return this.properties.get(key);
    }

    /**
     * Get a setting value as string
     *
     * @param key
     *
     * @return
     */
    public String getSetting(String key) {
        return this.properties.getProperty(key);
    }

    /**
     * Remove a setting
     *
     * @param key
     */
    public void removeSetting(Object key) {
        try {
            JSONObject result = (JSONObject) WebUtil.getJsonParser().parse(
                    WebUtil.getContents("http://bdn.parabot.org/api/v2/user/preferences/set/",
                            "apikey=" + manager.getAccount().getApi() +
                                    "&key=" + URLEncoder.encode(String.valueOf(key), "UTF-8") +
                                    "&script=" + scriptID
                    )
            );
            if ((boolean) result.get("result")) {
                this.properties.remove(key);
            }
        } catch (ParseException | MalformedURLException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a setting
     *
     * @param key
     * @param value
     */
    public void addSetting(Object key, Object value) {
        try {
            JSONObject result = (JSONObject) WebUtil.getJsonParser().parse(
                    WebUtil.getContents("http://bdn.parabot.org/api/v2/user/preferences/set/",
                            "apikey=" + manager.getAccount().getApi() +
                                    "&key=" + URLEncoder.encode(String.valueOf(key), "UTF-8") +
                                    "&value=" + URLEncoder.encode(String.valueOf(value), "UTF-8") +
                                    "&script=" + scriptID
                    )
            );
            if ((boolean) result.get("result")) {
                this.properties.put(key, value);
            }
        } catch (ParseException | MalformedURLException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void updateSettings() {
        properties = new Properties();
        try {
            JSONObject result = (JSONObject) WebUtil.getJsonParser().parse(
                    WebUtil.getContents("http://bdn.parabot.org/api/v2/user/preferences/" + scriptID,
                            "apikey=" + manager.getAccount().getApi())
            );

            JSONArray resultArray;
            if ((resultArray = ((JSONArray) result.get("result"))) != null) {
                for (Object rObject : resultArray) {
                    JSONObject resultObject = (JSONObject) rObject;
                    for (Object map : resultObject.entrySet()) {
                        Map.Entry<?, ?> pairs = (Map.Entry<?, ?>) map;
                        properties.put(pairs.getKey(), pairs.getValue());
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}
