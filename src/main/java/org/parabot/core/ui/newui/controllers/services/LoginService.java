package org.parabot.core.ui.newui.controllers.services;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import org.parabot.core.bdn.api.APIConfiguration;
import org.parabot.core.ui.newui.BrowserUI;
import org.parabot.core.user.UserAuthenticator;

import java.util.concurrent.CountDownLatch;

/**
 * @author JKetelaar
 */
public class LoginService extends Service {

    private UserAuthenticator authenticator;
    private Stage             stage;
    private boolean           result;
    private WebEngine         engine;

    public LoginService(UserAuthenticator authenticator, Stage stage) {
        this.authenticator = authenticator;
        this.stage = stage;

        this.result = false;
        this.engine = null;

        this.authenticator.setLoginService(this);
    }

    @Override
    protected Task createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                final CountDownLatch latch = new CountDownLatch(1);

                result = authenticator.loginWithTokens();

                if (!result) {
                    Platform.runLater(() -> {
                        try {
                            String url = String.format(APIConfiguration.CREATE_COPY_LOGIN, APIConfiguration.OAUTH_CLIENT_ID);
                            BrowserUI browserUI = BrowserUI.getBrowser();
                            engine = browserUI.getController().getWebView().getEngine();

                            browserUI.loadPage(url);
                        } finally {
                            latch.countDown();
                        }
                    });

                    while (engine == null) {
                        Thread.sleep(5);
                    }

                    result = authenticator.loginWithWebsite();

                    Platform.runLater(() -> BrowserUI.getBrowser().hide());
                    latch.await();
                }

                return null;
            }
        };
    }

    public WebEngine getEngine() {
        return engine;
    }

    public boolean getResult() {
        return result;
    }
}
