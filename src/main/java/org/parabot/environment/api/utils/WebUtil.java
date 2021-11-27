package org.parabot.environment.api.utils;

import org.json.simple.JSONObject;

/**
 * A WebUtil class fetches data from an URL
 *
 * @author Everel
 */
public class WebUtil extends org.parabot.api.io.WebUtil {

    /**
     * Fetches a single value from a JSON string at the given url
     *
     * @param url url to get the JSON string from
     * @param key key to search for in the JSON string
     *
     * @return value that belongs to given key
     */
    public static String getJsonValue(String url, String key) {
        try {
            String response = WebUtil.getContents(url);

            if (response.length() > 0) {
                JSONObject jsonObject = (JSONObject) WebUtil.getJsonParser().parse(response);
                if (jsonObject.get(key) != null) {
                    return jsonObject.get(key).toString();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
