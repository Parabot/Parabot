package org.parabot.core.ui.components;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

/**
 * @author JKetelaar
 */
public class DialogHelper {
    public static String showTextInput(String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    public static void showWarning(String title, String header, String content) {
        showMessage(title, header, content, Alert.AlertType.WARNING);
    }

    public static void showError(String title, String header, String content) {
        showMessage(title, header, content, Alert.AlertType.ERROR);
    }

    public static void showConfirmation(String title, String header, String content) {
        showMessage(title, header, content, Alert.AlertType.CONFIRMATION);
    }

    public static void showInformation(String title, String header, String content) {
        showMessage(title, header, content, Alert.AlertType.INFORMATION);
    }

    public static void showMessage(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
