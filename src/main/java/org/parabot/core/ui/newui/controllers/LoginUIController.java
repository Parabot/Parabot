package org.parabot.core.ui.newui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.parabot.core.Core;
import org.parabot.core.settings.Configuration;
import org.parabot.core.ui.newui.BotUI;
import org.parabot.core.ui.newui.components.DialogHelper;
import org.parabot.core.user.UserAuthenticator;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Fryslan, JKetelaar
 */
public class LoginUIController implements Initializable {

    @FXML
    private AnchorPane                  loginPanel;
    @FXML
    private Label                       title, description;
    @FXML
    private javafx.scene.control.Button loginButton, registerButton;

    private boolean disabled;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.title.setText(Configuration.BOT_TITLE);
        this.description.setText(Configuration.BOT_SLOGAN);
    }

    @FXML
    private void login(ActionEvent event) {
        UserAuthenticator authenticator = Core.getInjector().getInstance(UserAuthenticator.class);

        Thread login = new Thread(() -> {
            this.toggleButtons();
            if (authenticator.login()) {
                Stage stage = (Stage) loginPanel.getScene().getWindow();
                Core.getInjector().getInstance(BotUI.class).switchState(BotUI.ViewState.SERVER_SELECTOR, stage);
            }
            this.toggleButtons();
        });

        login.start();
    }

    @FXML
    private void register(ActionEvent event) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(Configuration.REGISTRATION_PAGE));
            }
        } catch (IOException | URISyntaxException e) {
            DialogHelper.showError("Login", "Could not open browser", "Go to " + Configuration.REGISTRATION_PAGE + " to register an account");
            e.printStackTrace();
        }
    }

    private void toggleButtons() {
        loginButton.setDisable(!disabled);
        registerButton.setDisable(!disabled);
        disabled = !disabled;
    }
}
