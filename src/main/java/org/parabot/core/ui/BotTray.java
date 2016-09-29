package org.parabot.core.ui;


import org.parabot.core.Context;
import org.parabot.core.ui.images.Images;
import org.parabot.environment.scripts.Script;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * Created by Eric on 5/20/2016.
 */
public class BotTray {
    public BotTray() {
        if (!SystemTray.isSupported()) {
            System.out.println("Tray is not supported.");
            return;
        }


        final TrayIcon trayIcon = new TrayIcon(Images.getResource("/storage/images/icon.png"));
        trayIcon.setImageAutoSize(true);
        final SystemTray tray = SystemTray.getSystemTray();
        final PopupMenu pop = new PopupMenu();


        Menu scripts = new Menu("Scripts");

        MenuItem run = new MenuItem("Run");
        MenuItem pause = new MenuItem("Pause");
        MenuItem stop = new MenuItem("Stop");
        MenuItem exit = new MenuItem("Exit");

        pop.add(scripts);
        scripts.add(run);
        scripts.add(pause);
        scripts.add(stop);
        pop.add(exit);


        run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Context.getInstance().getRunningScript() != null && Context.getInstance().getRunningScript().getState() == Script.STATE_PAUSE) {
                    Context.getInstance().getRunningScript().setState(Script.STATE_RUNNING);
                } else {
                    new ScriptSelector().setVisible(true);
                }
            }
        });

        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Context.getInstance().getRunningScript().getState() == Script.STATE_RUNNING) {
                    Context.getInstance().getRunningScript().setState(Script.STATE_PAUSE);
                }
            }
        });

        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Context.getInstance().getRunningScript().getState() == Script.STATE_RUNNING ||
                        Context.getInstance().getRunningScript().getState() == Script.STATE_PAUSE) {
                    Context.getInstance().getRunningScript().setState(Script.STATE_STOPPED);
                }
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BotUI.getInstance().dispatchEvent(new WindowEvent(BotUI.getInstance(), WindowEvent.WINDOW_CLOSING));
            }
        });

        trayIcon.setToolTip("Parabot");

        trayIcon.setPopupMenu(pop);


        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
