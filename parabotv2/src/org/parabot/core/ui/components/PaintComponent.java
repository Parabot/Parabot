package org.parabot.core.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * 
 * The panel that is painted on
 * 
 * @author Everel
 *
 */
public class PaintComponent extends JPanel {
	private static final long serialVersionUID = 4653612412080038193L;
	
	public PaintComponent(Dimension size) {
		setPreferredSize(size);
		setSize(size);
		setOpaque(false);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		System.out.println("paint");
		g.setColor(Color.red);
		g.drawString("hi", 10, 10);
	}

}
