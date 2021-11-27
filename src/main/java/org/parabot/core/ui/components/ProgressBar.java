package org.parabot.core.ui.components;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

/**
 * ProgressBar
 *
 * @author Everel
 */
public class ProgressBar {
    private final int width;
    private final int height;
    private final Color backColor;
    private double value;
    private double locX;
    private Color progColor;
    private FontMetrics fontMetrics;
    private String text;

    public ProgressBar(int width, int height) {
        this.progColor = new Color(255, 0, 0);
        this.backColor = new Color(74, 74, 72, 100);
        this.width = width;
        this.height = height;
        this.text = "";
    }

    public void setText(final String text) {
        this.text = text;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        if (value < 0 || value > 100) {
            return;
        }
        if (value > 99) {
            value = 100;
        }
        this.value = value;
        this.locX = (width / 100.0D) * value;

        int val = (int) value;
        /*if (value <= 50) {
			this.progColor = new Color(255, (2 * val), 0);
		} else {
			val -= 50;
			this.progColor = new Color((int) (255 - (5.1D * val)),
					100 + (2 * val), 0);
		}*/

        int r = (int) (((double) (225 - 218) * (double) val) / 100.D);
        int g = (int) (((double) (253 - 165) * (double) val) / 100.D);
        int b = (int) (((double) (145 - 32) * (double) val) / 100.D);
        this.progColor = new Color(255 - r, 253 - g, 145 - b);
    }

    public void draw(Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (fontMetrics == null) {
            fontMetrics = g2.getFontMetrics();
        }
        g2.setColor(backColor);
        g2.fillRect(x, y, width, height);
        g2.setColor(Color.DARK_GRAY);
        g2.drawRect(x - 1, y - 1, width + 1, height + 1);
        g2.setColor(this.progColor);
        g2.fill(new Rectangle2D.Double(x, y, locX, height));

        int value = (int) getValue();
        String percent = value + "% " + text;

        int strX = (x + (width / 2)) - (fontMetrics.stringWidth(percent) / 2);
        g2.setColor(Color.white);
        g2.drawString(percent, strX, y + 13);
    }

}

