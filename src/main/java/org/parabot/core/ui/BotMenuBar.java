package org.parabot.core.ui;

import org.parabot.core.ui.images.Images;
import org.parabot.environment.OperatingSystem;

import javax.swing.*;

/**
 * @author Ethan
 */

public class BotMenuBar extends JMenuBar {
    private BotUI botUI;
    private JButton startButton, pauseButton, stopButton;
    private JMenu features, file, scripts;
    private JMenuItem cacheClear, notifications, run, pause, stop;

    public BotMenuBar(BotUI botUI) {
        this.botUI = botUI;
        configure();
    }

    private final void configure() {
        createMenu();
        configureComponents();
    }

    private void createMenu() {

        file = new JMenu("File");
        features = new JMenu("Features");
        scripts = new JMenu("Script");

        JMenuItem screenshot = new JMenuItem("Create screenshot");
        JMenuItem proxy = new JMenuItem("Network");
        JMenuItem randoms = new JMenuItem("Randoms");
        JMenuItem dialog = new JCheckBoxMenuItem("Disable dialog");
        JMenuItem logger = new JCheckBoxMenuItem("Logger");

        if (!OperatingSystem.getOS().equals(OperatingSystem.WINDOWS)) {
            dialog.setSelected(true);
        }

        JMenuItem explorer = new JMenuItem("Reflection explorer");
        JMenuItem exit = new JMenuItem("Exit");


        run = createNewJMenuItem(new ImageIcon(Images.getResource("/storage/images/run.png")), "Run", false);

        pause = createNewJMenuItem(new ImageIcon(Images.getResource("/storage/images/pause.png")), "Pause", false);

        stop = createNewJMenuItem(new ImageIcon(Images.getResource("/storage/images/stop.png")), "Stop", false);

        cacheClear = new JMenuItem("Clear cache");
        cacheClear.setIcon(new ImageIcon(Images.getResource("/storage/images/trash.png")));

        notifications = new JMenuItem("Notifications");
        notifications.setIcon(new ImageIcon(Images.getResource("/storage/images/bell.png")));


        screenshot.addActionListener(botUI);
        proxy.addActionListener(botUI);
        randoms.addActionListener(botUI);
        dialog.addActionListener(botUI);
        logger.addActionListener(botUI);
        explorer.addActionListener(botUI);
        exit.addActionListener(botUI);
        cacheClear.addActionListener(botUI);
        notifications.addActionListener(botUI);

        scripts.add(run);
        scripts.add(pause);
        scripts.add(stop);

        file.add(screenshot);
        file.add(proxy);
        file.add(randoms);
        file.add(dialog);
        file.add(logger);
        file.add(explorer);
        file.add(exit);

        features.add(cacheClear);
        features.add(notifications);


        startButton = createNewButton(new ImageIcon(Images.getResource("/storage/images/run_button.png")), "Run Script", "Run", false);

        pauseButton = createNewButton(new ImageIcon(Images.getResource("/storage/images/pause_button.png")), "Pause Script", "Pause", false);

        stopButton = createNewButton(new ImageIcon(Images.getResource("/storage/images/stop_button.png")), "Stop Script", "Stop", false);

    }

    private JMenuItem createNewJMenuItem(ImageIcon icon, String name, boolean enabled) {
        final JMenuItem tempItem = new JMenuItem(name);
        tempItem.setIcon(icon);
        tempItem.setEnabled(enabled);
        tempItem.addActionListener(botUI);
        return tempItem;
    }

    private JButton createNewButton(ImageIcon icon, String tooltip, String action, boolean enabled) {
        final JButton tempButton = new JButton();
        tempButton.setIcon(icon);
        tempButton.setContentAreaFilled(false);
        tempButton.setRolloverEnabled(true);
        tempButton.setToolTipText(tooltip);
        tempButton.addActionListener(botUI);
        tempButton.setActionCommand(action);
        tempButton.setEnabled(enabled);
        return tempButton;
    }

    private void configureComponents() {
        removeAll();
        add(file);
        add(features);
        add(scripts);
        add(Box.createHorizontalGlue());
        add(startButton);
        add(pauseButton);
        add(stopButton);

    }


    public JMenu getFeatures() {
        return features;
    }

    public JMenu getFile() {
        return file;
    }

    public JMenuItem getCacheClear() {
        return cacheClear;
    }

    public JMenuItem getNotifications() {
        return notifications;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getPauseButton() {
        return pauseButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public JMenu getScripts() {
        return scripts;
    }

    public JMenuItem getRun() {
        return run;
    }

    public JMenuItem getPause() {
        return pause;
    }

    public JMenuItem getStop() {
        return stop;
    }
}
