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
 * Created by Fryslan.
 */

public class BrowserUIController implements Initializable{

    @FXML
    private WebView webView;

    @FXML
    private AnchorPane pane;

    @FXML
    private ImageView refreshIcon;

    @FXML
    void refresh(MouseEvent event) {
        WebEngine engine = webView.getEngine();
        engine.load(engine.getLocation());
    }

    public static String url = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(url != null){
            WebEngine engine = webView.getEngine();
            engine.load(url);
        }
    }
}
