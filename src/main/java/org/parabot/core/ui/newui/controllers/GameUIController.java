package org.parabot.core.ui.newui.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.parabot.api.io.Directories;
import org.parabot.core.Core;
import org.parabot.core.ui.newui.BotUI;
import org.parabot.environment.api.utils.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Fryslan, JKetelaar
 */
public class GameUIController {

    public static final int WIDTH             = 692;
    public static final int EXPANDED_WIDTH    = 806;
    public static final int LAYOUT_X          = 36;
    public static final int EXPANDED_LAYOUT_X = 150;

    @FXML
    private AnchorPane gamePanel;
    @FXML
    private ImageView  expandCollapseButton;
    @FXML
    private ImageView  notificationButton;
    @FXML
    private HBox       shutdownBox;
    @FXML
    private HBox       minimizeBox;
    @FXML
    private AnchorPane rootPanel;

    private boolean notificationsEnabled = true;

    @FXML
    private void resizePanel(MouseEvent e) {
        double width = rootPanel.getWidth();
        if (width == WIDTH) {
            expand();
        } else {
            collapse();
        }
    }

    @FXML
    void openDebug(MouseEvent e) {
        Stage stage = (Stage) gamePanel.getScene().getWindow();
        Core.getInjector().getInstance(BotUI.class).switchState(BotUI.ViewState.DEBUG, stage);
    }

    @FXML
    private void takeScreenshot(MouseEvent e) {
        try {
            Robot               robot         = new Robot();
            javafx.stage.Window window        = rootPanel.getScene().getWindow();
            Rectangle           parabotScreen = new Rectangle((int) window.getX(), (int) window.getY(), (int) window.getWidth(), (int) window.getHeight());
            BufferedImage       image         = robot.createScreenCapture(parabotScreen);
            String              randString    = StringUtils.randomString(10);
            boolean             search        = true;
            boolean             duplicate     = false;
            while (search) {
                File[] files;
                if ((files = Directories.getScreenshotDir().listFiles()) != null) {
                    for (File f : files) {
                        if (f.getAbsoluteFile().getName().contains(randString)) {
                            duplicate = true;
                            break;
                        }
                    }
                }
                if (!duplicate) {
                    search = false;
                } else {
                    randString = StringUtils.randomString(10);
                    duplicate = false;
                }
            }
            File file = new File(Directories.getScreenshotDir().getPath() + "/" + randString + ".png");
            ImageIO.write(image, "png", file);

        } catch (IOException | AWTException k) {
            k.printStackTrace();
        }
    }

    @FXML
    private void openNetwork(MouseEvent e) {
        //// TODO: 22-2-2017 open network panel
    }

    @FXML
    private void openRandoms(MouseEvent e) {
        //// TODO: 22-2-2017 open randoms panel
    }

    @FXML
    private void disableDiaglog(MouseEvent e) {
        //// TODO: 22-2-2017 disable dialog
    }

    @FXML
    private void openLogger(MouseEvent e) {
        //// TODO: 22-2-2017 open logger
    }

    @FXML
    private void openReflectionExplorer(MouseEvent e) {
        //// TODO: 22-2-2017 open reflection explorer panel
    }

    @FXML
    private void startScript(MouseEvent e) {
        //// TODO: 22-2-2017 start a new script
    }

    @FXML
    private void pauseScript(MouseEvent e) {
        //// TODO: 22-2-2017 pause running script
    }

    @FXML
    private void stopScript(MouseEvent e) {
        //// TODO: 22-2-2017 stop running script
    }

    @FXML
    private void clearCache(MouseEvent e) {
        Directories.clearCache();
    }

    @FXML
    private void handleNotifications(MouseEvent e) {
        if (notificationsEnabled) {
            notificationButton.setImage(new Image(getClass().getResource("resources/ic_notifications_off_white_24dp.png").toExternalForm()));
            //// TODO: 22-2-2017 enable notifications
            notificationsEnabled = false;
        } else {
            notificationButton.setImage(new Image(getClass().getResource("resources/ic_notifications_active_white_24dp.png").toExternalForm()));
            //// TODO: 22-2-2017 disable notifications
            notificationsEnabled = true;
        }
    }

    @FXML
    private void minimize(MouseEvent e) {
        Stage stage = (Stage) minimizeBox.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void shutdown(MouseEvent e) {
        Stage stage = (Stage) shutdownBox.getScene().getWindow();
        stage.close();
    }

    private void collapse() {
        Stage stage = (Stage) expandCollapseButton.getScene().getWindow();
        stage.setWidth(WIDTH);
        gamePanel.setLayoutX(LAYOUT_X);
    }

    private void expand() {
        Stage stage = (Stage) expandCollapseButton.getScene().getWindow();
        stage.setWidth(EXPANDED_WIDTH);
        gamePanel.setLayoutX(EXPANDED_LAYOUT_X);
    }

}
