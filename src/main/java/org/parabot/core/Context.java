package org.parabot.core;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.json.simple.parser.JSONParser;
import org.parabot.api.io.Directories;
import org.parabot.api.translations.TranslationHelper;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.parsers.hooks.HookParser;
import org.parabot.core.ui.BotDialog;
import org.parabot.core.ui.BotUI;
import org.parabot.core.ui.components.GamePanel;
import org.parabot.core.ui.listeners.PBKeyListener;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.servers.ServerProvider;

import java.applet.Applet;
import java.awt.*;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

/**
 * Game context
 *
 * @author Everel, JKetelaar, Matt
 */
@Singleton
public class Context {
    public static final HashMap<ThreadGroup, Context> threadGroups = new HashMap<>();
    private static      ArrayList<Paintable>          paintables   = new ArrayList<>();

    @Inject
    private ClassPath      classPath;
    private ServerProvider serverProvider;
    private Applet         gameApplet;
    private HookParser     hookParser;
    private Script         runningScript;

    private Object   clientInstance;
    private Mouse    mouse;
    private Keyboard keyboard;

    @Inject
    private JSONParser jsonParser;

    private PrintStream defaultOut = System.out;
    private PrintStream defaultErr = System.err;

    public Context() {
        this.defaultOut = System.out;
        this.defaultErr = System.err;

        System.setProperty("sun.java.command", "");
    }

    /**
     * Sets the main client instance
     */
    public void setClientInstance(Object object) {
        this.clientInstance = object;
    }

    /**
     * Gets the mouse
     *
     * @return mouse
     */
    public Mouse getMouse() {
        return mouse;
    }

    /**
     * Sets the mouse
     *
     * @param mouse
     */
    public void setMouse(final Mouse mouse) {
        this.mouse = mouse;
    }

    /**
     * Gets the keyboard
     *
     * @return keyboard
     */
    public Keyboard getKeyboard() {
        return keyboard;
    }

    /**
     * Sets the keyboard
     *
     * @param keyboard
     */
    public void setKeyboard(final Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    /**
     * ClassPath
     *
     * @return classpath
     */
    public ClassPath getClassPath() {
        return classPath;
    }

    /**
     * Determines if applet has been set
     *
     * @return <b>true</b> if set
     */
    public boolean appletSet() {
        return gameApplet != null;
    }

    /**
     * Gets game applet
     *
     * @return applet
     */
    public Applet getApplet() {
        return gameApplet;
    }

    /**
     * Sets the bot target applet
     *
     * @param applet
     */
    public void setApplet(final Applet applet) {
        gameApplet = applet;

        if (getClient() == null) {
            setClientInstance(gameApplet);
        }

        Core.verbose(TranslationHelper.translate("APPLET_FETCHED"));

        final GamePanel panel      = Core.getInjector().getInstance(GamePanel.class);
        final Dimension appletSize = serverProvider.getGameDimensions();

        panel.setPreferredSize(appletSize);
        serverProvider.addMenuItems(Core.getInjector().getInstance(BotUI.class).getJMenuBar());
        Core.getInjector().getInstance(BotUI.class).pack();
        Core.getInjector().getInstance(BotUI.class).validate();

        panel.removeComponents();
        gameApplet.setSize(appletSize);
        panel.add(gameApplet);
        panel.validate();

        gameApplet.init();
        gameApplet.start();

        java.util.Timer t = new java.util.Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                gameApplet.setBounds(0, 0, appletSize.width, appletSize.height);
            }
        }, 1000);

        Core.verbose(TranslationHelper.translate("INIT_MOUSE"));
        serverProvider.initMouse();
        Core.verbose(TranslationHelper.translate("DONE"));
        Core.verbose(TranslationHelper.translate("INIT_KEYBOARD"));
        serverProvider.initKeyboard();
        Core.verbose(TranslationHelper.translate("DONE"));

        Core.verbose(TranslationHelper.translate("INIT_KEY_LISTENER"));

        applet.addKeyListener(Core.getInjector().getInstance(PBKeyListener.class));

        Core.getInjector().getInstance(BotDialog.class).validate();
        System.setOut(this.defaultOut);
        System.setErr(this.defaultErr);
    }

    /**
     * Loads the game
     */
    public void load() {
        Core.verbose(TranslationHelper.translate("PARSING_SERVER_JAR"));
        serverProvider.init();
        serverProvider.parseJar();
        Core.verbose(TranslationHelper.translate("DONE"));
        Core.verbose(TranslationHelper.translate("INJECTING_HOOKS"));
        serverProvider.injectHooks();
        Core.verbose(TranslationHelper.translate("DONE"));
        Core.verbose(TranslationHelper.translate("FETCHING_GAME_APPLET"));
        if (Core.shouldDump()) {
            Core.verbose(TranslationHelper.translate("DUMPING_INJECTED_CLIENT"));
            classPath.dump(new File(Directories.getWorkspace(), "dump.jar"));
            Core.verbose(TranslationHelper.translate("DONE"));
        }
        Applet applet = serverProvider.fetchApplet();
        // If applet is null the server provider will call setApplet itself
        if (applet != null) {
            setApplet(applet);
        }
    }

    /**
     * Gets the server prodiver belonging to this context
     *
     * @return server provider
     */
    public ServerProvider getServerProvider() {
        return serverProvider;
    }

    public void setServerProvider(ServerProvider serverProvider) {
        threadGroups.put(Thread.currentThread().getThreadGroup(), this);

        this.serverProvider = serverProvider;
    }

    /**
     * Adds a paintable instance to the paintables
     *
     * @param paintable
     */
    public void addPaintable(Paintable paintable) {
        paintables.add(paintable);
    }

    /**
     * Removes a paintable instance from the paintables
     *
     * @param paintable
     */
    public void removePaintable(Paintable paintable) {
        paintables.remove(paintable);
    }

    /**
     * Gets the paintable instances
     *
     * @return array of paintable instances
     */
    public Paintable[] getPaintables() {
        return paintables.toArray(new Paintable[paintables.size()]);
    }

    /**
     * Gets the main/client instance
     *
     * @return instance of the the client
     */
    public Object getClient() {
        return this.clientInstance;
    }

    /**
     * Gets the hook parser, may be null if injection is not used or a server hook parser is used for injecting
     *
     * @return hook parser
     */
    public HookParser getHookParser() {
        return hookParser;
    }

    /**
     * Sets the hook parser
     *
     * @param hookParser
     */
    public void setHookParser(final HookParser hookParser) {
        this.hookParser = hookParser;
    }

    /**
     * Gets the current running script
     *
     * @return script
     */
    public Script getRunningScript() {
        return this.runningScript;
    }

    /**
     * Sets the current running script, if a script stops it will call this method with a null argument
     *
     * @param script
     */
    public void setRunningScript(final Script script) {
        this.runningScript = script;
    }

    public JSONParser getJsonParser() {
        return jsonParser;
    }
}
