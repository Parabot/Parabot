package org.parabot.core.ui.newui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Fryslan
 */
public class BrowserUIController implements Initializable {

    @FXML
    private WebView webView;
    @FXML
    private AnchorPane pane;
    @FXML
    private ImageView refreshIcon;

    private static String url;

    public static void setUrl(String url) {
        BrowserUIController.url = url;
    }

    @FXML
    private void refresh(MouseEvent event) {
        WebEngine engine = webView.getEngine();
        engine.load(engine.getLocation());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(url != null){
            WebEngine engine = webView.getEngine();
            engine.load(url);
        }
    }

    public void loadPage(String url){
        WebEngine engine = getWebView().getEngine();
        engine.load(url);
    }

    public WebView getWebView() {
        return webView;
    }
}
