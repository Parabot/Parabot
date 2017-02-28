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

    private boolean             closed;
    private BrowserUIController controller;
    private Stage               stage;

    public BrowserUI() {
        this.initialize();
    }

    public static BrowserUI getBrowser() {
        BrowserUI browserUI = Core.getInjector().getInstance(BrowserUI.class);
        if (browserUI.isClosed()) {
            browserUI.initialize();
        }
        return browserUI;
    }

    private void initialize() {
        FXMLLoader loader;
        Parent     root;
        try {
            loader = new FXMLLoader(getClass().getResource("/storage/ui/browser.fxml"));
            root = loader.load();

            controller = loader.getController();

            stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            stage.toFront();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hide() {
        stage.hide();
        closed = true;
    }

    public BrowserUIController getController() {
        return controller;
    }

    public void loadPage(String url) {
        controller.loadPage(url);
    }

    public Stage getStage() {
        return stage;
    }

    private boolean isClosed() {
        return closed || controller.isClosed();
    }
}
