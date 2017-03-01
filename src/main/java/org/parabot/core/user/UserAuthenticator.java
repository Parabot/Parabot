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

    /**
     * UserAuthenticator constructor
     */
    public UserAuthenticator() {
        this.clientId = APIConfiguration.OAUTH_CLIENT_ID;
        this.userLoginActionListeners = new ArrayList<>();

        this.setListeners();
        this.setFixedThreadPool();
    }

    /**
     * Setting the pool to a fixed thread pool of 10 threads
     */
    private void setFixedThreadPool(){
        this.pool = Executors.newFixedThreadPool(10);
    }

    /**
     * Settings the login service, including a reset of the pool
     * @param loginService service to be set
     */
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
        this.setFixedThreadPool();
    }

    /**
     * Setting the listeners using UserLoginActionListener
     */
    private void setListeners() {
        userLoginActionListeners.add(SlackNotification.USER_LOGIN_ACTION_LISTENER);
    }

    /**
     * Refreshing the tokens we have, so we won't have expiry errors
     */
    public void refreshToken() {
        if (authorizationCode != null && authorizationCode.getRefreshToken() != null) {
            authorizationCode = getAuthorizationCodes(TokenRequestType.REFRESH_TOKEN.createParameters(clientId, authorizationCode.getRefreshToken(), APIConfiguration.CLOSE_PAGE));
        }
    }

    /**
     * Returns the access token. If expiring it will refresh the tokens
     *
     * @return Access token if available, otherwise null
     */
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

    /**
     * Logs in with tokens we have in storage
     *
     * @return True if logged in went correctly, false if not
     */
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

    /**
     * Logs in with website, most likely to be done when #loginWithTokens doesn't work
     *
     * @return True if logged in went correctly, false if not
     */
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

    /**
     * First checks if login with tokens works, otherwise tries to login with website
     *
     * @return True if either #loginWithTokens or #loginWithWebsite returns true, false if both of them return false
     */
    public final boolean login(){
        return loginWithTokens() || loginWithWebsite();
    }

    /**
     * Validates the received access token against the API, to see if it's (still) valid
     *
     * @param accessToken Token to be checked
     * @return True if valid, false if not
     */
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

    /**
     * Writes the tokens to the tokens file
     *
     * @param code The code object being used to write to the file
     */
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

    /**
     * Reads the tokens from the tokens file and returns true if they're still valid
     *
     * @return True if tokens are valid, false if not
     */
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

    /**
     * Parses the authorization code into OAuth tokens, using the API
     *
     * @param parameters Values to be written to the post to the API
     * @return AuthorizationCode object if response is valid, null if not valid
     */
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

    /**
     * Redirects the user to the login page, with the internal browser
     *
     * @return True if login went fine, false if not
     */
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
