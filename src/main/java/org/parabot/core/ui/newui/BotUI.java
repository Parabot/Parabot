package org.parabot.core.ui.newui;

import com.google.inject.Singleton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.parabot.core.Core;

import java.io.IOException;

/**
 * @author Fryslan, JKetelaar
 */
@Singleton
public class BotUI extends Application {

    public static void main(String[] args) {
        BotUI botUI = Core.getInjector().getInstance(BotUI.class);
        Application.launch(botUI.getClass());
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.UNDECORATED);
        setLoginInterface(stage);
    }

    private void setLoginInterface(Stage stage) {
        try {
            Parent root = FXMLLoader.load(BotUI.class.getResource("/storage/ui/login.fxml"));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setServerSelectorInterface(Stage stage) {
        try {
            Parent root = FXMLLoader.load(BotUI.class.getResource("/storage/ui/server_selector.fxml"));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGameInterface(Stage stage) {
        try {
            Parent root = FXMLLoader.load(BotUI.class.getResource("/storage/ui/game.fxml"));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setWidth(692);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDebugsInterface(Stage stage) {
        try {
            Parent root = FXMLLoader.load(BotUI.class.getResource("/storage/ui/debugs.fxml"));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
