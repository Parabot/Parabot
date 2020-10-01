package org.parabot.core.ui;

import org.parabot.core.ui.components.PaintComponent;
import org.parabot.environment.OperatingSystem;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JDialog;

/**
 * @author Everel
 */
public class BotDialog extends JDialog {
    private static final long serialVersionUID = 521800552287194673L;
    private static BotDialog instance;

    private BotDialog(BotUI botUI) {
        super(botUI);

        botUI.setDialog(this);
        setUndecorated(true);
        getRootPane().setOpaque(false);
        if (!OperatingSystem.getOS().equals(OperatingSystem.OTHER)) {
            try {
                setBackground(new Color(0, 0, 0, 0));
            } catch (UnsupportedOperationException e) {
                //My "fix" for the perpixel errors some user have when using VPSes
                if (e.getMessage().contains("PERPIXEL_TRANS")) {
                    System.err
                            .println("WARNING: We were unable to set a translucent background!"
                                    + "\n\tThis generally occurs with old/outdated graphics drivers. Please consider updating them if possible."
                                    + "\n\tParabot will still attempt to run, however some GUI elements may or may not function.");
                }
            }
        }
        setFocusableWindowState(true);
        setPreferredSize(botUI.getSize());
        setSize(botUI.getSize());
        setVisible(true);
        setContentPane(PaintComponent.getInstance(botUI.getSize()));
        botUI.setVisible(true);

    }

    public static BotDialog getInstance(BotUI botUI) {
        return instance == null ? instance = new BotDialog(botUI) : instance;
    }

    public static BotDialog getInstance() {
        return getInstance(null);
    }

    public void setDimensions(Dimension dimension) {
        setUndecorated(true);
        getRootPane().setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setFocusableWindowState(true);
        setPreferredSize(dimension);
        setSize(dimension);
        setVisible(true);
        setContentPane(PaintComponent.getInstance());
        PaintComponent.getInstance().setDimensions(dimension);
    }

}
