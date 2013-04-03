package org.parabot.core.ui.applet;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.parabot.core.ui.images.Images;

/**
 * An informative applet which tells the user what bot is doing
 * 
 * @author Clisprail
 *
 */
public class LoadApplet extends Applet {
	private static final long serialVersionUID = 7412412644921803896L;
	private static LoadApplet current = null;
	private static String state = "Initializing loader...";
	private FontMetrics fontMetrics = null;
	private BufferedImage bot_image = null;

	public LoadApplet() {
		bot_image = Images.getResource("/org/parabot/core/ui/images/para.png");
	}

	@Override
	public void init() {
		setSize(775, 510);
		setBackground(Color.black);
	}

	/**
	 * Paints on this applet
	 */
	@Override
	public void paint(Graphics g) {
		g.setFont(new Font("Calibri", Font.PLAIN, 18));
		if (fontMetrics == null) {
			fontMetrics = g.getFontMetrics();
		}
		g.setColor(Color.white);
		int x = (getWidth() / 2) - (fontMetrics.stringWidth(state) / 2);
		g.drawString(state, x, 200);
		g.setFont(new Font("Calibri", Font.PLAIN, 12));
		final String version = "v2.0";
		g.drawString(version,
				getWidth() - g.getFontMetrics().stringWidth(version) - 10,
				getHeight() - 12);
		x = (getWidth() / 2) - (bot_image.getWidth() / 2);
		g.drawImage(bot_image, x, 80, null);
	}

	/**
	 * Gets instance of this applet
	 * @return instance of this applet
	 */
	public static LoadApplet get() {
		if (current != null) {
			return current;
		}
		final LoadApplet splash = new LoadApplet();
		splash.init();
		current = splash;
		return current;
	}

	/**
	 * Updates the status message and repaints the applet
	 * @param message
	 */
	public static void setState(final String message) {
		state = message;
		current.repaint();
	}

}
