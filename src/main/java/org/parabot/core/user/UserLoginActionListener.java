package org.parabot.core.user;

/**
 * @author JKetelaar
 */
public interface UserLoginActionListener {

    void onLogin(boolean success);

    void afterLogin();

}
