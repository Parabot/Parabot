package org.parabot.core.ui.components.notifications;

import org.parabot.api.notifications.NotificationManager;
import org.parabot.api.notifications.types.NotificationType;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * @author JKetelaar
 */
public class NotificationUIController implements Initializable {

    @FXML // fx:id="availableTypes"
    private ListView availableTypes;

    @FXML // fx:id="enabledTypes"
    private ListView enabledTypes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.refreshTypes();
    }

    private void refreshTypes() {
        availableTypes.getItems().clear();
        for (NotificationType notificationType : NotificationManager.getContext().getAvailableNotificationTypes()) {
            availableTypes.getItems().add(notificationType.getName());
        }

        enabledTypes.getItems().clear();
        for (NotificationType notificationType : NotificationManager.getContext().getEnabledTypes()) {
            enabledTypes.getItems().add(notificationType.getName());
        }
    }

    @FXML
    private void enableType() {
        Object object = availableTypes.getSelectionModel().getSelectedItem();
        if (object != null) {
            String name = (String) object;

            NotificationType type = NotificationManager.getContext().getNotificationType(name);
            NotificationManager.getContext().enableNotificationType(type);

            this.refreshTypes();
        }
    }

    @FXML
    private void disableType() {
        Object object = enabledTypes.getSelectionModel().getSelectedItem();
        if (object != null) {
            String name = (String) object;

            NotificationType type = NotificationManager.getContext().getNotificationType(name);
            NotificationManager.getContext().disableNotificationType(type);

            this.refreshTypes();
        }
    }

    @FXML
    private void save() {
        Stage stage = (Stage) this.enabledTypes.getScene().getWindow();
        stage.close();
    }
}
