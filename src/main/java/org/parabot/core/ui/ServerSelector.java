package org.parabot.core.ui;

import org.parabot.core.Core;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.parsers.servers.ServerParser;
import org.parabot.core.ui.components.ServerComponent;
import org.parabot.environment.Environment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Shows a list of every supported server which can be started
 *
 * @author Dane, Everel
 */

public class ServerSelector extends JPanel {
    private static final long serialVersionUID = 5238720307271493899L;
    public static String initServer;
    private static ServerSelector instance;

    public ServerSelector() {
        Queue<ServerComponent> widgets = getServers();
        if (initServer != null || Core.getQuickLaunchByUuid() > -1) {
            if (runServer(widgets)) {
                initServer = null;
                return;
            }
        }

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 350));

        JPanel interior = new JPanel(null);

        int i = 0;
        int y = 0;
        while (widgets != null && !widgets.isEmpty()) {
            final ServerComponent w = widgets.poll();
            w.setSize(300, 100);
            if (i % 2 == 0 && i != 0) {
                y += 100;
            }
            w.setLocation(i % 2 == 0 ? 0 : 300, y);
            interior.add(w);
            i++;
        }
        y += 100;
        interior.setPreferredSize(new Dimension(300, y));

        JScrollPane scrlInterior = new JScrollPane(interior);
        scrlInterior
                .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrlInterior
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrlInterior
                .getVerticalScrollBar().setUnitIncrement(16);
        add(scrlInterior);

    }

    public static ServerSelector getInstance() {
        if (instance == null) {
            instance = new ServerSelector();
        }
        return instance;
    }

    /**
     * Fetches array of server widgets
     *
     * @return widgets array
     */
    public Queue<ServerComponent> getServers() {
        final Queue<ServerComponent> widgets = new LinkedList<>();
        ServerDescription[] servers = ServerParser.getDescriptions();
        if (servers != null) {
            for (ServerDescription desc : servers) {
                widgets.add(new ServerComponent(desc));
            }
        }
        return widgets;
    }

    /**
     * This method is called when -server argument is given, or -uuid arg is given.
     *
     * @param widgets
     */
    private boolean runServer(Queue<ServerComponent> widgets) {
        if (widgets == null || widgets.isEmpty()) {
            return false;
        }
        if (Core.getQuickLaunchByUuid() > -1) { // match the pre-requested server config uuid to quick-launch
            for (ServerComponent widget : widgets) {
                if (widget.desc.uuid == Core.getQuickLaunchByUuid()) {
                    Environment.load(widget.desc);
                    return true;
                }
            }
            System.err.println("No server config with -uuid " + Core.getQuickLaunchByUuid() + " was found to quick launch.");
        }

        if (initServer != null) {
            final String serverName = initServer.toLowerCase(); // match the pre-requested server name to quick-launch
            for (ServerComponent widget : widgets) {
                if (widget.desc.getServerName().toLowerCase().equals(serverName)) {
                    Environment.load(widget.desc);
                    return true;
                }
            }
            System.err.println("No server config with -server " + serverName + " was found to quick launch.");
        }
        return false;
    }

}
