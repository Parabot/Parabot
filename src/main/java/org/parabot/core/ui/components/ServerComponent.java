package org.parabot.core.ui.components;

import com.google.inject.Inject;
import org.parabot.core.Core;
import org.parabot.core.bdn.api.servers.IServerDownloader;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.ui.fonts.Fonts;
import org.parabot.environment.Environment;
import org.parabot.environment.api.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * A neat looking server component
 *
 * @author Everel
 */
public class ServerComponent extends JPanel implements MouseListener,
        MouseMotionListener {
    private static final long serialVersionUID = 1L;

    public ServerDescription desc;
    private String name;
    private boolean hovered;

    public ServerComponent() {

    }

    public ServerComponent setDesc(final ServerDescription desc) {
        this.desc = desc;
        setLayout(null);
        this.name = desc.getServerName().replaceAll(" ", "");

        addMouseListener(this);
        addMouseMotionListener(this);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return this;
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

        Color bgColor = Color.LIGHT_GRAY;
        if (hovered) {
            bgColor = Color.GRAY;
        }

        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, w, h);
        g.setColor(Color.black);
        Font title = Fonts.getResource("leelawadee.ttf", 16);
        g.setFont(title);
        String serverName = desc.getServerName();
        int sw = g.getFontMetrics().stringWidth(serverName);
        g.drawString(serverName, (w / 2) - (sw / 2), 30);

        Font normal = Fonts.getResource("leelawadee.ttf");
        g.setFont(normal);
        FontMetrics fm = g.getFontMetrics();
        String author = "Author: " + (desc.getAuthors().length > 0 ? StringUtils.implode(", ", desc.getAuthors()) : "");
        String revision = "Revision: " + desc.getRevision();

        g.drawString(author, (w / 2) - (fm.stringWidth(author) / 2), 55);
        g.drawString(revision, (w / 2) - (fm.stringWidth(revision) / 2), 70);
    }

    public void load(final ServerDescription desc) {
        Core.getInjector().getInstance(VerboseLoader.class).switchState(VerboseLoader.STATE_LOADING);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Environment.load(desc);

            }
        }).start();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!hovered) {
            hovered = true;
            this.repaint();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (hovered) {
            hovered = false;
            this.repaint();
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (hovered) {
            load(desc);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
