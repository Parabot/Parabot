package org.parabot.core.user;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.parabot.api.io.Directories;
import org.parabot.core.Core;
import org.parabot.core.user.OAuth.AuthorizationCode;
import org.parabot.environment.api.utils.WebUtil;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author JKetelaar, Capslock
 */
public class UserAuthenticator {

    private final String clientId;
    private AuthorizationCode authorizationCode;
    private static final String closeURL = "http://local.v3.bdn.parabot.org:88/app_dev.php/close";

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
            HttpURLConnection urlConnection = (HttpURLConnection) WebUtil.getConnection(new URL("http://local.v3.bdn.parabot.org:88/app_dev.php/api/users/oauth/v2/valid?access_token=" + accessToken));
            if (urlConnection != null && urlConnection.getResponseCode() == 200) {
                BufferedReader bufferedReader = WebUtil.getReader(urlConnection);
                if (bufferedReader != null) {
                    JSONObject result = (JSONObject) WebUtil.getJsonParser().parse(bufferedReader);
                    if ((boolean) result.get("result")) {
                        return true;
                    }
                }
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

    private void writeTokens(AuthorizationCode code) {
        JSONObject object = new JSONObject();
        object.put("access_token", code.getAccessToken());
        object.put("refresh_token", code.getRefreshToken());

        try (FileWriter fileWriter = new FileWriter(Directories.getSettingsPath() + "/account.json")) {
            fileWriter.write(object.toJSONString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean readTokens() {
        JSONParser parser = WebUtil.getJsonParser();

        try {
            File file = new File(Directories.getSettingsPath() + "/account.json");
            if (file.exists()) {
                Object obj = parser.parse(new FileReader(file));

                JSONObject object = (JSONObject) obj;

                String accessToken = (String) object.get("access_token");
                String refreshToken = (String) object.get("refresh_token");

                AuthorizationCode code = new AuthorizationCode(accessToken, refreshToken);

                if (this.validateAccessToken(code.getAccessToken())) {
                    return true;
                } else {
                    code = getAuthorizationCodes(TokenRequestType.REFRESH_TOKEN.createParameters(clientId, "refresh_token", code.getRefreshToken(), closeURL));
                    if (code != null && code.getAccessToken() != null) {
                        if (this.validateAccessToken(code.getAccessToken())) {
                            this.writeTokens(code);
                            return true;
                        }
                    }
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void forumLogin() {
//        String url = Configuration.V3_API_ENDPOINT + "users/connect/forums?after_login_redirect" + "http://local.v3.bdn.parabot.org:88/app_dev.php/close";
        String url;
        try {
            url = "http://local.v3.bdn.parabot.org:88/app_dev.php/api/users/log_in?after_login_redirect=" + URLEncoder.encode(closeURL, "UTF-8");

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
            if (c != null && c.getAccessToken() != null) {

                if (this.validateAccessToken(c.getAccessToken())) {
                    this.authorizationCode = c;
                    this.writeTokens(this.authorizationCode);

                    return true;
                }
            } else {
                Core.verbose("Authorization code is null");
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
