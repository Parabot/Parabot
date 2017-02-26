package org.parabot.core.ui.newui;

import javafx.fxml.FXMLLoader;
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
    private BrowserUIController controller;

    public BrowserUI(String url){
        this.url = url;
    }

    public void initialize(){
        FXMLLoader loader;
        Parent root;
        BrowserUIController.setUrl(url);
        try {
            loader = FXMLLoader.load(BotUI.class.getResource("/storage/ui/browser.fxml"));
            root = loader.load();
            controller = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BrowserUIController getController() {
        return controller;
    }

    public void loadPage(String url){
        if(getController() == null){
            System.out.println("You'll have to initialize te UI first.");
        }else{
            controller.loadPage(url);
        }
    }
}
