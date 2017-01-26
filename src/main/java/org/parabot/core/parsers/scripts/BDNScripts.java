package org.parabot.core.parsers.scripts;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.parabot.core.Configuration;
import org.parabot.core.Context;
import org.parabot.core.desc.ScriptDescription;
import org.parabot.core.forum.AccountManager;
import org.parabot.core.forum.AccountManagerAccess;
import org.parabot.environment.api.utils.WebUtil;
import org.parabot.environment.scripts.executers.BDNScriptsExecuter;

import java.io.BufferedReader;
import java.net.URL;

/**
 * Parses scripts stored on the BDN of Parabot
 *
 * @author Paradox, Everel
 */
public class BDNScripts extends ScriptParser {
    private static AccountManager manager;

    public static final AccountManagerAccess MANAGER_FETCHER = new AccountManagerAccess() {

        @Override
        public final void setManager(AccountManager manager) {
            BDNScripts.manager = manager;
        }

    };

    @Override
    public void execute() {
        if (!manager.isLoggedIn()) {
            System.err.println("Not logged in...");
            return;
        }

        JSONParser parser = new JSONParser();
        try {
            BufferedReader br = WebUtil.getReader(new URL(
                            Configuration.GET_SCRIPTS + Context.getInstance().getServerProviderInfo().getServerName()),
                    manager.getAccount().getURLUsername(), manager.getAccount().getURLPassword());

            String line;

            while ((line = br.readLine()) != null) {
                JSONObject jsonObject = (JSONObject) parser.parse(line);
                int bdnId = Integer.parseInt(String.valueOf(jsonObject.get("id")));
                String scriptName = String.valueOf(jsonObject.get("name"));
                String author = String.valueOf(jsonObject.get("author"));
                double version = Double.parseDouble(String.valueOf(jsonObject.get("version")));
                String category = String.valueOf(jsonObject.get("category"));
                String description = String.valueOf(jsonObject.get("description"));

                final ScriptDescription desc = new ScriptDescription(scriptName,
                        author, category, version, description,
                        null, bdnId);
                SCRIPT_CACHE.put(desc, new BDNScriptsExecuter(bdnId));

            }

            br.close();

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
