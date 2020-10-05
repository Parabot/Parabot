package org.parabot.core.forum;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Class which holds parabot forum account user and pass, only specific classes
 * have access to it unless it's a modified version of parabot intended to
 * steal user information.
 *
 * @author Everel
 */
public class Account {
    private final String username;
    private final String password;
    private String api;

    /**
     * @param username - Forum account username
     * @param password - Forum account password
     */
    public Account(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public Account(String username, String password, String api) {
        this.username = username;
        this.password = password;
        this.api = api;
    }

    /**
     * Gets user's parabot account name
     *
     * @return username.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets user's parabot password
     *
     * @return password.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Gets user's parabot account name
     *
     * @return username, already URL UTF-8 encoded.
     */
    public String getURLUsername() {
        try {
            return URLEncoder.encode(this.username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets user's password
     *
     * @return password, already URL UTF-8 encoded.
     */
    public String getURLPassword() {
        try {
            return URLEncoder.encode(this.password, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getApi() {
        return api;
    }
}
