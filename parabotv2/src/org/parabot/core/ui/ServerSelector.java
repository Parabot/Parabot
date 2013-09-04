package org.parabot.core.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.parabot.core.desc.ServerDescription;
import org.parabot.core.parsers.servers.ServerParser;
import org.parabot.core.ui.utils.SwingUtil;
import org.parabot.core.ui.widgets.ServerWidget;
import org.parabot.environment.Environment;

/**
 * 
 * Shows a list of every supported server which can be started
 * 
 * @author Dane, Everel
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
		// TODO: test this method
		if(widgets == null || widgets.isEmpty()) {
			return false;
		}
		final String serverName = initServer.toLowerCase();
		for(ServerWidget widget : widgets) {
			if(widget.desc.serverName.toLowerCase().equals(serverName)) {
				Environment.load(widget.desc);
				return true;
			}
		}
		return false;
	}

	/**
	 * Fetches array of server widgets
	 * @return widgets array
	 */
	public Queue<ServerWidget> getServers() {
		final Queue<ServerWidget> widgets = new LinkedList<ServerWidget>();
		ServerDescription[] servers = ServerParser.getDescriptions();
		if (servers != null) {
			for (ServerDescription desc : servers) {
				widgets.add(new ServerWidget(desc));
			}
		}
		return widgets;
	}

}
