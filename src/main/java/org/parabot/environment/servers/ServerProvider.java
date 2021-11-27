package org.parabot.environment.servers;

import org.objectweb.asm.Opcodes;
import org.parabot.core.Configuration;
import org.parabot.core.Context;
import org.parabot.core.asm.hooks.HookFile;
import org.parabot.core.asm.interfaces.Injectable;
import org.parabot.core.parsers.hooks.HookParser;
import org.parabot.core.ui.utils.UILog;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.Script;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

/**
 * Provides a server to the bot
 *
 * @author Everel
 */
public abstract class ServerProvider implements Opcodes {
    private boolean crashed = false;

    /**
     * Get the game/applet dimensions
     *
     * @return game/applet dimensions
     */
    public Dimension getGameDimensions() {
        return new Dimension(765, 503);
    }

    /**
     * Hooks to parse
     *
     * @return URL to hooks file
     *
     * @deprecated use getHookFile() now
     */
    @Deprecated
    public URL getHooks() {
        return null;
    }

    /**
     * Get hook file to parse
     *
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

        if (hookFile == null) {
            return;
        }

        HookParser parser = hookFile.getParser();
        Injectable[] injectables = parser.getInjectables();

        if (injectables == null) {
            return;
        }

        int index = 0;

        try {
            for (Injectable inj : injectables) {
                inj.inject();
                index++;
            }
        } catch (NullPointerException ex) {
            if (!crashed) {
                Injectable inj = injectables[index];

                int resp = UILog.alert("Outdated client", "This server currently has outdated hooks, please report it to the Parabot staff.\r\n\r\n" +
                        "Broken hook:\r\n" + inj, new Object[]{ "Close", "Report here..." }, JOptionPane.ERROR_MESSAGE);

                if (resp == 1) {
                    URI uri = URI.create(Configuration.COMMUNITY_PAGE + "forum/135-reports/");
                    try {
                        Desktop.getDesktop().browse(uri);
                    } catch (IOException ignore) {
                    }
                }
            }
            crashed = true;
            throw ex;
        }

        Context.getInstance().setHookParser(parser);
    }

    /**
     * Add custom items to the bot menu bar
     *
     * @param bar menu bar to add items on
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

    /**
     * Called in Context.setApplet before applet.init() is called. Exclusively used for manipulating the Frame attached
     * to the applet of Roatpkz.
     */
    public void preAppletInit() {

    }

    /**
     * Called in Context.setApplet before after applet.start()  and applet.init() are called. Exclusively used for manipulating the Frame attached
     * to the applet of Roatpkz.
     */
    public void postAppletStart() {

    }

    private HookFile fetchHookFile() {
        HookFile hookFile = getHookFile();
        if (hookFile != null) {
            return hookFile;
        }

        URL hookLocation = getHooks();
        if (hookLocation == null) {
            return null;
        }

        return new HookFile(hookLocation, HookFile.TYPE_XML);
    }

}
