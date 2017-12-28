package org.parabot.core.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.parabot.core.Core;
import org.parabot.core.settings.Configuration;
import org.parabot.core.ui.BotUI;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Fryslan, JKetelaar
 */
public class RegisterUIController implements Initializable {
    @FXML
    private ImageView refreshIcon, returnIcon;
    @FXML
    private WebView    registerWebView;
    @FXML
    private AnchorPane pane;

    @FXML
    private void returnToLogin(MouseEvent event) {
        Core.getInjector().getInstance(BotUI.class).switchState(BotUI.ViewState.LOGIN);
    }

    @FXML
    private void refresh(MouseEvent event) {
        registerWebView.getEngine().load(Configuration.REGISTRATION_PAGE);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final WebEngine engine = registerWebView.getEngine();
        engine.load(Configuration.REGISTRATION_PAGE);
        engine.setUserAgent(Configuration.USER_AGENT);
        engine.getLoadWorker().stateProperty().addListener(
                (ov, oldState, newState) -> {
                    if (engine.getLocation().contains("crossLogin")) {
                        engine.load(null);
                        Core.getInjector().getInstance(BotUI.class).switchState(BotUI.ViewState.REGISTER_SUCCESS);
                    }
                });
    }

}
