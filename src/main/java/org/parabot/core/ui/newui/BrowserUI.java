package org.parabot.core.ui.newui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.parabot.core.ui.newui.controllers.BrowserUIController;

import java.io.IOException;

/**
 * Created by Fryslan.
 */
public class BrowserUI {
    private String url;

    public BrowserUI(String url){
        this.url = url;
    }

    public void initialize(){
        Parent root;
        BrowserUIController.setUrl(url);
        try {
            root = FXMLLoader.load(BotUI.class.getResource("/storage/ui/browser.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BrowserUI("http://parabot.org/").initialize();
    }
}
