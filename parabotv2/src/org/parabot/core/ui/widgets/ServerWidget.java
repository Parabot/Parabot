package org.parabot.core.ui.widgets;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.parabot.core.desc.ServerDescription;
import org.parabot.core.ui.BotUI;
import org.parabot.core.ui.ServerSelector;
import org.parabot.environment.Environment;

/**
 * A colorful server widget
 * 
 * @author Everel
 * 
 */
public class ServerWidget extends JPanel {

	private static final long serialVersionUID = 1L;
	private String name = null;
	public ServerDescription desc = null;

	final ActionListener play = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			load(desc);
		}
	};

	public ServerWidget(final ServerDescription desc) {
		this.desc = desc;
		setLayout(null);
		this.name = desc.serverName.replaceAll(" ", "");
		JLabel l = new JLabel();
		l.setFont(new Font("Arial", Font.BOLD, 16));
		l.setForeground(Color.white);
		l.setText(desc.serverName);
		l.setBounds(10, 10, 200, 20);
		add(l);
		final Font f = new Font("Arial", Font.PLAIN, 12);
		add(l);
		l = new JLabel();
		l.setFont(f);
		l.setForeground(Color.white);
		l.setBounds(10, 45, 100, 20);
		l.setText("Author: " + desc.author);
		add(l);
		l = new JLabel();
		l.setFont(f);
		l.setForeground(Color.white);
		l.setBounds(10, 60, 100, 20);
		l.setText("Revision: " + desc.revision);
		add(l);
		final JButton b = new JButton("Start");
		b.setFocusable(false);
		b.setBounds(300, 70, 70, 20);
		b.setOpaque(false);
		b.addActionListener(play);
		add(b);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		setOpaque(false);
		super.paintComponent(g);
		setOpaque(true);
		int w = getWidth();
		int h = getHeight();
		Color color1 = new Color(41, 97, 105);
		Color color2 = color1.darker();
		GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, w, h);
	}

	public void load(final ServerDescription desc) {
		ServerSelector.getInstance().dispose();
		BotUI.getInstance().setVisible(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Environment.load(desc);

			}
		}).start();
	}
}
