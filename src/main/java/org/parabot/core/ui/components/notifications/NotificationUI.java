package org.parabot.core.ui.components.notifications;

import org.parabot.api.output.Verboser;
import org.parabot.api.ui.JavaFxUtil;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A JavaFX Panel embedded into a Swing JFrame - handles notification settings
 *
 * @author Shadowrs
 */
public class NotificationUI extends JavaFxUtil {

    final NotificationUI n;

    private NotificationUI() {
        super("/storage/ui/notifications.fxml", NotificationUIController.class);
        this.n = this;
    }

    public static void create() {
        new NotificationUI();
    }

    @Override
    public WindowAdapter getWindowAdapter() {
        return new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                Verboser.verbose("NotificationUI closed " + e);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                // Anything here. JFrame hides on exit.
                Verboser.verbose("NotificationUI closing " + e);
                n.getFrame().dispose();
            }
        };
    }

    @Override
    protected void onLaunched() {
        n.getFrame().setTitle("Notifications");
    }
}