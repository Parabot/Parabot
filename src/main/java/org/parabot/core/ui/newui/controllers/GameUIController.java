package org.parabot.core.ui.newui.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.parabot.core.Core;
import org.parabot.core.ui.newui.BotUI;

/**
 * @author Fryslan, JKetelaar
 */
public class GameUIController {

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
    void resizePanel(MouseEvent e) {
        double width = rootPanel.getWidth();
        if (width == 692) {
            expand();
        } else {
            collapse();
        }
    }

    @FXML
    void openDebug(MouseEvent e) {
        //// TODO: 22-2-2017 open debug panel

        Stage stage = (Stage) gamePanel.getScene().getWindow();
        Core.getInjector().getInstance(BotUI.class).switchState(BotUI.ViewState.DEBUG, stage);
    }

    @FXML
    void takeScreenshot(MouseEvent e) {
        //// TODO: 22-2-2017 take a screenshot and save it
    }

    @FXML
    void openNetwork(MouseEvent e) {
        //// TODO: 22-2-2017 open network panel
    }

    @FXML
    void openRandoms(MouseEvent e) {
        //// TODO: 22-2-2017 open randoms panel
    }

    @FXML
    void disableDiaglog(MouseEvent e) {
        //// TODO: 22-2-2017 disable dialog
    }

    @FXML
    void openLogger(MouseEvent e) {
        //// TODO: 22-2-2017 open logger
    }

    @FXML
    void openReflectionExplorer(MouseEvent e) {
        //// TODO: 22-2-2017 open reflection explorer panel
    }

    @FXML
    void startScript(MouseEvent e) {
        //// TODO: 22-2-2017 start a new script
    }

    @FXML
    void pauseScript(MouseEvent e) {
        //// TODO: 22-2-2017 pause running script
    }

    @FXML
    void stopScript(MouseEvent e) {
        //// TODO: 22-2-2017 stop running script
    }

    @FXML
    void clearCache(MouseEvent e) {
        //// TODO: 22-2-2017 todo clear cache
    }

    @FXML
    void handleNotifications(MouseEvent e) {
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
    void minimize(MouseEvent e) {
        Stage stage = (Stage) minimizeBox.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void shutdown(MouseEvent e) {
        Stage stage = (Stage) shutdownBox.getScene().getWindow();
        stage.close();
    }

    private void collapse() {
        Stage stage = (Stage) expandCollapseButton.getScene().getWindow();
        stage.setWidth(692);
        gamePanel.setLayoutX(36);
    }

    private void expand() {
        Stage stage = (Stage) expandCollapseButton.getScene().getWindow();
        stage.setWidth(806);
        gamePanel.setLayoutX(150);
    }

}
