package org.parabot.core.user;

/**
 * @author JKetelaar
 */
public class UserAuthenticationTest {
    public static void main(String[] args) {
        UserAuthenticator authenticator = new UserAuthenticator("27_3esr3gc8g8ow088k0c0kckcg84gssgkc8gsgo040owgkg8owkg");

        authenticator.login();

        System.exit(0);

        int tries = 0;
        while (!authenticator.login() && tries < 5){
            tries++;
        }
    }
}
