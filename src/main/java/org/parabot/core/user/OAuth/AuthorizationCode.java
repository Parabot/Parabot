package org.parabot.core.user.OAuth;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.parabot.environment.api.utils.WebUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLConnection;

/**
 * @author JKetelaar
 */
public class AuthorizationCode {

    private final String accessToken;
    private final String refreshToken;

    public AuthorizationCode(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public static AuthorizationCode readResponse(URLConnection connection) {
        try {
            JSONObject object = (JSONObject) WebUtil.getJsonParser().parse(WebUtil.getReader(connection));

            return new AuthorizationCode((String) object.get("access_token"), (String) object.get("refresh_token"));
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
