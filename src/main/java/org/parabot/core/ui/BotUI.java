package org.parabot.core.ui;

import org.parabot.core.Configuration;
import org.parabot.core.Context;
import org.parabot.core.Directories;
import org.parabot.core.ui.components.GamePanel;
import org.parabot.core.ui.components.VerboseLoader;
import org.parabot.core.ui.components.notifications.NotificationUI;
import org.parabot.core.ui.images.Images;
import org.parabot.core.ui.utils.SwingUtil;
import org.parabot.environment.OperatingSystem;
import org.parabot.environment.api.utils.StringUtils;
import org.parabot.environment.randoms.Random;
import org.parabot.environment.scripts.Script;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.WindowConstants;

/**
 * The bot user interface
 *
 * @author Dane, Everel, JKetelaar
 */
public class BotUI extends JFrame implements ActionListener, ComponentListener, WindowListener {

    private static final long serialVersionUID = -2126184292879805519L;
    private static BotUI instance;
    private static JDialog dialog;

    private JMenuBar menuBar;
    private JMenu features, scripts, file;
    private JMenuItem run, pause, stop, cacheClear, notifications;
    private boolean runScript, pauseScript;

    public BotUI(String username, String password) {
        if (instance != null) {
            throw new IllegalStateException("BotUI already created");
        }
        instance = this;
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        setTitle(Configuration.BOT_TITLE);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        createMenu();

        setLayout(new BorderLayout());
        addComponentListener(this);
        addWindowListener(this);

        add(GamePanel.getInstance());
        GamePanel.getInstance().add(VerboseLoader.get(username, password), BorderLayout.CENTER);
        add(Logger.getInstance(), BorderLayout.SOUTH);

        SwingUtil.setParabotIcons(this);

        pack();
        setLocationRelativeTo(null);
        BotDialog.getInstance(this);

        if (!OperatingSystem.getOS().equals(OperatingSystem.WINDOWS)) {
            BotDialog.getInstance().setVisible(false);
        }
    }

    public static BotUI getInstance() {
        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.performCommand(e.getActionCommand());
    }

