package org.parabot.core.ui;

import org.parabot.core.Context;
import org.parabot.core.ui.components.GamePanel;
import org.parabot.core.ui.components.VerboseLoader;
import org.parabot.core.ui.images.Images;
import org.parabot.core.ui.utils.SwingUtil;
import org.parabot.environment.OperatingSystem;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.randoms.Random;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

/**
 * The bot user interface
 *
 * @author Dane, Everel, Paradox
 */
public class BotUI extends JFrame implements ActionListener, ComponentListener, WindowListener {

    private static final long serialVersionUID = -2126184292879805519L;
    private static BotUI instance;
    private static JDialog dialog;

    private JMenuItem run, pause, stop, cacheClear;
    private boolean runScript, pauseScript;

    public BotUI(String username, String password) {
        if (instance != null) {
            throw new IllegalStateException("BotUI already created");
        }
        instance = this;
        //WebLookAndFeel.install();
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        setTitle("Parabot");
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

    public static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
        }
        return (directory.delete());
    }

    public static void createDirectory(File directory) {
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu scripts = new JMenu("Script");
        JMenu features = new JMenu("Features");

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

        screenshot.addActionListener(this);
        proxy.addActionListener(this);
        randoms.addActionListener(this);
        dialog.addActionListener(this);
        logger.addActionListener(this);
        explorer.addActionListener(this);
        exit.addActionListener(this);
        cacheClear.addActionListener(this);

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

        menuBar.add(file);
        menuBar.add(scripts);
        menuBar.add(features);


        setJMenuBar(menuBar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Create screenshot":
                JOptionPane.showMessageDialog(this, "We are still working on this...");
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
                    Logger.addMessage("Logger started", false);
                }
                break;
            case "Disable dialog":
                BotDialog.getInstance().setVisible(!dialog.isVisible());
                break;
            case "Clear cache":
                    deleteDirectory(new File("C:/Users/Eric/Documents/Parabot/cache"));
                    createDirectory(new File("C:/Users/Eric/Documents/Parabot/cache"));
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
            scriptRunning();
        } else {
            scriptStopped();
        }
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

}
