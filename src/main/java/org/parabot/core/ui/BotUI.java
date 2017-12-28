package org.parabot.core.ui;

import com.google.inject.Singleton;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.parabot.api.Configuration;
import org.parabot.api.io.images.Images;
import org.parabot.api.misc.OperatingSystem;
import org.parabot.core.Core;
import org.parabot.core.ui.components.GamePanel;
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

    private final JFXPanel  jfxPanel;
    private       GamePanel gamePanel;
    private       GameUI    gameUI;
    private       JPanel    parent;
    private       boolean   locked;
    private       ViewState currentState;

    public BotUI() {
        super(Configuration.BOT_TITLE);
        this.jfxPanel = new JFXPanel();
        this.gameUI = Core.getInjector().getInstance(GameUI.class);
    }

    private void fillBotUI() {
        this.setVisible(true);
        this.setIcon();
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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
        locked = true;

        Core.verbose("Switching state to: " + viewState.name());

        Scene scene = null;
        try {
            Parent root = FXMLLoader.load(BotUI.class.getResource(viewState.getFile()));
            scene = new Scene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (scene != null) {
            if (!viewState.holdsApplet()) {
                this.setContentPane(jfxPanel);
                this.setJfxPanelScene(scene);
            } else {
                if (parent == null) {
                    parent = new JPanel(new BorderLayout());
                    if (gamePanel == null) {
                        gamePanel = this.gameUI.getContent();
                    }
                }

                this.setJfxPanelScene(scene);

                parent.add(jfxPanel, BorderLayout.WEST);
                parent.add(gamePanel, BorderLayout.CENTER);

                this.setContentPane(parent);
            }

            this.pack();
            this.validate();
            this.repaint();

            if (center) {
                this.setLocationRelativeTo(null);
            }
        }

        locked = false;
    }

    private void setJfxPanelScene(Scene scene) {
        this.jfxPanel.setScene(scene);
        this.jfxPanel.setPreferredSize(new Dimension((int) scene.getWidth(), (int) scene.getHeight()));
        this.jfxPanel.repaint();
    }

    private void switchState(ViewState viewState, boolean center) {
        if (this.currentState == null || !this.currentState.holdsApplet()) {
            switchStateScene(ViewState.LOADER, false);
        }

        Thread screen = new Thread(() -> {
            while (locked) {
                Core.verbose("Waiting for lock...");
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

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
        });
        screen.start();

        this.currentState = viewState;
    }

    public void switchState(ViewState viewState) {
        switchState(viewState, false);
    }

    public ViewState getCurrentState() {
        return currentState;
    }

    public enum ViewState {
        DEBUG("/storage/ui/debugs.fxml", true, true),
        GAME("/storage/ui/game.fxml", true, true),
        SERVER_SELECTOR("/storage/ui/server_selector.fxml", true, false),
        LOGIN("/storage/ui/login.fxml", false, false),
        REGISTER("/storage/ui/register.fxml", false, false),
        REGISTER_SUCCESS("/storage/ui/register_success.fxml", false, false),
        BROWSER("/storage/ui/browser.fxml", false, false),
        LOADER("/storage/ui/loader.fxml", false, false);

        private String  file;
        private boolean requiresLogin;
        private boolean applet;

        ViewState(String file, boolean requiresLogin, boolean applet) {
            this.file = file;
            this.requiresLogin = requiresLogin;
            this.applet = applet;
        }

        public boolean holdsApplet() {
            return applet;
        }

        public boolean requiresLogin() {
            return requiresLogin;
        }

        public String getFile() {
            return file;
        }

    }
}
