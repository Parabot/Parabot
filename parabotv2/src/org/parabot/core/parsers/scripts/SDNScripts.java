package org.parabot.core.parsers.scripts;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.parabot.core.Configuration;
import org.parabot.core.desc.ScriptDescription;
import org.parabot.core.forum.AccountManager;
import org.parabot.core.forum.AccountManagerAccess;
import org.parabot.environment.api.utils.WebUtil;
import org.parabot.environment.scripts.executers.SDNScriptExecuter;

import java.io.BufferedReader;
import java.net.URL;

/**
 * Parses scripts stored on the sdn of Parabot
 *
 * @author Everel, Paradox
 */
public class SDNScripts extends ScriptParser {
    private static AccountManager manager;

    public static final AccountManagerAccess MANAGER_FETCHER = new AccountManagerAccess() {

        @Override
        public final void setManager(AccountManager manager) {
            SDNScripts.manager = manager;
        }

    };

    @Override
    public void execute(){
        if (!manager.isLoggedIn()) {
            System.err.println("Not logged in...");
            return;
        }

        JSONParser parser = new JSONParser();
        try {
            BufferedReader br = WebUtil.getReader(new URL(String.format(Configuration.SDN_SCRIPTS_JSON, manager.getAccount()
                    .getUsername())));

            String line;


            while ((line = br.readLine()) != null){
                Object obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                String jarName = String.valueOf(jsonObject.get("jarname"));
                int sdnId = Integer.parseInt(String.valueOf(jsonObject.get("scriptid")));
                String scriptName = String.valueOf(jsonObject.get("scriptname"));
                String author = String.valueOf(jsonObject.get("author"));
                double version = Double.parseDouble(String.valueOf(jsonObject.get("version")));
                String category = String.valueOf(jsonObject.get("category"));
                String description = String.valueOf(jsonObject.get("description"));

                final ScriptDescription desc = new ScriptDescription(jarName, scriptName,
                        author, category, version, description,
                        null, sdnId);
                SCRIPT_CACHE.put(desc, new SDNScriptExecuter(sdnId));

            }

            br.close();

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