    public void performCommand(String command) {
        switch (command) {
            case "Create screenshot":
                try {
                    Robot robot = new Robot();
                    int menuBarHeight = menuBar.getHeight() + file.getHeight();
                    Rectangle parabotScreen = new Rectangle(
                            (int) getLocation().getX(), (int) getLocation().getY() + menuBarHeight,
                            getWidth(), getHeight() - menuBarHeight);
                    BufferedImage image = robot.createScreenCapture(parabotScreen);
                    String randString = StringUtils.randomString(10);
                    boolean search = true;
                    boolean duplicate = false;
                    while (search) {
                        File[] files;
                        if ((files = Directories.getScreenshotDir().listFiles()) != null) {
                            for (File f : files) {
                                if (f.getAbsoluteFile().getName().contains(randString)) {
                                    duplicate = true;
                                    break;
                                }
                            }
                        }
                        if (!duplicate) {
                            search = false;
                        } else {
                            randString = StringUtils.randomString(10);
                            duplicate = false;
                        }
                    }
                    File file = new File(Directories.getScreenshotDir().getPath() + "/" + randString + ".png");
                    ImageIO.write(image, "png", file);

                } catch (IOException | AWTException k) {
                    k.printStackTrace();
                }
                break;
            case "Exit":
                System.exit(0);
                break;
            case "Network":
                NetworkUI.getInstance().setVisible(true);
                break;
            case "Randoms":
                ArrayList<String> randoms = new ArrayList<>();
                for (Random r : Context.getInstance().getRandomHandler().getRandoms()) {
                    randoms.add(r.getName());
                }
                RandomUI.getInstance().openFrame(randoms);
                break;
            case "Reflection explorer":
                new ReflectUI().setVisible(true);
                break;
            case "Run":
                if (pauseScript) {
                    pauseScript = false;
                    pause.setEnabled(true);
                    run.setEnabled(false);
                    setScriptState(Script.STATE_RUNNING);
                    break;
                }
                new ScriptSelector().setVisible(true);
                break;
            case "Pause":
                setScriptState(Script.STATE_PAUSE);
                pause.setEnabled(false);
                run.setEnabled(true);
                pauseScript = true;
                break;
            case "Stop":
                if (pauseScript) {
                    pauseScript = false;
                    pause.setEnabled(false);
                    run.setEnabled(true);
                    stop.setEnabled(false);
                }
                setScriptState(Script.STATE_STOPPED);
                break;
            case "Logger":
                Logger.getInstance().setVisible(!Logger.getInstance().isVisible());
                BotUI.getInstance().pack();
                BotUI.getInstance().revalidate();
                if (!Logger.getInstance().isClearable()) {
                    Logger.getInstance().setClearable();
                } else if (Logger.getInstance().isClearable() && !Logger.getInstance().isVisible()) {
                    Logger.clearLogger();
                    Logger.addMessage("Logger initialised", false);
                }
                break;
            case "Disable dialog":
                BotDialog.getInstance().setVisible(!dialog.isVisible());
                break;
            case "Clear cache":
                Directories.clearCache();
                break;
            case "Notifications":
                NotificationUI.create();
                break;
            default:
                System.out.println("Invalid command: " + command);
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        if (dialog == null || !isVisible()) {
            return;
        }
        Point gameLocation = GamePanel.getInstance().getLocationOnScreen();
        dialog.setLocation(gameLocation.x, gameLocation.y);
    }

    public void toggleRun() {
        runScript = !runScript;
        if (runScript) {
            scriptRunning();
        } else {
            scriptStopped();
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (isVisible()) {
            BotDialog.getInstance().setSize(getSize());
        }
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {

    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
        if (isVisible()) {
            BotDialog.getInstance().setVisible(false);
            BotDialog.getInstance().setVisible(true);
        }
    }

    @Override
    public void windowIconified(WindowEvent arg0) {

    }

    @Override
    public void windowOpened(WindowEvent arg0) {
    }

    public JMenu getFeatures() {
        return features;
    }

    public JMenu getScripts() {
        return scripts;
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

    protected void setDialog(JDialog dialog) {
        BotUI.dialog = dialog;
    }

    private void createMenu() {
        menuBar = new JMenuBar();

        file = new JMenu("File");
        scripts = new JMenu("Script");
        features = new JMenu("Features");

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

        run = new JMenuItem("Run");
        run.setIcon(new ImageIcon(Images.getResource("/storage/images/run.png")));

        pause = new JMenuItem("Pause");
        pause.setEnabled(false);
        pause.setIcon(new ImageIcon(Images.getResource("/storage/images/pause.png")));

        stop = new JMenuItem("Stop");
        stop.setEnabled(false);
        stop.setIcon(new ImageIcon(Images.getResource("/storage/images/stop.png")));

        cacheClear = new JMenuItem("Clear cache");
        cacheClear.setIcon(new ImageIcon(Images.getResource("/storage/images/trash.png")));

        notifications = new JMenuItem("Notifications");
        notifications.setIcon(new ImageIcon(Images.getResource("/storage/images/bell.png")));

        screenshot.addActionListener(this);
        proxy.addActionListener(this);
        randoms.addActionListener(this);
        dialog.addActionListener(this);
        logger.addActionListener(this);
        explorer.addActionListener(this);
        exit.addActionListener(this);
        cacheClear.addActionListener(this);
        notifications.addActionListener(this);

        run.addActionListener(this);
        pause.addActionListener(this);
        stop.addActionListener(this);

        file.add(screenshot);
        file.add(proxy);
        file.add(randoms);
        file.add(dialog);
        file.add(logger);
        file.add(explorer);
        file.add(exit);

        scripts.add(run);
        scripts.add(pause);
        scripts.add(stop);

        features.add(cacheClear);
        features.add(notifications);

        menuBar.add(file);
        menuBar.add(scripts);
        menuBar.add(features);

        setJMenuBar(menuBar);
    }

    private void scriptRunning() {
        run.setEnabled(false);
        pause.setEnabled(true);
        stop.setEnabled(true);
    }

    private void scriptStopped() {
        run.setEnabled(true);
        pause.setEnabled(false);
        stop.setEnabled(false);
    }

    private void setScriptState(int state) {
        if (Context.getInstance().getRunningScript() != null) {
            Context.getInstance().getRunningScript().setState(state);
        }
    }
}
