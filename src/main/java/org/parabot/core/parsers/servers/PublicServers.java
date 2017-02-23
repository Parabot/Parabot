package org.parabot.core.parsers.servers;

import com.google.inject.Inject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.parabot.core.bdn.api.APICaller;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.user.SharedUserAuthenticator;
import org.parabot.core.user.implementations.UserAuthenticatorAccess;
import org.parabot.environment.servers.executers.PublicServerExecutor;

/**
 * Parses servers hosted on Parabot
 *
 * @author JKetelaar, Everel
 */
public class PublicServers extends ServerParser implements UserAuthenticatorAccess {

    private SharedUserAuthenticator authenticator;

    @Override
    public void execute() {
        JSONArray object = (JSONArray) APICaller.callPoint(APICaller.APIPoint.LIST_SERVERS, authenticator);

        for (Object o : object) {
            JSONObject jsonObject = (JSONObject) o;

            long   id   = (Long) jsonObject.get("id");
            String name = (String) jsonObject.get("name");
            double version;
            try {
                version = (Double) jsonObject.get("version");
            } catch (ClassCastException e) {
                version = (Long) jsonObject.get("version");
            }
            JSONArray jsonAuthors = (JSONArray) jsonObject.get("authors");
            String[]  authors     = new String[jsonAuthors.size()];

            for (int i = 0; i < authors.length; i++) {
                JSONObject author = (JSONObject) jsonAuthors.get(i);
                authors[i] = (String) author.get("username");
            }
            String    description = (String) jsonObject.get("description");
            JSONArray details     = (JSONArray) jsonObject.get("authors");

            ServerDescription desc = new ServerDescription((int) id, name,
                    authors, version, description, details);
            SERVER_CACHE.put(desc, new PublicServerExecutor(desc));
        }
    }

    @Override
    @Inject
    public void setUserAuthenticator(SharedUserAuthenticator userAuthenticator) {
        this.authenticator = userAuthenticator;
    }
}
