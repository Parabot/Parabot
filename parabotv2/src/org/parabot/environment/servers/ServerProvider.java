package org.parabot.environment.servers;

import java.applet.Applet;
import java.applet.AppletStub;
import java.net.URL;
import javax.swing.JMenuBar;

import org.parabot.core.Context;
/**
 * Provides a server to the bot
 *  
 * @author Clisprail
 *
 */
public abstract class ServerProvider {
	public Context context = new Context(this);
	
	/**
	 * Hooks to parse
	 * @return URL to hooks file
	 */
	public URL getHooks() {
		return null;
	}
	
	/**
	 * Jar to parse
	 * @return URL to client jar
	 */
	public abstract String getJar();
	
	public abstract Applet fetchApplet();
	
	public String getAccessorsPackage() {
		return null;
	}
	
	/**
	 * Add custom items to the bot menu bar
	 * @param menu bar to add items on
	 */
	public void addMenuItems(JMenuBar bar) {
	}
	
	public AppletStub getStub() {
		return null;
	}
	
	public void parseJar() {
		context.getClassPath().addJar(getJar());
	}

}
