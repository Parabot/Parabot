package org.parabot.core.ui.newui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.parabot.core.Core;
import org.parabot.core.ui.newui.BotUI;

/**
 * @author Fryslan, JKetelaar
 */
public class LoginUIController {

    @FXML
    private AnchorPane login_panel;

    @FXML
    private void login(ActionEvent event) {
        Stage stage = (Stage) login_panel.getScene().getWindow();
        Core.getInjector().getInstance(BotUI.class).setServerSelectorInterface(stage);
    }

    @FXML
    private void register(ActionEvent event) {
        // TODO: Send user to register page
    }
}
