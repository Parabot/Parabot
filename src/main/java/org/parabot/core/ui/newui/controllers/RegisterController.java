package org.parabot.core.ui.newui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.parabot.core.Core;
import org.parabot.core.settings.Configuration;
import org.parabot.core.ui.newui.BotUI;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Fryslan.
 */
public class RegisterController implements Initializable {
    @FXML
    private ImageView refreshIcon;

    @FXML
    private ImageView returnIcon;

    @FXML
    private WebView registerWebView;

    @FXML
    private void returnToLogin(MouseEvent event) {
        Stage stage = (Stage) returnIcon.getScene().getWindow();
        Core.getInjector().getInstance(BotUI.class).switchState(BotUI.ViewState.LOGIN, stage);
    }

    @FXML
    private void refresh(MouseEvent event) {
        registerWebView.getEngine().load(Configuration.REGISTRATION_PAGE);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final WebEngine engine = registerWebView.getEngine();
        engine.load(Configuration.REGISTRATION_PAGE);
        engine.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/601.6.17 (KHTML, like Gecko) Version/9.1.1 Safari/601.6.17");
        engine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (engine.getLocation().endsWith("https://www.parabot.org/community//applications/core/interface/ipsconnect/ipsconnect.php&returnTo=https://www.parabot.org/community/")) {
                            Stage stage = (Stage) registerWebView.getScene().getWindow();
                            Core.getInjector().getInstance(BotUI.class).switchState(BotUI.ViewState.REGISTER_SUCCES, stage);
                        }
                    }
                });
    }

}
