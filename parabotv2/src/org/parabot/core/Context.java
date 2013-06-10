package org.parabot.core;

import java.applet.Applet;
import java.util.HashMap;
import java.util.TimerTask;

import org.objectweb.asm.tree.ClassNode;
import org.parabot.core.asm.ASMClassLoader;
import org.parabot.core.bot.loader.BotLoader;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.ui.components.GamePanel;
import org.parabot.environment.servers.ServerProvider;

/**
 * Game context
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
	 * @param serverEnvironment
	 */
	public void setEnvironment(ASMClassLoader serverEnvironment) {
		classLoader = new BotLoader(classPath, serverEnvironment);
	}
	
	/**
	 * ClassPath
	 * @return classpath
	 */
	public ClassPath getClassPath() {
		return classPath;
	}
	
	/**
	 * Determines if applet has been set
	 * @return <b>true</b> if set
	 */
	public boolean appletSet() {
		return gameApplet != null;
	}
	
	/**
	 * Gets game applet
	 * @return applet
	 */
	public Applet getApplet() {
		return gameApplet;
	}

	/**
	 * Resolves the context from threadgroup
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
		for(final ClassNode node : classPath.classes.values()) {
			serverProvider.inject(node);
		}
		gameApplet = serverProvider.fetchApplet();
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
	 * @return server provider
	 */
	public ServerProvider getServerProvider() {
		return serverProvider;
	}

	/**
	 * Gets class loader from this context
	 * @return class loader
	 */
	public ASMClassLoader getASMClassLoader() {
		return classLoader;
	}

	public static int getID() {
		return id;
	}

	public int getTab() {
		return tab;
	}

}
