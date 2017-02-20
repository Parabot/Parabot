package org.parabot.core.bdn.api;

/**
 * @author JKetelaar
 */
public class APIConfiguration {

    public static final String ENDPOINT = "http://v3.bdn.parabot.org/";
    public static final String API_ENDPOINT = ENDPOINT + "api/";
    public static final String VALID_OAUTH_CHECK = API_ENDPOINT + "users/oauth/v2/valid";
    public static final String CLOSE_PAGE = ENDPOINT + "close";
    public static final String USERS_LOGIN = API_ENDPOINT + "users/log_in?after_login_redirect=%s";
    public static final String INTERNAL_ROUTE_CLIENT = ENDPOINT + "internal/route/oauth/v2/token/client";
    public static final String CREATE_COPY_LOGIN = API_ENDPOINT + "users/oauth/v2/create_copy?clientId=%s";
    public static final String COPY_LOGIN = API_ENDPOINT + "users/oauth/v2/copy";
    public static final String VALIDATE_ACCESS_TOKEN = API_ENDPOINT + "users/oauth/v2/valid?access_token=%s";

}
