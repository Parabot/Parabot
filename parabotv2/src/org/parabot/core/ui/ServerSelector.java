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
import org.parabot.core.ui.utils.AwtUtil;
import org.parabot.core.ui.utils.SwingUtil;
import org.parabot.core.ui.widgets.ServerWidget;

/**
 * 
 * @author Dane
 * 
 */

public class ServerSelector extends JFrame {

	private static final long serialVersionUID = 5238720307271493899L;
	private static ServerSelector instance = null;
	private JPanel panel;

	public static ServerSelector getInstance() {
		if (instance == null) {
			instance = new ServerSelector();
		}
		return instance;
	}

	public ServerSelector() {

		this.setTitle("Servers");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		this.panel = new JPanel(new BorderLayout());
		this.panel.setPreferredSize(new Dimension(400, 200));

		Queue<ServerWidget> widgets = getServers();

		JPanel interior = new JPanel(null);
		interior.setPreferredSize(new Dimension(400, widgets.size() * 100));

		int i = 0;
		for (ServerWidget w : widgets) {
			w.setSize(400, 100);
			w.setLocation(0, i * 100);
			interior.add(w);
			i++;
		}

		JScrollPane scrlInterior = new JScrollPane(interior);
		scrlInterior.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		this.panel.add(scrlInterior, BorderLayout.CENTER);
		this.add(panel);

		SwingUtil.finalize(this);

	}

	public Queue<ServerWidget> getServers() {
		final Queue<ServerWidget> widgets = new LinkedList<ServerWidget>();
		for (ServerDescription desc : new ServerManifestParser().getDescriptions()) {
			widgets.add(new ServerWidget(desc));
		}
		return widgets;
	}

}
