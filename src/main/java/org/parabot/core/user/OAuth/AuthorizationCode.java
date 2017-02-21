package org.parabot.core.user.OAuth;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.parabot.api.io.WebUtil;
import org.parabot.core.Core;

import java.io.IOException;
import java.net.URLConnection;

/**
 * @author JKetelaar
 */
public class AuthorizationCode {

    private final String accessToken;
    private final String refreshToken;
    private final long time;

    public AuthorizationCode(String accessToken, String refreshToken, long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        time = System.currentTimeMillis() / 1000 + expiresIn;
    }

    public static AuthorizationCode readResponse(URLConnection connection) {
        try {
            JSONObject object = (JSONObject) WebUtil.getJsonParser().parse(WebUtil.getReader(connection));

            Object error;
            if ((error = object.get("error")) == null) {
                return new AuthorizationCode((String) object.get("access_token"), (String) object.get("refresh_token"), (Long) object.get("expires_in"));
            } else {
                Core.verbose("Error: " + error);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Checks if the access token will expire within the next 30 seconds
     *
     * @return True if it is expiring, false if not
     */
    public boolean isExpiring() {
        return System.currentTimeMillis() / 1000 - 30 > time;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
