package org.parabot.core;

import org.json.simple.parser.JSONParser;
import org.parabot.api.translations.TranslationHelper;
import org.parabot.core.asm.ASMClassLoader;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.desc.ServerProviderInfo;
import org.parabot.core.paint.PaintDebugger;
import org.parabot.core.parsers.hooks.HookParser;
import org.parabot.core.ui.BotDialog;
import org.parabot.core.ui.BotUI;
import org.parabot.core.ui.components.GamePanel;
import org.parabot.core.ui.listeners.PBKeyListener;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.randoms.RandomHandler;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.servers.ServerProvider;

import java.applet.Applet;
import java.awt.Dimension;
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
public class Context {
    public static final HashMap<ThreadGroup, Context> threadGroups = new HashMap<>();

    private static final ArrayList<Paintable> paintables = new ArrayList<>();
    private static Context instance;
    private static String username;

    private final ASMClassLoader classLoader;
    private final ClassPath classPath;
    private final ServerProvider serverProvider;
    private final RandomHandler randomHandler;
    private final PaintDebugger paintDebugger;
    private final JSONParser jsonParser;
    private final PrintStream defaultOut;
    private final PrintStream defaultErr;
    private Applet gameApplet;
    private HookParser hookParser;
    private Script runningScript;
    private Object clientInstance;
    private Mouse mouse;
    private Keyboard keyboard;
    private PBKeyListener pbKeyListener;
    private ServerProviderInfo providerInfo;

    private Context(final ServerProvider serverProvider) {
        threadGroups.put(Thread.currentThread().getThreadGroup(), this);

        System.setProperty("sun.java.command", "");

        this.serverProvider = serverProvider;
        this.paintDebugger = new PaintDebugger();
        this.classPath = new ClassPath();
        this.classLoader = new ASMClassLoader(classPath);
        this.randomHandler = new RandomHandler();
        this.jsonParser = new JSONParser();
        this.defaultOut = System.out;
        this.defaultErr = System.err;
    }

    public static double getJavaVersion() {
        String version = System.getProperty("java.version");
        int pos = version.indexOf('.');
        pos = version.indexOf('.', pos + 1);
        return Double.parseDouble(version.substring(0, pos));
    }

    /**
     * Returns the instance of this class, based on a given ServerProvider
     *
     * @param serverProvider
     *
     * @return
     */
    public static Context getInstance(ServerProvider serverProvider) {
        return instance == null ? instance = new Context(serverProvider) : instance;
    }

    /**
     * Returns the instance of this class
     *
     * @return
     */
    public static Context getInstance() {
        return getInstance(null);
    }

    /**
     * Returns the username of the current logged in user to Parabot
     *
     * @return
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Sets the username for the logged in user to Parabot
     *
     * @param username
     */
    public static void setUsername(String username) {
        Context.username = username;
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

        final GamePanel panel = GamePanel.getInstance();
        final Dimension appletSize = serverProvider.getGameDimensions();

        panel.setPreferredSize(appletSize);
        serverProvider.addMenuItems(BotUI.getInstance().getJMenuBar());
        BotUI.getInstance().pack();
        BotUI.getInstance().validate();

        panel.removeComponents();
        gameApplet.setSize(appletSize);
        panel.add(gameApplet);
        panel.validate();

        serverProvider.preAppletInit();

        gameApplet.init();
        gameApplet.start();

        serverProvider.postAppletStart();

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
        this.pbKeyListener = new PBKeyListener();
        applet.addKeyListener(this.pbKeyListener);

        BotDialog.getInstance().validate();
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

    /**
     * Sets provider info of this context
     *
     * @param providerInfo
     */
    public void setProviderInfo(ServerProviderInfo providerInfo) {
        this.providerInfo = providerInfo;
    }

    /**
     * Gets ServerProvider info
     * Can be null if this is not a public server provider
     *
     * @return info about this provider
     */
    public ServerProviderInfo getServerProviderInfo() {
        return this.providerInfo;
    }

    /**
     * Gets class loader of server from this context
     *
     * @return class loader
     */
    public ASMClassLoader getASMClassLoader() {
        return classLoader;
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
     * The client debug painter
     *
     * @return debug painter
     */
    public PaintDebugger getPaintDebugger() {
        return paintDebugger;
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
     * Gets the hook parser, may be null if injection is not used or a custom hook parser is used for injecting
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

    /**
     * Gets the random handler
     *
     * @return random handler
     */
    public RandomHandler getRandomHandler() {
        return this.randomHandler;
    }

    /**
     * Returns the JSON Parser instance
     *
     * @return
     */
    public JSONParser getJsonParser() {
        return jsonParser;
    }

    /**
     * Returns the PBKeyListener instance
     *
     * @return
     */
    public PBKeyListener getPbKeyListener() {
        return pbKeyListener;
    }
}
