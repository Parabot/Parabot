package org.parabot.core;

import org.parabot.core.asm.ASMClassLoader;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.paint.PaintDebugger;
import org.parabot.core.parsers.hooks.HookParser;
import org.parabot.core.ui.BotUI;
import org.parabot.core.ui.components.GamePanel;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.randoms.RandomHandler;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.servers.ServerProvider;

import java.applet.Applet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

/**
 * Game context
 *
 * @author Everel
 */
public class Context {
    public static final HashMap<ThreadGroup, Context> threadGroups = new HashMap<ThreadGroup, Context>();
    private static ArrayList<Paintable> paintables = new ArrayList<Paintable>();
    
    private static Context instance;

    public boolean added;
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

    private Context(final ServerProvider serverProvider) {
        threadGroups.put(Thread.currentThread().getThreadGroup(), this);
      
        this.serverProvider = serverProvider;
        this.paintDebugger = new PaintDebugger();
        this.classPath = new ClassPath();
        this.classLoader = new ASMClassLoader(classPath);
        this.randomHandler = new RandomHandler();
   
    }

    public static Context getInstance(ServerProvider serverProvider) {
        return instance == null ? instance = new Context(serverProvider) : instance;
    }
    
    public static Context getInstance() {
    	return getInstance(null);
    }

    public void setEnvironment() {
        classLoader = new ASMClassLoader(classPath);
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
        Core.verbose("Parsing server jar...");
        serverProvider.init();
        serverProvider.parseJar();
        Core.verbose("Done.");
        Core.verbose("Injecting hooks...");
        serverProvider.injectHooks();
        Core.verbose("Done.");
        Core.verbose("Fetching game applet...");
        gameApplet = serverProvider.fetchApplet();
        if (getClient() == null) {
            setClientInstance(gameApplet);
        }
        Core.verbose("Applet fetched.");
        serverProvider.addMenuItems(BotUI.getInstance().getJMenuBar());
        BotUI.getInstance().validate();
        final GamePanel panel = GamePanel.getInstance();
        panel.removeLoader();
        panel.add(gameApplet);
        gameApplet.setSize(765, 503);
        java.util.Timer t = new java.util.Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                gameApplet.setBounds(0, 0, 765, 503);
            }
        }, 1000);
        Core.verbose("Initializing mouse...");
        serverProvider.initMouse();
        Core.verbose("Done.");
        Core.verbose("Initializing keyboard...");
        serverProvider.initKeyboard();
        Core.verbose("Done.");
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

}
