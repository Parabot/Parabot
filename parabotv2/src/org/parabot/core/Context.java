package org.parabot.core;

import java.applet.Applet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

import org.parabot.core.asm.ASMClassLoader;
import org.parabot.core.bot.loader.BotLoader;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.paint.PaintDebugger;
import org.parabot.core.ui.BotUI;
import org.parabot.core.ui.components.GamePanel;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.servers.ServerProvider;

/**
 * Game context
 * 
 * @author Clisprail
 * 
 */
public class Context {
	private static HashMap<ThreadGroup, Context> threadGroups = new HashMap<ThreadGroup, Context>();
	private static int id = 1;

	private ASMClassLoader classLoader = null;
	private ClassPath classPath = null;
	private ServerProvider serverProvider = null;
	private int tab = 0;
	private Applet gameApplet = null;

	private Object clientInstance = null;

	private static ArrayList<Paintable> paintables = new ArrayList<Paintable>();

	private PaintDebugger paintDebugger = new PaintDebugger();

	public boolean added = false;

	public Context(final ServerProvider serverProvider) {
		threadGroups.put(Thread.currentThread().getThreadGroup(), this);
		tab = id;
		this.serverProvider = serverProvider;
		id++;
		this.classPath = new ClassPath();
	}

	/**
	 * Sets the ServerProvider class loader
	 * 
	 * @param serverEnvironment
	 */
	public void setEnvironment(ASMClassLoader serverEnvironment) {
		classLoader = new BotLoader(classPath, serverEnvironment);
	}

	/**
	 * Sets the main client instance
	 */
	public void setClientInstance(Object object) {
		this.clientInstance = object;
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
	 * Resolves the context from threadgroup
	 * 
	 * @return context
	 */
	public static Context resolve() {
		return threadGroups.get(Thread.currentThread().getThreadGroup());
	}

	/**
	 * Loads the game
	 */
	public void load() {
		serverProvider.parseJar();
		serverProvider.injectHooks();
		gameApplet = serverProvider.fetchApplet();
		if (getClient() == null) {
			setClientInstance(gameApplet);
		}
		serverProvider.addMenuItems(BotUI.getInstance().getJMenuBar());
		BotUI.getInstance().validate();
		final GamePanel panel = GamePanel.getInstance();
		panel.removeLoader();
		panel.setContext(this);
		gameApplet.setSize(765, 503);
		java.util.Timer t = new java.util.Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				gameApplet.setBounds(0, 0, 765, 503);
			}
		}, 1000);
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
	 * Gets class loader from this context
	 * 
	 * @return class loader
	 */
	public ASMClassLoader getASMClassLoader() {
		return classLoader;
	}

	/**
	 * Gets the id of this context
	 * 
	 * @return id context
	 */
	public static int getID() {
		return id;
	}

	/**
	 * Tab id of this context
	 * 
	 * @return tab id of this context
	 */
	public int getTab() {
		return tab;
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

}
