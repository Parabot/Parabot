package org.parabot.environment.servers;

import org.objectweb.asm.Opcodes;
import org.parabot.core.Context;
import org.parabot.core.asm.interfaces.Injectable;
import org.parabot.core.parsers.HookParser;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.Script;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AppletStub;
import java.net.URL;

/**
 * Provides a server to the bot
 * 
 * @author Everel
 * 
 */
public abstract class ServerProvider implements Opcodes {

	/**
	 * Hooks to parse
	 * 
	 * @return URL to hooks file
	 */
	public URL getHooks() {
		return null;
	}

	/**
	 * Jar to parse
	 * 
	 * @return URL to client jar
	 */
	public abstract URL getJar();

	public abstract Applet fetchApplet();

	public String getAccessorsPackage() {
		return null;
	}

	public void injectHooks() {
		URL hooksFile = getHooks();
		if (hooksFile == null) {
			return;
		}
		HookParser parser = new HookParser(hooksFile);
		Injectable[] injectables = parser.getInjectables();
		if (injectables == null) {
			return;
		}
		for (Injectable inj : injectables) {
			inj.inject();
		}
		Context.resolve().setHookParser(parser);
	}

	/**
	 * Add custom items to the bot menu bar
	 * 
	 * @param bar
	 * menu bar to add items on
	 */
	public void addMenuItems(JMenuBar bar) {
	}

	public AppletStub getStub() {
		return null;
	}
	
	public void setClientInstance(Object client) {
		Context.resolve().setClientInstance(client);
	}

	public void parseJar() {
		Context.resolve().getClassPath().addJar(getJar());
	}
	
	public void initScript(Script script) {
		
	}
	
	public void initMouse() {
		final Context context = Context.resolve();
		final Applet applet = context.getApplet();
		final Mouse mouse = new Mouse(applet);
		applet.addMouseListener(mouse);
		applet.addMouseMotionListener(mouse);
		context.setMouse(mouse);
	}
	
	public void initKeyboard() {
		final Context context = Context.resolve();
		final Applet applet = context.getApplet();
		final Keyboard keyboard = new Keyboard(applet);
		applet.addKeyListener(keyboard);
		context.setKeyboard(keyboard);
	}
	
	public void unloadScript(Script script) {
		
	}

}
