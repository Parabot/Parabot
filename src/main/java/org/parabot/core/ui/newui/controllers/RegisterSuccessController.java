package org.parabot.core.ui.newui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.parabot.core.Core;
import org.parabot.core.ui.newui.BotUI;

/**
 * @author Fryslan
 */
public class RegisterSuccessController {
    @FXML
    private Button returnButton;

    @FXML
    private void returnToBot(MouseEvent event) {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        Core.getInjector().getInstance(BotUI.class).switchState(BotUI.ViewState.LOGIN, stage);
    }
}
