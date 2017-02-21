package org.parabot.core.ui.components.notifications;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.parabot.core.settings.Configuration;

public class NotificationUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //noinspection RedundantCast
        BorderPane root = (BorderPane) FXMLLoader.load(this.getClass().getResource("/storage/ui/notifications.fxml"));
        stage.setTitle(Configuration.BOT_TITLE);
        stage.setScene(new Scene(root));
        stage.show();
    }
}