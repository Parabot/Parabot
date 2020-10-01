package org.parabot.core.parsers.servers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.parabot.core.Configuration;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.forum.AccountManager;
import org.parabot.core.forum.AccountManagerAccess;
import org.parabot.core.ui.utils.UILog;
import org.parabot.environment.api.utils.WebUtil;
import org.parabot.environment.servers.executers.PublicServerExecuter;

import java.io.BufferedReader;
import java.net.URL;

import javax.swing.JOptionPane;

/**
 * Parses servers hosted on Parabot
 *
 * @author Paradox, Everel
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
                    Configuration.GET_SERVER_PROVIDERS), manager.getAccount().getURLUsername(), manager.getAccount().getURLPassword());
            String line;

            JSONParser parser = new JSONParser();
            while ((line = br.readLine()) != null) {

                JSONObject jsonObject = (JSONObject) parser.parse(line);
                String name = String.valueOf(jsonObject.get("name"));
                String author = String.valueOf(jsonObject.get("author"));
                double version = Double.parseDouble(String.valueOf(jsonObject.get("version")));

                ServerDescription desc = new ServerDescription(name,
                        author, version);
                SERVER_CACHE.put(desc, new PublicServerExecuter(name));
            }

            br.close();

        } catch (Exception e) {
            UILog.log("Error", "Failed to load public servers. Either disable your anti-virus or request support on the forums.", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
