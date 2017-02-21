package org.parabot.core.user.implementations;

/**
 * @author JKetelaar
 */
public interface UserLoginActionListener {

    void onLogin(boolean success);

    void afterLogin();

}
