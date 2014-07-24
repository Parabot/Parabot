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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Gets the information for the selected server provider
 *
 * @author Paradox, Everel
 *
 */
public class ServerProviderInfo {
	private HashMap<String, String> properties;
	
	public ServerProviderInfo(URL providerInfo, String username, String password) {
		this.properties = new HashMap<>();
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
            }else{
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
			return new URL(properties.get("client"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public URL getHookFile() {
		try {
			return new URL(properties.get("hooks"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getClientClass() {
		return properties.get("clientClass");
	}
	
	public String getServerName() {
		return properties.get("serverName");
	}
	
	public long getCRC32() {
        if (properties.get("crc32") != null) {
            return Long.parseLong(properties.get("crc32"));
        }else{
            return (long) new Random().nextInt(2000) + 1;
        }
	}
	
	public long getClientCRC32() {
		return Long.parseLong(properties.get("clientCrc32"));
	}
	
	public int getBankTabs() {
		return Integer.parseInt(properties.get("bankTabs"));
	}
	
	public HashMap<String, String> getProperties() {
		return this.properties;
	}
}
