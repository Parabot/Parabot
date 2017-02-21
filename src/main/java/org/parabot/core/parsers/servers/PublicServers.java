package org.parabot.core.parsers.servers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.parabot.core.bdn.api.APICaller;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.user.SharedUserAuthenticator;
import org.parabot.core.user.UserAuthenticatorAccess;
import org.parabot.environment.servers.executers.PublicServerExecuter;

/**
 * Parses servers hosted on Parabot
 *
 * @author JKetelaar, Everel
 */
public class PublicServers extends ServerParser {

    private static SharedUserAuthenticator authenticator;

    @Override
    public void execute() {
        JSONArray object = (JSONArray) APICaller.callPoint(APICaller.APIPoint.LIST_SERVERS, authenticator);

        for (Object o : object) {
            JSONObject jsonObject = (JSONObject) o;

            String name = (String) jsonObject.get("name");
            double version;
            try {
                version = (Double) jsonObject.get("version");
            }catch (ClassCastException e){
                version = (Long) jsonObject.get("version");
            }
            JSONArray jsonAuthors = (JSONArray) jsonObject.get("authors");
            String[] authors = new String[jsonAuthors.size()];

            for (int i = 0; i < authors.length; i++) {
                JSONObject author = (JSONObject) jsonAuthors.get(i);
                authors[i] = (String) author.get("username");
            }

            ServerDescription desc = new ServerDescription(name,
                    authors, version);
            SERVER_CACHE.put(desc, new PublicServerExecuter(name));
        }
    }

    public static final UserAuthenticatorAccess AUTHENTICATOR = new UserAuthenticatorAccess() {
        @Override
        public void setUserAuthenticator(SharedUserAuthenticator userAuthenticator) {
            authenticator = userAuthenticator;
        }
    };
}
