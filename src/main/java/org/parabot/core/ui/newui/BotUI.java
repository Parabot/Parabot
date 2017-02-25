package org.parabot.core.ui.newui;

import com.google.inject.Singleton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.parabot.api.io.images.Images;
import org.parabot.api.misc.OperatingSystem;
import org.parabot.core.Core;
import org.parabot.core.ui.newui.controllers.GameUIController;
import org.parabot.core.user.UserAuthenticator;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Fryslan, JKetelaar
 */
@Singleton
public class BotUI extends Application {

    public void start() {
        BotUI botUI = Core.getInjector().getInstance(BotUI.class);
        Application.launch(botUI.getClass());
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.UNDECORATED);
        setIcon(stage);
        switchState(ViewState.LOGIN, stage);
    }

    private void setIcon(Stage stage) {
        stage.getIcons().add(new Image("/storage/images/icon.png"));
        if (OperatingSystem.getOS() == OperatingSystem.MAC) {
            try {
                Class  t                = Class.forName("com.apple.eawt.Application");
                Object application      = t.getMethod("getApplication", new Class[0]).invoke(null, new Object[0]);
                Method setDockIconImage = t.getMethod("setDockIconImage", java.awt.Image.class);
                setDockIconImage.invoke(application, Images.getResource("/storage/images/icon.png"));
            } catch (Throwable var5) {
                var5.printStackTrace();
            }
        }
    }

    private void switchStateScene(ViewState viewState, Stage stage) {
        Core.verbose("Switching state to: " + viewState.name());

        Platform.runLater(() -> {
            try {
                Parent root = FXMLLoader.load(BotUI.class.getResource(viewState.getFile()));

                Scene scene = new Scene(root);
                stage.setScene(scene);

                if (viewState.getWidth() != null) {
                    stage.setWidth(viewState.getWidth());
                }

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void switchState(ViewState viewState, Stage stage) {
        if (viewState.isRequiresLogin()) {
            Thread login = new Thread(() -> {
                UserAuthenticator authenticator = Core.getInjector().getInstance(UserAuthenticator.class);
                if (authenticator.getAccessToken() == null) {
                    Core.verbose("User not logged in, view requires logged in state");
                }
            });

            try {
                login.join(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        switchStateScene(viewState, stage);
    }

    public enum ViewState {
        DEBUG("/storage/ui/debugs.fxml", null, true),
        GAME("/storage/ui/game.fxml", GameUIController.WIDTH, true),
        SERVER_SELECTOR("/storage/ui/server_selector.fxml", null, true),
        LOGIN("/storage/ui/login.fxml", null, false);

        private String  file;
        private Integer width;
        private boolean requiresLogin;

        ViewState(String file, Integer width, boolean requiresLogin) {
            this.file = file;
            this.width = width;
            this.requiresLogin = requiresLogin;
        }

        public boolean isRequiresLogin() {
            return requiresLogin;
        }

        public Integer getWidth() {
            return width;
        }

        public String getFile() {
            return file;
        }
    }
}
