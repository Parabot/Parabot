package org.parabot.environment.scripts.uliratha;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.parabot.core.Context;
import org.parabot.environment.api.utils.WebUtil;

import java.io.IOException;

/**
 * @author JKetelaar
 */
public class UlirathaExecuter {

    private String api;
    private static boolean isVip = true;

    public UlirathaExecuter(String api){
        this.api = api;
    }

    public void start(int scriptID){
        if (UlirathaExecuter.isVip) {
            String vipUrl = "http://bdn.parabot.org/api/v2/user/" + api + "/vip";
            JSONParser parser = new JSONParser();
            try {
                JSONObject vipObject = (JSONObject) parser.parse(WebUtil.getReader(vipUrl));

                boolean isVip = (boolean) vipObject.get("result");
                if (isVip) {
                    String serverUrl = "http://bdn.parabot.org/api/v2/clients/server";
                    JSONObject serverObject = (JSONObject) parser.parse(WebUtil.getReader(serverUrl));
                    JSONObject detailsObject = (JSONObject) serverObject.get("result");
                    String host = (String) detailsObject.get("host");
                    long port = (long) detailsObject.get("port");

                    UlirathaClient client = new UlirathaClient(host, (int) port, scriptID, api);
                    client.start();
                    Context.getInstance().setUlirathaClient(client);
                }else{
                    UlirathaExecuter.isVip = false;
                }
            } catch (IOException | ParseException | ClassCastException e) {
                e.printStackTrace();
            }
        }
    }
}
