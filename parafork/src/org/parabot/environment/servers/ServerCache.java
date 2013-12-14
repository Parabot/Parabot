package org.parabot.environment.servers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.parabot.core.Directories;
import org.parabot.environment.api.utils.WebUtil;

public class ServerCache {

	private static Properties lastModified = new Properties();
	private static final File propFile = new File(Directories.getSettingsPath(),"modified-dates.ini");
	
	static{
		try(InputStream in = new FileInputStream(propFile)){
		lastModified.load(in);
		}catch(Exception e){
			//ignored
		}
	}

	private static String getDate(URL u) throws IOException {
		Map<String, List<String>> values = WebUtil.getHeaders(u);
		List<String> strs = values.get("Last-Modified");
		if (strs == null || strs.size() == 0)
			return null;
		return strs.get(0);
	}

	public static boolean check(URL u) {
		String date = (String) lastModified.get(u.toString());
		String current = null;
		try {
			current = getDate(u);
		} catch (IOException e) {
			return false;
		}
		if (current == null)
			return false;
		return current.equals(date);
	}
	
	public static void setDate(URL u){
		try(OutputStream out = new FileOutputStream(propFile)){
			String date = getDate(u);
			lastModified.put(u.toString(), date);
			lastModified.store(out, "Delete this file if you want to downlaod all the cached server jars again.");
		}catch(Exception e){
			
		}
	}

}
