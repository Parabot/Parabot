package org.parabot.core.user;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.parabot.core.user.OAuth.AuthorizationCode;
import org.parabot.environment.api.utils.WebUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author JKetelaar, Capslock
 */
public class UserAuthenticator {

    private final String clientId;

    private AuthorizationCode authorizationCode;

    public UserAuthenticator(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public AuthorizationCode getAuthorizationCode() {
        return authorizationCode;
    }

    private boolean validateAccessToken(String accessToken) {
        try {
            JSONObject result = (JSONObject) WebUtil.getJsonParser().parse(WebUtil.getReader("http://local.v3.bdn.parabot.org:88/app_dev.php/api/users/oauth/v2/valid?access_token=" + accessToken));
            if ((boolean) result.get("result")) {
                return true;
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean login() {
        if (!readTokens()) {
            if (!redirectToLogin()) {
                return false;
            }
        }

        return true;
    }

    private boolean readTokens() {
        return false;
    }

    public void forumLogin() {
//        String url = Configuration.V3_API_ENDPOINT + "users/connect/forums?after_login_redirect" + "http://local.v3.bdn.parabot.org:88/app_dev.php/close";
        String url;
        try {
            url = "http://local.v3.bdn.parabot.org:88/app_dev.php/api/users/log_in?after_login_redirect=" + URLEncoder.encode("http://local.v3.bdn.parabot.org:88/app_dev.php/close", "UTF-8");

            URI uri = URI.create(url);
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, String.format("Please open %s manually", url),
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private AuthorizationCode getAuthorizationCodes(String parameters) {
        try {
            URL url1 = new URL("http://local.v3.bdn.parabot.org:88/app_dev.php/internal/route/oauth/v2/token/client");
            return AuthorizationCode.readResponse(WebUtil.getConnection(url1, parameters));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean redirectToLogin() {
        String url = "http://local.v3.bdn.parabot.org:88/app_dev.php/api/users/oauth/v2/create_copy?clientId=" + this.clientId;
        URI uri = URI.create(url);
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, String.format("Please open %s manually", url),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        String message = "Once you're logged in the page you just opened shows a key.\nPlease paste it in here.";
        String s = JOptionPane.showInputDialog(null, message, "Paste key", JOptionPane.QUESTION_MESSAGE);

        if (s != null) {
            String clientId = this.clientId;
            String grandType = "authorization_code";
            String code = s;
            String closeURL = "http://local.v3.bdn.parabot.org:88/app_dev.php/api/users/oauth/v2/copy";

            AuthorizationCode c = getAuthorizationCodes(TokenRequestType.AUTHORIZATION_CODE.createParameters(clientId, grandType, code, closeURL));

            if (this.validateAccessToken(c.getAccessToken())) {
                this.authorizationCode = c;

                System.out.println("Logged in!");
                return true;
            }
        }
        String failedMessage = "Incorrect key.\nPlease try again.";
        JOptionPane.showMessageDialog(null, failedMessage,
                "Incorrect key", JOptionPane.ERROR_MESSAGE);

        return false;
    }

    public enum TokenRequestType {
        REFRESH_TOKEN("refresh_token"),
        AUTHORIZATION_CODE("code");

        private String dataType;

        TokenRequestType(String dataType) {
            this.dataType = dataType;
        }

        public String getDataType() {
            return dataType;
        }

        public String createParameters(String clientId, String grandType, String code, String closeURL) {
            try {
                closeURL = URLEncoder.encode(closeURL, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return "client_id" + "=" + clientId + "&" +
                    "grant_type" + "=" + grandType + "&" +
                    this.dataType + "=" + code + "&" +
                    "redirect_uri" + "=" + closeURL;
        }
    }
}
