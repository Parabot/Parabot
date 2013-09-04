package org.parabot.core.ui.components;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.GroupLayout;
import javax.swing.JPanel;

import org.parabot.core.Context;

/**
 * 
 * Main panel where applets are added.
 * 
 * @author Everel
 * 
 */
public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static GamePanel instance = null;
	private static VerboseLoader loader = VerboseLoader.get();
	public Context context = null;

	private GamePanel() {
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setOpaque(true);
		setBackground(Color.black);
		setPreferredSize(new Dimension(770, 503));
		GroupLayout panelLayout = new GroupLayout(this);
		setLayout(panelLayout);
		panelLayout.setHorizontalGroup(panelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGap(0, 770, Short.MAX_VALUE));
		panelLayout.setVerticalGroup(panelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGap(0, 418, Short.MAX_VALUE));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	/**
	 * Updates context of this panel and adds a different Applet to the panel
	 * @param context
	 */
	public void setContext(final Context c) {
		if(context != null) {
			context.getApplet().setVisible(false);
		}
		context = c;
		if(c == null) {
			return;
		}
		final Applet gameApplet = context.getApplet();
		if(!c.added) {
			add(gameApplet);
			c.added = true;
			return;
		}
		gameApplet.setVisible(true);
	}
	

	/**
	 * Gets instance of this panel
	 * @return instance of this panel
	 */
	public static GamePanel getInstance() {
		return instance == null ? instance = new GamePanel() : instance;
	}

	/**
	 * Adds the loader applet
	 */
	public void addLoader() {
		add(loader);
	}

	/**
	 * Removes the loader applet
	 */
	public void removeLoader() {
		remove(loader);
	}
}
