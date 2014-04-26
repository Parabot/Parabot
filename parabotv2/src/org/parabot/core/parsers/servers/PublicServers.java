package org.parabot.core.parsers.servers;

import org.parabot.core.Configuration;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.forum.AccountManager;
import org.parabot.core.forum.AccountManagerAccess;
import org.parabot.environment.api.utils.WebUtil;
import org.parabot.environment.servers.executers.PublicServerExecuter;

import java.io.BufferedReader;
import java.net.URL;

/**
 * Parses servers hosted on parabot
 *
 * @author Everel
 */
public class PublicServers extends ServerParser {
	
	private static AccountManager manager;

	public static final AccountManagerAccess MANAGER_FETCHER = new AccountManagerAccess() {

		@Override
		public final void setManager(AccountManager manager) {
			PublicServers.manager = manager;
		}

	};

    @Override
    public void execute() {
        try {
            BufferedReader br = WebUtil.getReader(new URL(
                    Configuration.GET_SERVER_PROVIDERS), manager.getAccount().getUsername(), manager.getAccount().getPassword());
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
                        // serverID
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
