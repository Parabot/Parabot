package org.parabot.core.ui;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.parabot.core.ui.utils.Center;
import org.parabot.core.ui.widgets.ServerWidget;


public class ServerSelector extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static ServerSelector instance = null;

	public static ServerSelector getInstance() {
		if(instance != null) {
			instance.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			return instance;
		}
		return instance = new ServerSelector();
	}


	public ServerSelector() {
		setLayout(null);
		ServerWidget[] widgets = getServers();
		JPanel p = new JPanel();
		p.setBounds(0, 0, 400, 800);
		p.setLayout(null);
		p.setPreferredSize(new Dimension(400, widgets.length * 100));
		JScrollPane pane = new JScrollPane(p);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		for (int i = 0; i < widgets.length; i++) {
			widgets[i].setBounds(0, i * 100, 400, 100);
			p.add(widgets[i]);
		}
		pane.setBounds(0, 0, 400, 200);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(pane);
		setResizable(false);
		setTitle("Servers");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Center.centerFramea(this, 406, 228);
	}

	public ServerWidget[] getServers() {
		ArrayList<ServerWidget> widgets = new ArrayList<ServerWidget>();
		widgets.add(new ServerWidget("RecklessPk", "Clisprail", "317"));
		widgets.add(new ServerWidget("Soulsplit", "Clisprail", "317"));
		return widgets.toArray(new ServerWidget[widgets.size()]);
	}

}

