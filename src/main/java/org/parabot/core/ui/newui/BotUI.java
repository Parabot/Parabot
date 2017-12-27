package org.parabot.core.ui.newui;

import com.google.inject.Singleton;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.parabot.api.Configuration;
import org.parabot.api.io.images.Images;
import org.parabot.api.misc.OperatingSystem;
import org.parabot.core.Core;
import org.parabot.core.ui.newui.controllers.GameUIController;
import org.parabot.core.user.UserAuthenticator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * @author Fryslan, JKetelaar
 */
@Singleton
public class BotUI extends JFrame {

    private final JFXPanel jfxPanel;

    public BotUI() {
        super(Configuration.BOT_TITLE);
        jfxPanel = new JFXPanel();
    }

    private void fillBotUI() {
        this.add(jfxPanel);
        this.setVisible(true);
        this.setIcon();
        this.setResizable(false);

        switchState(ViewState.LOGIN, true);
    }

    public void start() {
        this.fillBotUI();
    }

    private void setIcon() {
        try {
            URL           resource = this.getClass().getResource("/storage/images/icon.png");
            BufferedImage image    = ImageIO.read(resource);
            this.setIconImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (OperatingSystem.getOS() == OperatingSystem.MAC) {
            try {
                Class  t                = Class.forName("com.apple.eawt.Application");
                Object application      = t.getMethod("getApplication").invoke(null);
                Method setDockIconImage = t.getMethod("setDockIconImage", java.awt.Image.class);
                setDockIconImage.invoke(application, Images.getResource("/storage/images/icon.png"));
            } catch (Throwable var5) {
                var5.printStackTrace();
            }
        }
    }

    private void switchStateScene(ViewState viewState, boolean center) {
        Core.verbose("Switching state to: " + viewState.name());

        Platform.runLater(() -> {
            try {
                Parent root  = FXMLLoader.load(BotUI.class.getResource(viewState.getFile()));
                Scene  scene = new Scene(root);

                this.jfxPanel.setScene(scene);
                this.getContentPane().setPreferredSize(new Dimension((int) scene.getWidth(), (int) scene.getHeight()));
                this.pack();

                if (center) {
                    this.setLocationRelativeTo(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void switchState(ViewState viewState, boolean center) {
        if (viewState.requiresLogin()) {
            Thread login = new Thread(() -> {
                UserAuthenticator authenticator = Core.getInjector().getInstance(UserAuthenticator.class);
                if (authenticator.getAccessToken() == null) {
                    Core.verbose("User not logged in, view requires logged in state");
                }
            });

            try {
                login.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        switchStateScene(viewState, center);
    }

    public void switchState(ViewState viewState) {
        switchState(viewState, false);
    }

    public enum ViewState {
        DEBUG("/storage/ui/debugs.fxml", null, true),
        GAME("/storage/ui/game.fxml", GameUIController.WIDTH, true),
        SERVER_SELECTOR("/storage/ui/server_selector.fxml", null, true),
        LOGIN("/storage/ui/login.fxml", null, false),
        REGISTER("/storage/ui/register.fxml", null, false),
        REGISTER_SUCCESS("/storage/ui/register_success.fxml", null, false),
        BROWSER("/storage/ui/browser.fxml", null, false),
        LOADER("/storage/ui/loader.fxml", null, false);

        private String  file;
        private Integer width;
        private boolean requiresLogin;

        ViewState(String file, Integer width, boolean requiresLogin) {
            this.file = file;
            this.width = width;
            this.requiresLogin = requiresLogin;
        }

        public boolean requiresLogin() {
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
