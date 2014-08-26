package org.parabot.core.desc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.parabot.core.Core;
import org.parabot.core.ui.utils.UILog;
import org.parabot.environment.api.utils.WebUtil;

import javax.swing.*;

import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/**
 * Gets the information for the selected server provider
 *
 * @author Paradox, Everel
 *
 */
public class ServerProviderInfo {
	private Properties properties;
	
	public ServerProviderInfo(URL providerInfo, String username, String password) {
		this.properties = new Properties();
        try {
            String line;
            Core.verbose("Reading info: " + providerInfo);
            BufferedReader br = WebUtil.getReader(new URL(providerInfo.toString() + "&method=json"), username, password);

            JSONParser parser = new JSONParser();
            if ((line = br.readLine()) != null) {
                JSONObject jsonObject = (JSONObject) parser.parse(line);
                for (Object o : jsonObject.entrySet()) {
                    Map.Entry<?, ?> pairs = (Map.Entry<?, ?>) o;
                    properties.put(String.valueOf(pairs.getKey()), String.valueOf(pairs.getValue()));
                }
            } else {
                UILog.log(
                        "Error",
                        "Failed to load server provider, error: [No information about the provider found.]",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public URL getClient() {
		try {
			return new URL(properties.getProperty("client"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public URL getExtendedHookFile() {
		try {
			return new URL(properties.get("hooks") + "&extended=true");
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
		return properties.getProperty("clientClass");
	}
	
	public String getServerName() {
		return properties.getProperty("serverName");
	}
	
	public long getCRC32() {
        if (properties.get("crc32") != null) {
            return Long.parseLong(properties.getProperty("crc32"));
        } else {
            return System.currentTimeMillis() / 1000 / 60 / 60 / 24;
        }
	}
	
	public long getClientCRC32() {
		return Long.parseLong(properties.getProperty("clientCrc32"));
	}
	
	public int getBankTabs() {
		return Integer.parseInt(properties.getProperty("bankTabs"));
	}
	
	public Properties getProperties() {
		return this.properties;
	}
}
