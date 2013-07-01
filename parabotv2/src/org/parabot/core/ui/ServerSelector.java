package org.parabot.core.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.parabot.core.desc.ServerDescription;
import org.parabot.core.parsers.ServerManifestParser;
import org.parabot.core.ui.utils.SwingUtil;
import org.parabot.core.ui.widgets.ServerWidget;
import org.parabot.environment.Environment;

/**
 * 
 * @author Dane, Clisprail
 * 
 */

public class ServerSelector extends JFrame {

	private static final long serialVersionUID = 5238720307271493899L;
	private static ServerSelector instance = null;
	private JPanel panel;
	
	public static String initServer = null;

	public static ServerSelector getInstance() {
		if (instance == null) {
			instance = new ServerSelector();
		}
		return instance;
	}

	public ServerSelector() {
		
		Queue<ServerWidget> widgets = getServers();
		if(initServer != null) {
			if(runServer(widgets)) {
				initServer = null;
				return;
			}
		}

		this.setTitle("Servers");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		this.panel = new JPanel(new BorderLayout());
		this.panel.setPreferredSize(new Dimension(400, 200));

		JPanel interior = new JPanel(null);
		interior.setPreferredSize(new Dimension(400, widgets.size() * 100));

		int i = 0;
		while (widgets != null && !widgets.isEmpty()) {
			final ServerWidget w = widgets.poll();
			w.setSize(400, 100);
			w.setLocation(0, i * 100);
			interior.add(w);
			i++;
		}

		JScrollPane scrlInterior = new JScrollPane(interior);
		scrlInterior.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrlInterior.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		this.panel.add(scrlInterior, BorderLayout.CENTER);
		this.add(panel);

		SwingUtil.finalize(this);

	}
	
	/**
	 * This method is called when -server argument is given
	 * @param widgets
	 */
	private boolean runServer(Queue<ServerWidget> widgets) {
		if(widgets == null || widgets.isEmpty()) {
			return false;
		}
		final String serverName = initServer.toLowerCase();
		for(ServerWidget widget : widgets) {
			if(widget.desc.serverName.toLowerCase().equals(serverName)) {
				Environment.load(widget.desc, widget.desc.serverName.replaceAll(" ", ""));
				return true;
			}
		}
		return false;
	}

	public Queue<ServerWidget> getServers() {
		final Queue<ServerWidget> widgets = new LinkedList<ServerWidget>();
		ServerDescription[] servers = new ServerManifestParser().getDescriptions();
		if (servers != null) {
			for (ServerDescription desc : servers) {
				widgets.add(new ServerWidget(desc));
			}
		}
		return widgets;
	}

}
