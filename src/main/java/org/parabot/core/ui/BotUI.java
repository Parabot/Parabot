package org.parabot.core.ui;

import org.parabot.core.Configuration;
import org.parabot.core.Context;
import org.parabot.core.Directories;
import org.parabot.core.ui.components.GamePanel;
import org.parabot.core.ui.components.VerboseLoader;
import org.parabot.core.ui.components.notifications.NotificationUI;
import org.parabot.core.ui.utils.SwingUtil;
import org.parabot.environment.OperatingSystem;
import org.parabot.environment.api.utils.StringUtils;
import org.parabot.environment.randoms.Random;
import org.parabot.environment.scripts.Script;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The bot user interface
 *
 * @author Dane, Everel, JKetelaar, Ethan
 */
public class BotUI extends JFrame implements ActionListener, ComponentListener, WindowListener {

    private static final long serialVersionUID = -2126184292879805519L;
    private static BotUI instance;
    private static JDialog dialog;
    private BotMenuBar menuBar;
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
        this.menuBar = new BotMenuBar(this);
        setJMenuBar(menuBar);
        setLayout(new BorderLayout());
        addComponentListener(this);
        addWindowListener(this);

        add(GamePanel.getInstance());
        GamePanel.getInstance().add(VerboseLoader.get(username, password), BorderLayout.CENTER);
        add(Logger.getInstance(), BorderLayout.SOUTH);

        SwingUtil.setParabotIcons(this);

        pack();
        revalidate();
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
                    int menuBarHeight = menuBar.getHeight() + getFile().getHeight();
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
                    setRunning();
                    setScriptState(Script.STATE_RUNNING);
                    break;
                }
                new ScriptSelector().setVisible(true);
                break;
            case "Pause":
                setScriptState(Script.STATE_PAUSE);
                setPaused();
                pauseScript = true;
                break;
            case "Stop":
                if (pauseScript) {
                    pauseScript = false;
                    setStopped();
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

    protected void setDialog(JDialog dialog) {
        BotUI.dialog = dialog;
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
            setRunning();
        } else {
            setStopped();
        }
    }

    private void setPaused() {
        setPausedButtons(false);
        setRunButtons(true);
        setStopButtons(true);
    }

    private void setRunning() {
        setRunButtons(false);
        setPausedButtons(true);
        setStopButtons(true);
    }

    private void setStopped() {
        setStopButtons(false);
        setPausedButtons(false);
        setRunButtons(true);
    }

    private void setPausedButtons(boolean enabled) {
        menuBar.getPause().setEnabled(enabled);
        menuBar.getPauseButton().setEnabled(enabled);
    }

    private void setStopButtons(boolean enabled) {
        menuBar.getStop().setEnabled(enabled);
        menuBar.getStopButton().setEnabled(enabled);
    }

    private void setRunButtons(boolean enabled) {
        menuBar.getRun().setEnabled(enabled);
        menuBar.getStartButton().setEnabled(enabled);
    }

    private void setScriptState(int state) {
        if (Context.getInstance().getRunningScript() != null) {
            Context.getInstance().getRunningScript().setState(state);
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
        return menuBar.getFeatures();
    }

    public JMenu getScripts() {
        return null;
    }

    public JMenu getFile() {
        return menuBar.getFile();
    }

    public JMenuItem getCacheClear() {
        return menuBar.getCacheClear();
    }

    public JMenuItem getNotifications() {
        return menuBar.getNotifications();
    }
}
