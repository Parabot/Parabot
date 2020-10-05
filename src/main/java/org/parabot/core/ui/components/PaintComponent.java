package org.parabot.core.ui.components;

import org.parabot.core.Context;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Time;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * The panel that is painted on
 *
 * @author Everel
 */
public class PaintComponent extends JComponent implements Runnable {
    private static final long serialVersionUID = 4653612412080038193L;
    private static PaintComponent instance;

    private BufferedImage buffer;
    private Graphics2D g2;
    private Dimension dimensions;
    private Context context;

    private PaintComponent(Dimension dimensions) {
        this.dimensions = dimensions;
        this.buffer = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_ARGB);
        this.g2 = buffer.createGraphics();

        setPreferredSize(dimensions);
        setSize(dimensions);
        setOpaque(false);
        setIgnoreRepaint(true);
    }

    public static PaintComponent getInstance(Dimension dimensions) {
        return instance == null ? instance = new PaintComponent(dimensions) : instance;
    }

    public static PaintComponent getInstance() {
        return getInstance(null);
    }

    public void setDimensions(Dimension dimensions) {
        this.dimensions = dimensions;
        this.dimensions = dimensions;
        this.buffer = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_ARGB);
        this.g2 = buffer.createGraphics();

        setPreferredSize(dimensions);
        setSize(dimensions);
        setOpaque(false);
        setIgnoreRepaint(true);
    }

    public void startPainting(Context context) {
        this.context = context;
        new Thread(this).start();
    }

    @Override
    public void paintComponent(Graphics g) {
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, dimensions.width, dimensions.height);
        g2.setComposite(AlphaComposite.SrcOver);

        if (context != null) {
            for (Paintable p : context.getPaintables()) {
                p.paint(g);
            }
            context.getPaintDebugger().debug(g2);
        }
        g.drawImage(buffer, 0, 0, null);
    }

    @Override
    public void run() {
        while (true) {
            Time.sleep(100);
            repaint();
        }
    }

}
