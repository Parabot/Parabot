package org.parabot.core;

import org.json.simple.parser.JSONParser;
import org.parabot.core.asm.ASMClassLoader;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.desc.ServerProviderInfo;
import org.parabot.core.paint.PaintDebugger;
import org.parabot.core.parsers.hooks.HookParser;
import org.parabot.core.ui.BotDialog;
import org.parabot.core.ui.BotUI;
import org.parabot.core.ui.components.GamePanel;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.randoms.RandomHandler;
import org.parabot.environment.scripts.uliratha.UlirathaClient;
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
public class Context {
    public static final HashMap<ThreadGroup, Context> threadGroups = new HashMap<ThreadGroup, Context>();
    private static ArrayList<Paintable> paintables = new ArrayList<Paintable>();
    
    private static Context instance;
    private static String username;

    private ASMClassLoader classLoader;
    private ClassPath classPath;
    private ServerProvider serverProvider;
    private Applet gameApplet;
    private HookParser hookParser;
    private Script runningScript;
    private RandomHandler randomHandler;
    private Object clientInstance;
    private PaintDebugger paintDebugger;
    private Mouse mouse;
    private Keyboard keyboard;
    private ServerProviderInfo providerInfo;
    private UlirathaClient ulirathaClient;
    private JSONParser jsonParser;

    private PrintStream defaultOut;
    private PrintStream defaultErr = System.err;

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

    public static Context getInstance(ServerProvider serverProvider) {
        return instance == null ? instance = new Context(serverProvider) : instance;
    }
    
    public static Context getInstance() {
    	return getInstance(null);
    }

    /**
     * Sets the main client instance
     */
    public void setClientInstance(Object object) {
        this.clientInstance = object;
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
     * Sets the mouse
     *
     * @param mouse
     */
    public void setMouse(final Mouse mouse) {
        this.mouse = mouse;
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
     * Sets the keyboard
     *
     * @param keyboard
     */
    public void setKeyboard(final Keyboard keyboard) {
        this.keyboard = keyboard;
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
     * Loads the game
     */
    public void load() {
        BotUI.getInstance().getJMenuBar().remove(2);
        Core.verbose("Parsing server jar...");
        serverProvider.init();
        serverProvider.parseJar();
        Core.verbose("Done.");
        Core.verbose("Injecting hooks...");
        serverProvider.injectHooks();
        Core.verbose("Done.");
        Core.verbose("Fetching game applet...");
        if(Core.shouldDump()) {
        	Core.verbose("Dumping injected client...");
        	classPath.dump(new File(Directories.getWorkspace(), "dump.jar"));
        	Core.verbose("Done.");
        }
        Applet applet = serverProvider.fetchApplet();
        // if applet is null the server provider will call setApplet itself
        if(applet != null) {
            setApplet(applet);
        }
    }
    
    /**
     * Sets the bot target applet
     * @param applet
     */
    public void setApplet(final Applet applet) {
    	gameApplet = applet;
    	
    	if (getClient() == null) {
            setClientInstance(gameApplet);
        }

        Core.verbose("Applet fetched.");
        
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

        gameApplet.init();
        gameApplet.start();

        java.util.Timer t = new java.util.Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                gameApplet.setBounds(0, 0, appletSize.width, appletSize.height);
            }
        }, 1000);
        
        Core.verbose("Initializing mouse...");
        serverProvider.initMouse();
        Core.verbose("Done.");
        Core.verbose("Initializing keyboard...");
        serverProvider.initKeyboard();
        Core.verbose("Done.");
        
        BotDialog.getInstance().validate();
        System.setOut(this.defaultOut);
        System.setErr(this.defaultErr);
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
     * 
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
     * Sets the current running script, if a script stops it will call this method with a null argument
     *
     * @param script
     */
    public void setRunningScript(final Script script) {
        this.runningScript = script;
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
     * Gets the random handler
     * @return random handler
     */
    public RandomHandler getRandomHandler() {
    	return this.randomHandler;
    }

    public static String getUsername() {
        return username;
    }

    public UlirathaClient getUlirathaClient() {
        return ulirathaClient;
    }

    public void setUlirathaClient(UlirathaClient ulirathaClient) {
        this.ulirathaClient = ulirathaClient;
    }

    public static void setUsername(String username) {
        Context.username = username;
    }

    public JSONParser getJsonParser() {
        return jsonParser;
    }
}
