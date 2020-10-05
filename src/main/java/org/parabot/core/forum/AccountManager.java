package org.parabot.core.forum;

import org.json.simple.JSONObject;
import org.parabot.core.Configuration;
import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.core.parsers.hooks.HookParser;
import org.parabot.core.parsers.scripts.BDNScripts;
import org.parabot.core.parsers.servers.PublicServers;
import org.parabot.core.ui.components.VerboseLoader;
import org.parabot.environment.api.utils.PBPreferences;
import org.parabot.environment.api.utils.WebUtil;
import org.parabot.environment.scripts.executers.BDNScriptsExecuter;
import org.parabot.environment.servers.executers.PublicServerExecuter;

import java.io.BufferedReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * Handles logging in to parabot forum, only certain classes may use this class.
 *
 * @author Everel
 */
public final class AccountManager {
    private static boolean validated;
    private static AccountManager instance;

    private Account account;

    private AccountManager() {

    }

    public static final void validate() {
        if (validated) {
            return;
        }
        validated = true;
        instance = new AccountManager();

        Core.verbose("Initializing account manager accessors...");
        final ArrayList<AccountManagerAccess> accessors = new ArrayList<AccountManagerAccess>();
        accessors.add(BDNScripts.MANAGER_FETCHER);
        accessors.add(VerboseLoader.MANAGER_FETCHER);
        accessors.add(BDNScriptsExecuter.MANAGER_FETCHER);
        accessors.add(PublicServers.MANAGER_FETCHER);
        accessors.add(PublicServerExecuter.MANAGER_FETCHER);
        accessors.add(PBPreferences.MANAGER_FETCHER);
        accessors.add(HookParser.MANAGER_FETCHER);

        for (final AccountManagerAccess accessor : accessors) {
            accessor.setManager(instance);
        }
        Core.verbose("Account managers initialized.");
    }

    public final boolean isLoggedIn() {
        return account != null;
    }

    public final Account getAccount() {
        return account;
    }

    public final boolean login(final String user, final String pass, boolean requestTwoStep) {
        if (account != null) {
            throw new IllegalStateException("Already logged in.");
        }
        JSONObject result = null;
        if (!requestTwoStep) {
            try {
                BufferedReader contents = WebUtil.getReader(WebUtil.getConnection(
                        new URL(Configuration.LOGIN_SERVER),
                        URLEncoder.encode(user, "UTF-8"),
                        URLEncoder.encode(pass, "UTF-8")));

                result = (JSONObject) WebUtil.getJsonParser().parse(contents);

            } catch (Throwable t) {
                t.printStackTrace();
                return false;
            }
        } else {
            try {
                String two = JOptionPane.showInputDialog("Please provide your two factor authentication code\nYou can find this in either your email or the app you've setup");
                if (two != null && two.length() > 0) {
                    String contents = WebUtil.getContents(Configuration.LOGIN_SERVER,
                            "username=" + URLEncoder.encode(user, "UTF-8") + "&password=" + URLEncoder.encode(pass, "UTF-8") + "&2fa=" + URLEncoder.encode(two, "UTF-8")
                    );
                    result = (JSONObject) WebUtil.getJsonParser().parse(contents);
                }
            } catch (Throwable t) {
                t.printStackTrace();
                return false;
            }
        }

        if (result != null) {
            if (result.get("complete") != null) {
                String api = (String) ((JSONObject) result.get("data")).get("api");
                account = new Account(user, pass, api);
                Context.setUsername(user);
                return true;
            } else if (result.get("error") != null) {
                String errorResult = (String) result.get("error");
                if (errorResult.equals("2fa") || errorResult.equals("2fae")) {
                    return login(user, pass, true);
                }
                Core.verbose(errorResult);
                return false;
            }
        }
        return false;
    }

}
