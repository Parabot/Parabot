package org.parabot.core.ui.utils;

import javafx.scene.control.Alert;
import org.parabot.core.ui.newui.components.DialogHelper;

import javax.swing.*;

/**
 * Log messages to the log user interface which is attached to the bot user interface
 *
 * @author Everel, JKetelaar
 * @deprecated Use DialogHelper instead
 */
public class UILog {

    public static void log(final String title, final String message) {
        log(title, message, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void log(final String title, final String message,
                           int messageType) {
        Alert.AlertType type;
        switch (messageType) {
            case JOptionPane.ERROR_MESSAGE:
                type = Alert.AlertType.ERROR;
                break;
            case JOptionPane.WARNING_MESSAGE:
                type = Alert.AlertType.WARNING;
                break;
            case JOptionPane.INFORMATION_MESSAGE:
            default:
                type = Alert.AlertType.INFORMATION;
                break;
        }

        DialogHelper.showMessage("Parabot", title, message, type);
    }

}
