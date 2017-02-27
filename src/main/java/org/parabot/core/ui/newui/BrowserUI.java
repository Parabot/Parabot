package org.parabot.core.ui.newui;

import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.parabot.core.Core;
import org.parabot.core.ui.newui.controllers.BrowserUIController;

import java.io.IOException;

/**
 * @author Fryslan, JKetelaar
 */
@Singleton
public class BrowserUI {

    private BrowserUIController controller;

    public BrowserUI() {
        this.initialize();
    }

    public static BrowserUI getBrowser() {
        return Core.getInjector().getInstance(BrowserUI.class);
    }

    private void initialize() {
        FXMLLoader loader;
        Parent     root;
        try {
            loader = new FXMLLoader(getClass().getResource("/storage/ui/browser.fxml"));
            root = loader.load();
            controller = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BrowserUIController getController() {
        return controller;
    }

    public void loadPage(String url) {
        controller.loadPage(url);
    }
}
