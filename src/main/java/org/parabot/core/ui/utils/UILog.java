package org.parabot.core.ui.utils;

import javax.swing.JOptionPane;

/**
 * Log messages to the log user interface which is attached to the bot user interface
 *
 * @author Everel
 */
public class UILog {

    public static void log(final String title, final String message) {
        log(title, message, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void log(final String title, final String message,
                           int messageType) {
        JOptionPane.showMessageDialog(null, message, title,
                messageType);
    }

    public static int alert(final String title, final String message) {
        return alert(title, message, JOptionPane.YES_NO_OPTION);
    }

    public static int alert(final String title, final String message, int option) {
        return alert(title, message, option, JOptionPane.DEFAULT_OPTION);
    }

    public static int alert(final String title, final String message, int optionType, int messageType) {
        return JOptionPane.showConfirmDialog(null, message, title, optionType, messageType);
    }

    public static int alert(final String title, final String message, Object[] options) {
        return alert(title, message, options, JOptionPane.INFORMATION_MESSAGE);
    }

    public static int alert(final String title, final String message, Object[] options, int messageType) {
        return JOptionPane.showOptionDialog(null, message, title,
                JOptionPane.YES_NO_CANCEL_OPTION, messageType, null, options, null);
    }

    public static int alert(final String title, final String message, Object[] options, int initialValue, int messageType) {
        return JOptionPane.showOptionDialog(null, message, title,
                JOptionPane.YES_NO_CANCEL_OPTION, messageType, null, options, initialValue);
    }
}
