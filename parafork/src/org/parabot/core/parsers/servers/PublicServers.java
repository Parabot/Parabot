package org.parabot.core.parsers.servers;

import java.io.BufferedReader;
import java.net.URL;

import org.parabot.core.Configuration;
import org.parabot.core.desc.ServerDescription;
import org.parabot.environment.api.utils.WebUtil;
import org.parabot.environment.servers.PublicServerExecuter;

/**
 * 
 * Parses servers hosted on parabot
 * 
 * @author Everel
 * 
 */
public class PublicServers extends ServerParser {

	@Override
	public void execute() {
		try {
			BufferedReader br = WebUtil.getReader(new URL(
					Configuration.GET_SERVER_PROVIDERS));
			int count = 0;
			String line;

			String name = null;
			String author = null;
			double version = 0D;

			while ((line = br.readLine()) != null) {
				count++;
				switch (count % 4) {
				case 1:
					// server name
					name = line;
					break;
				case 2:
					// author
					author = line;
					break;
				case 3:
					// version
					version = Double.parseDouble(line);
					break;
				case 0:
					// jarName
					ServerDescription desc = new ServerDescription(name,
							author, version);
					SERVER_CACHE.put(desc, new PublicServerExecuter(name, line));
				}
			}
			
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
