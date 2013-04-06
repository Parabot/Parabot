package org.parabot.core.ui;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.parabot.core.desc.ServerDescription;
import org.parabot.core.parsers.ServerManifestParser;
import org.parabot.core.ui.utils.Center;
import org.parabot.core.ui.widgets.ServerWidget;

public class ServerSelector extends JFrame {
	private static final long serialVersionUID = 1L;
	private static ServerSelector instance = null;

	public static ServerSelector getInstance() {
		if (instance != null) {
			instance.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			return instance;
		}
		return instance = new ServerSelector();
	}

	public ServerSelector() {
		setLayout(null);
		Queue<ServerWidget> widgets = getServers();
		JPanel p = new JPanel();
		p.setBounds(0, 0, 400, 800);
		p.setLayout(null);
		p.setPreferredSize(new Dimension(400, widgets.size() * 100));
		JScrollPane pane = new JScrollPane(p);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final int count = widgets.size() - 1;
		while (widgets.size() > 0) {
			final ServerWidget widget = widgets.poll();
			widget.setBounds(0, (count - widgets.size()) * 100, 400, 100);
			p.add(widget);
		}
		pane.setBounds(0, 0, 400, 200);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(pane);
		setResizable(false);
		setTitle("Servers");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Center.centerFramea(this, 406, 228);
	}

	public Queue<ServerWidget> getServers() {
		final Queue<ServerWidget> widgets = new LinkedList<ServerWidget>();
		for (ServerDescription desc : new ServerManifestParser()
				.getDescriptions()) {
			widgets.add(new ServerWidget(desc));
		}
		return widgets;
	}

}
