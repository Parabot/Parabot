package org.parabot.core.parsers.servers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.parabot.core.bdn.api.APICaller;
import org.parabot.core.user.SharedUserAuthenticator;
import org.parabot.core.user.UserAuthenticatorAccess;

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

        for (Object o : object){
            JSONObject jsonObject = (JSONObject) o;

            String name = (String) jsonObject.get("name");
            double version = (Double) jsonObject.get("version");

        }

        System.out.println(object);
    }

    public static final UserAuthenticatorAccess AUTHENTICATOR = new UserAuthenticatorAccess(){
        @Override
        public void setUserAuthenticator(SharedUserAuthenticator userAuthenticator) {
            authenticator = userAuthenticator;
        }
    };
}
