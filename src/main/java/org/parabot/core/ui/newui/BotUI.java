package org.parabot.core.ui.newui;

import com.google.inject.Singleton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.parabot.core.Core;

import java.io.IOException;

/**
 * @author Fryslan, JKetelaar
 */
@Singleton
public class BotUI extends Application {

    public void start() {
        BotUI botUI = Core.getInjector().getInstance(BotUI.class);
        Application.launch(botUI.getClass());
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image("/storage/images/icon.png"));
        switchState(ViewState.LOGIN, stage);
    }

    public void switchState(ViewState viewState, Stage stage) {
        Core.verbose("Switching state to " + viewState.name());

        try {
            Parent root = FXMLLoader.load(BotUI.class.getResource(viewState.getFile()));

            Scene scene = new Scene(root);
            stage.setScene(scene);

            if (viewState.getWidth() != null) {
                stage.setWidth(692);
            }

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum ViewState {
        DEBUG("/storage/ui/debugs.fxml", null),
        GAME("/storage/ui/game.fxml", 692),
        SERVER_SELECTOR("/storage/ui/server_selector.fxml", null),
        LOGIN("/storage/ui/login.fxml", null);

        private String  file;
        private Integer width;

        ViewState(String file, Integer width) {
            this.file = file;
            this.width = width;
        }

        public Integer getWidth() {
            return width;
        }

        public String getFile() {
            return file;
        }
    }
}
