package org.parabot.core.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JDialog;

import org.parabot.core.ui.components.PaintComponent;

/**
 * 
 * @author Everel
 *
 */
public class BotDialog extends JDialog {
	private static final long serialVersionUID = 521800552287194673L;

	public BotDialog(BotUI botUI) {
		super(botUI);
		botUI.setDialog(this);
		
		setLayout(new BorderLayout());
		setUndecorated(true);
		setBackground(new Color(0, 0, 0, 0));
		setContentPane(new PaintComponent(botUI.getSize()));
		setPreferredSize(botUI.getSize());
		setSize(botUI.getSize());
		setVisible(true);
	}

}

