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

	private Properties settings;
	private Properties properties;
	
	public ServerProviderInfo(URL providerInfo, String username, String password) {
		this.properties = new Properties();
		this.settings = new Properties();
        try {
            String line;
            Core.verbose("Reading info: " + providerInfo);
            BufferedReader br = WebUtil.getReader(new URL(providerInfo.toString()), username, password);

			//TODO Make this one line (web sided)
            JSONParser parser = new JSONParser();
            if ((line = br.readLine()) != null) {
                JSONObject jsonObject = (JSONObject) parser.parse(line);
                for (Object o : jsonObject.entrySet()) {
                    Map.Entry<?, ?> pairs = (Map.Entry<?, ?>) o;
					if (String.valueOf(pairs.getKey()).equalsIgnoreCase("settings")){
						settings.putAll((JSONObject) pairs.getValue());
					}else {
						properties.put(String.valueOf(pairs.getKey()), String.valueOf(pairs.getValue()));
					}
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

	public Properties getSettings(){
		return this.settings;
	}
}
