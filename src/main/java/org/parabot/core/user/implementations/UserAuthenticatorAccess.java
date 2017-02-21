package org.parabot.core.user.implementations;

import org.parabot.core.user.SharedUserAuthenticator;

/**
 * @author JKetelaar
 */
public interface UserAuthenticatorAccess {
    void setUserAuthenticator(SharedUserAuthenticator userAuthenticator);
}
