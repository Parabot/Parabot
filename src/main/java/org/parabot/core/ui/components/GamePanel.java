package org.parabot.core.ui.components;

import org.parabot.core.Context;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JPanel;

/**
 * Main panel where applets are added.
 *
 * @author Everel
 */
public class GamePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static GamePanel instance;

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

    /**
     * Gets instance of this panel
     *
     * @return instance of this panel
     */
    public static GamePanel getInstance() {
        return instance == null ? instance = new GamePanel() : instance;
    }

    /**
     * Updates context of this panel and adds a different Applet to the panel
     *
     * @param c
     */
    public void setContext(final Context c) {
        add(c.getApplet(), BorderLayout.CENTER);
    }

    /**
     * Removes all components
     */
    public void removeComponents() {
        removeAll();
    }
}
