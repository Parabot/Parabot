package org.parabot.core.bdn.api.slack;

import com.google.inject.Inject;
import org.json.simple.JSONObject;
import org.parabot.api.notifications.NotificationManager;
import org.parabot.api.notifications.types.NotificationType;
import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.core.bdn.api.APICaller;
import org.parabot.core.user.SharedUserAuthenticator;
import org.parabot.core.user.implementations.UserAuthenticatorAccess;
import org.parabot.core.user.implementations.UserLoginActionListener;
import org.parabot.environment.scripts.Script;

/**
 * @author JKetelaar
 */
public class SlackNotification extends NotificationType implements UserAuthenticatorAccess {

    public static final UserLoginActionListener USER_LOGIN_ACTION_LISTENER = new UserLoginActionListener() {
        @Override
        public void onLogin(boolean success) {
        }

        @Override
        public void afterLogin() {
            NotificationManager.getContext().addNotificationType(new SlackNotification());
        }
    };
    private SharedUserAuthenticator authenticator;

    public SlackNotification() {
        super("Slack");
    }

    @Override
    public boolean isAvailable() {
        JSONObject object = (JSONObject) APICaller.callPoint(APICaller.APIPoint.IN_SLACK, authenticator);
        return object != null && ((boolean) object.get("result"));
    }

    @Override
    public void notify(String s, String s1, String s2) {
        this.notify(s2);
    }

    @Override
    public void notify(String header, String message) {
        this.notify(message);
    }

    @Override
    public void notify(String message) {
        Script script;
        if ((script = Core.getInjector().getInstance(Context.class).getRunningScript()) != null) {
            APICaller.APIPoint apiPoint = APICaller.APIPoint.SEND_SLACK.setPointParams(script.getScriptID());

            APICaller.callPoint(apiPoint, authenticator, "Message=" + message);
        }
    }

    @Override
    @Inject
    public void setUserAuthenticator(SharedUserAuthenticator userAuthenticator) {
        this.authenticator = userAuthenticator;
    }
}
