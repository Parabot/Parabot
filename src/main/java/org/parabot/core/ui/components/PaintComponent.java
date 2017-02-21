package org.parabot.core.ui.components;

import com.google.inject.Singleton;
import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.core.paint.PaintDebugger;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Time;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The panel that is painted on
 *
 * @author Everel
 */
@Singleton
public class PaintComponent extends JComponent implements Runnable {
    private static final long serialVersionUID = 4653612412080038193L;

    private BufferedImage buffer;
    private Graphics2D g2;
    private Dimension dimensions;
    private Context context;

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
            Core.getInjector().getInstance(PaintDebugger.class).debug(g2);
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
