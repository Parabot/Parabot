package org.parabot.environment.servers;

import org.objectweb.asm.Opcodes;
import org.parabot.core.Context;
import org.parabot.core.asm.hooks.HookFile;
import org.parabot.core.asm.interfaces.Injectable;
import org.parabot.core.parsers.hooks.HookParser;
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

//    public static Handler.RandomChecker getRandomChecker() {
//        Handler.RandomChecker randomChecker = new Handler.RandomChecker();
//        return randomChecker;
//    }

    /**
	 * Hooks to parse
	 * 
	 * @deprecated use getHookFile() now
	 * @return URL to hooks file
	 */
	@Deprecated
	public URL getHooks() {
		return null;
	}
	
	/**
	 * Get hook file to parse
	 * @return hook file
	 */
	public HookFile getHookFile() {
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
		HookFile hookFile = fetchHookFile();
		
		if(hookFile == null) {
			return;
		}
		
		HookParser parser = hookFile.getParser();
		Injectable[] injectables = parser.getInjectables();
		if (injectables == null) {
			return;
		}
		for (Injectable inj : injectables) {
			inj.inject();
		}
		Context.getInstance().setHookParser(parser);
	}
	
	private HookFile fetchHookFile() {
		HookFile hookFile = getHookFile();
		if(hookFile != null) {
			return hookFile;
		}
		
		URL hookLocation = getHooks();
		if(hookLocation == null) {
			return null;
		}
		
		return new HookFile(hookLocation, HookFile.TYPE_XML);
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
		Context.getInstance().setClientInstance(client);
	}

	public void parseJar() {
		Context.getInstance().getClassPath().addJar(getJar());
	}
	
	public void initScript(Script script) {
		
	}
	
	public void init() {
		
	}
	
	public void initMouse() {
		final Context context = Context.getInstance();
		final Applet applet = context.getApplet();
		final Mouse mouse = new Mouse(applet);
		applet.addMouseListener(mouse);
		applet.addMouseMotionListener(mouse);
		context.setMouse(mouse);
	}
	
	public void initKeyboard() {
		final Context context = Context.getInstance();
		final Applet applet = context.getApplet();
		final Keyboard keyboard = new Keyboard(applet);
		applet.addKeyListener(keyboard);
		context.setKeyboard(keyboard);
	}
	
	public void unloadScript(Script script) {
		
	}


}
