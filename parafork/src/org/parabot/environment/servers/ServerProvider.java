package org.parabot.environment.servers;

import java.applet.Applet;
import java.applet.AppletStub;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JMenuBar;

import org.objectweb.asm.Opcodes;
import org.parabot.core.Context;
import org.parabot.core.Directories;
import org.parabot.core.asm.interfaces.Injectable;
import org.parabot.core.io.SizeInputStream;
import org.parabot.core.parsers.HookParser;
import org.parabot.core.ui.components.VerboseLoader;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.Script;

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
		URL jarURL = getJar();

		URLConnection conn = null;
		try {
			conn = jarURL.openConnection();
		} catch (IOException e1) {
			//ignored
		}
		
		String name = jarURL.toString();
		name = name.substring(name.lastIndexOf('/') + 1);
		File f = new File(Directories.getCachePath(), name);
		if (!ServerCache.check(jarURL) || !f.exists() || f.length() != conn.getContentLength()) {
			VerboseLoader.setState("Caching:" + jarURL);
			byte[] b = new byte[1024];
			int len;
			try(InputStream in = new SizeInputStream(conn.getInputStream(),conn.getContentLength(),VerboseLoader.get())){
				try(OutputStream out = new FileOutputStream(f)){
					while((len = in.read(b)) != -1)
						out.write(b,0,len);
				}
				VerboseLoader.get().onProgressUpdate(100);
				ServerCache.setDate(jarURL);
			}catch(Exception e){
				//ignored
			}
		}
		try {
			Context.resolve().getClassPath().addJar(f.toURI().toURL());
		} catch (MalformedURLException e) {
		}
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
