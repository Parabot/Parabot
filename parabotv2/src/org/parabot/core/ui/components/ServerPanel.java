package org.parabot.core.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.GroupLayout;
import javax.swing.JPanel;

/**
 * 
 * @author Everel
 * 
 */
public class ServerPanel extends JPanel {
	private static final long serialVersionUID = 6034139911394898295L;

	public ServerPanel() {
		setBorder(null);
		setOpaque(false);
		setPreferredSize(new Dimension(765, 503));
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 706, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 418, Short.MAX_VALUE));
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

}
