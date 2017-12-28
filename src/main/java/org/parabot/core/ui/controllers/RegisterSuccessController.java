package org.parabot.core.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import org.parabot.core.Core;
import org.parabot.core.ui.BotUI;

/**
 * @author Fryslan
 */
public class RegisterSuccessController {
    @FXML
    private Button returnButton;

    @FXML
    private void returnToBot(MouseEvent event) {
        Core.getInjector().getInstance(BotUI.class).switchState(BotUI.ViewState.LOGIN);
    }
}
