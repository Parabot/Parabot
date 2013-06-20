package org.parabot.environment.servers;

import java.applet.Applet;
import java.applet.AppletStub;
import java.net.URL;
import javax.swing.JMenuBar;

import org.objectweb.asm.Opcodes;
import org.parabot.core.Context;
import org.parabot.core.asm.interfaces.Injectable;
import org.parabot.core.parsers.HookParser;

/**
 * Provides a server to the bot
 * 
 * @author Clisprail
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
			System.out.println("null");
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
	}

	/**
	 * Add custom items to the bot menu bar
	 * 
	 * @param menu
	 *            bar to add items on
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

}
