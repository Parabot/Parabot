package org.parabot.core.user;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.parabot.api.io.Directories;
import org.parabot.core.Core;
import org.parabot.core.bdn.api.APIConfiguration;
import org.parabot.core.user.OAuth.AuthorizationCode;
import org.parabot.environment.api.utils.WebUtil;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

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
            HttpURLConnection urlConnection = (HttpURLConnection) WebUtil.getConnection(new URL(String.format(APIConfiguration.VALIDATE_ACCESS_TOKEN, accessToken)));
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
                    code = getAuthorizationCodes(TokenRequestType.REFRESH_TOKEN.createParameters(clientId, code.getRefreshToken(), APIConfiguration.CLOSE_PAGE));
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

    private AuthorizationCode getAuthorizationCodes(String parameters) {
        try {
            URL url1 = new URL(APIConfiguration.INTERNAL_ROUTE_CLIENT);
            URLConnection connection = WebUtil.getConnection(url1);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(parameters);
            wr.flush();
            wr.close();
            return AuthorizationCode.readResponse(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean redirectToLogin() {
        String url = String.format(APIConfiguration.CREATE_COPY_LOGIN, this.clientId);
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

            AuthorizationCode c = getAuthorizationCodes(TokenRequestType.AUTHORIZATION_CODE.createParameters(clientId, s, APIConfiguration.COPY_LOGIN));
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
        REFRESH_TOKEN("refresh_token", "refresh_token"),
        AUTHORIZATION_CODE("code", "authorization_code");

        private String dataType;
        private String grandType;

        TokenRequestType(String dataType, String grandType) {
            this.dataType = dataType;
            this.grandType = grandType;
        }

        public String getDataType() {
            return dataType;
        }

        public String createParameters(String clientId, String code, String closeURL) {
            try {
                closeURL = URLEncoder.encode(closeURL, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return "client_id" + "=" + clientId + "&" +
                    "grant_type" + "=" + this.grandType + "&" +
                    this.dataType + "=" + code + "&" +
                    "redirect_uri" + "=" + closeURL;
        }
    }
}
