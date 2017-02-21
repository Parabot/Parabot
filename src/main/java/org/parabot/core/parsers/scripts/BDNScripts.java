package org.parabot.core.parsers.scripts;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.parabot.core.Configuration;
import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.core.desc.ScriptDescription;
import org.parabot.environment.api.utils.WebUtil;
import org.parabot.environment.scripts.executers.BDNScriptsExecuter;

import java.io.BufferedReader;
import java.net.URL;

/**
 * Parses scripts stored on the BDN of Parabot
 *
 * @author JKetelaar, Everel
 */
public class BDNScripts extends ScriptParser {

    @Override
    public void execute() {
        try {
            BufferedReader br = WebUtil.getReader(new URL(
                    "" + Core.getInjector().getInstance(Context.class).getServerProvider().getServerDescription().getServerName()));

            String line;

            while ((line = br.readLine()) != null) {
                JSONObject jsonObject = (JSONObject) Core.getInjector().getInstance(Context.class).getJsonParser().parse(line);
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
