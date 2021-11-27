package org.parabot.core.desc;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.parabot.core.Configuration;
import org.parabot.core.Core;
import org.parabot.environment.api.utils.WebUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.CRC32;

/**
 * Gets the information for the selected server provider
 *
 * @author Paradox, Everel
 */
public class ServerProviderInfo {

    private final HashMap<String, Integer> settings;
    private final Properties properties;

    public ServerProviderInfo(URL providerInfo, String username, String password) {
        this.properties = new Properties();
        this.settings = new HashMap<>();
        try {
            Core.verbose("Reading info: " + providerInfo);
            BufferedReader br = WebUtil.getReader(new URL(providerInfo.toString()), username, password);

            JSONObject jsonObject = (JSONObject) WebUtil.getJsonParser().parse(br);
            for (Object o : jsonObject.entrySet()) {
                Map.Entry<?, ?> pairs = (Map.Entry<?, ?>) o;
                if (String.valueOf(pairs.getKey()).equalsIgnoreCase("settings")) {
                    JSONObject object = (JSONObject) pairs.getValue();
                    parseSettings(object);
                } else {
                    properties.put(String.valueOf(pairs.getKey()), String.valueOf(pairs.getValue()));
                }
            }
            if (br != null) {
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize configuration with data provided by {@link org.parabot.core.parsers.servers.LocalServers} from a {@code /parabot/servers/config.json} file. Also loads the default Settings map from the BDN.
     *
     * @param clientJar   Name of the client jar file
     * @param hooks       Name of the hooks file
     * @param name        Server name
     * @param clientClass Entry class within the client jar
     * @param bankTabs    Bank tabs - only relevant for certain servers. Default 0
     */
    public ServerProviderInfo(String clientJar, String hooks, String name, String clientClass, int bankTabs) {
        this(clientJar, hooks, name, clientClass, bankTabs, null);
    }

    /**
     * Initialize configuration with data provided by {@link org.parabot.core.parsers.servers.LocalServers} from a {@code /parabot/servers/config.json} file. Also loads the default Settings map from the BDN.
     *
     * @param clientJar   Name of the client jar file
     * @param hooks       Name of the hooks file
     * @param name        Server name
     * @param clientClass Entry class within the client jar
     * @param bankTabs    Bank tabs - only relevant for certain servers. Default 0
     * @param randoms     A URL to an endpoint where the Randoms are located. Can be Null, in which case getRandoms() will fallback to the default BDN Randoms URL.
     */
    public ServerProviderInfo(String clientJar, String hooks, String name, String clientClass, int bankTabs, String randoms) {
        this.properties = new Properties();
        this.settings = new HashMap<>();

        try {
            BufferedReader br = WebUtil.getReader(new URL(Configuration.GET_SERVER_SETTINGS));
            JSONObject settings = (JSONObject) WebUtil.getJsonParser().parse(br);
            parseSettings(settings);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        this.properties.setProperty("client_jar", clientJar);
        this.properties.setProperty("hooks", hooks);
        this.properties.setProperty("name", name);
        this.properties.setProperty("client_class", clientClass);
        this.properties.setProperty("provider_crc32", String.valueOf(getCRC32(name, "provider")));
        this.properties.setProperty("client_crc32", String.valueOf(getCRC32(name, "client")));
        this.properties.setProperty("bank_tabs", String.valueOf(bankTabs));
        this.properties.setProperty("randoms_jar", randoms);
    }

    public URL getClient() {
        try {
            return new URL(properties.getProperty("client_jar"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public URL getExtendedHookFile() {
        try {
            return new URL(properties.getProperty("hooks") /*+ "&extended=true"*/);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return getHookFile();
        }
    }

    public URL getHookFile() {
        try {
            return new URL(properties.getProperty("hooks"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getClientClass() {
        return properties.getProperty("client_class");
    }

    public String getServerName() {
        return properties.getProperty("name");
    }

    public long getCRC32() {
        return Long.parseLong(properties.getProperty("provider_crc32"));
    }

    public long getClientCRC32() {
        return Long.parseLong(properties.getProperty("client_crc32"));
    }

    public int getBankTabs() {
        return Integer.parseInt(properties.getProperty("bank_tabs"));
    }

    public Properties getProperties() {
        return this.properties;
    }

    public HashMap<String, Integer> getSettings() {
        return settings;
    }

    /**
     * Gets the URL to download the Randoms JAR from.
     *
     * @return The provided URL in the server config JSON (denoted by 'randoms:') or, fallback to the default BDN URL.
     */
    public URL getRandoms() {
        try {
            String randomsUrl = properties.getProperty("randoms_jar");
            if (randomsUrl == null || randomsUrl.length() == 0) {
                // Fallback to default BDN URL if there is no 'randoms' specified in the server JSON configuration.
                randomsUrl = Configuration.GET_RANDOMS + (Configuration.BOT_VERSION.isNightly() ? Configuration.NIGHTLY_APPEND : "");
            }
            return new URL(randomsUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // Will never return null, unless the BDN URL is changed. It shouldn't be.
        return null;
    }

    /**
     * Gets the current provider version
     *
     * @return provider version
     */
    public String getProviderVersion() {
        String providerType = WebUtil.getJsonValue(String.format(Configuration.GET_SERVER_PROVIDER_TYPE, properties.getProperty("name")), "type");
        if (providerType != null) {
            String providerInfo = String.format(Configuration.SERVER_PROVIDER_INFO, providerType);
            return WebUtil.getJsonValue(providerInfo, "version");
        }

        return null;
    }

    private long getCRC32(String name, String type) {
        CRC32 crc = new CRC32();
        name += "-" + type;
        crc.update(name.getBytes());
        return crc.getValue();
    }

    private void parseSettings(JSONObject object) {
        for (Object settingObject : object.entrySet()) {
            Map.Entry<?, ?> settingValue = (Map.Entry<?, ?>) settingObject;
            String key = (String) settingValue.getKey();
            long value = (Long) settingValue.getValue();
            settings.put(key, (int) value);
        }
    }
}
