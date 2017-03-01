package org.parabot.core.user;

import com.google.inject.Singleton;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.parabot.api.io.Directories;
import org.parabot.api.io.WebUtil;
import org.parabot.core.Core;
import org.parabot.core.bdn.api.APIConfiguration;
import org.parabot.core.bdn.api.slack.SlackNotification;
import org.parabot.core.ui.newui.controllers.services.LoginService;
import org.parabot.core.user.OAuth.AuthorizationCode;
import org.parabot.core.user.implementations.UserLoginActionListener;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author JKetelaar, Capslock
 */
@Singleton
public class UserAuthenticator implements SharedUserAuthenticator, UserLoginActionListener {

    private final String                        clientId;
    private final  List<UserLoginActionListener> userLoginActionListeners;
    private ExecutorService               pool;
    private       AuthorizationCode             authorizationCode;
    private       LoginService                  loginService;

    public UserAuthenticator() {
        this.clientId = APIConfiguration.OAUTH_CLIENT_ID;
        this.userLoginActionListeners = new ArrayList<>();
        this.setUserLoginActionListeners();

        this.setListeners();
    }

    private void setUserLoginActionListeners(){
        this.pool = Executors.newFixedThreadPool(10);
    }

    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
        this.setUserLoginActionListeners();
    }

    private void setListeners() {
        userLoginActionListeners.add(SlackNotification.USER_LOGIN_ACTION_LISTENER);
    }

    public void refreshToken() {
        if (authorizationCode != null && authorizationCode.getRefreshToken() != null) {
            authorizationCode = getAuthorizationCodes(TokenRequestType.REFRESH_TOKEN.createParameters(clientId, authorizationCode.getRefreshToken(), APIConfiguration.CLOSE_PAGE));
        }
    }

    @Override
    public String getAccessToken() {
        if (authorizationCode != null) {
            if (authorizationCode.isExpiring()) {
                refreshToken();
            }

            return authorizationCode.getAccessToken();
        }
        return null;
    }

    public final boolean loginWithTokens(){
        if (readTokens()){
            this.onLogin(true);
            this.afterLogin();
            return true;
        }else{
            this.onLogin(false);
            return false;
        }
    }

    public final boolean loginWithWebsite() {
        if (redirectToLogin()) {
            this.onLogin(true);
            this.afterLogin();
            return true;
        }else {
            this.onLogin(true);
            return false;
        }
    }

    public final boolean login(){
        return loginWithTokens() || loginWithWebsite();
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

                String accessToken  = (String) object.get("access_token");
                String refreshToken = (String) object.get("refresh_token");

                AuthorizationCode code = new AuthorizationCode(accessToken, refreshToken, 3600);

                if (this.validateAccessToken(code.getAccessToken())) {
                    this.authorizationCode = code;
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
            URL           url1       = new URL(APIConfiguration.INTERNAL_ROUTE_CLIENT);
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
        BrowserUserAuthenticator task   = new BrowserUserAuthenticator(loginService.getEngine());
        Future                   future = pool.submit(task);

        while (!future.isDone()) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String result = null;
        try {
            result = (String) future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        pool.shutdown();

        if (result != null) {
            AuthorizationCode c = getAuthorizationCodes(TokenRequestType.AUTHORIZATION_CODE.createParameters(clientId, result, APIConfiguration.COPY_LOGIN));
            if (c != null && c.getAccessToken() != null) {

                if (validateAccessToken(c.getAccessToken())) {
                    authorizationCode = c;
                    writeTokens(authorizationCode);

                    return true;
                }
            }
        }
        Core.verbose("Authorization code is null");

        return false;
    }

    @Override
    public void onLogin(boolean success) {
        for (UserLoginActionListener listener : this.userLoginActionListeners) {
            listener.onLogin(success);
        }
    }

    @Override
    public void afterLogin() {
        for (UserLoginActionListener listener : this.userLoginActionListeners) {
            listener.afterLogin();
        }
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
