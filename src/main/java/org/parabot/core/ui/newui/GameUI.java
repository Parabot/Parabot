package org.parabot.core.ui.newui;

import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.core.ui.components.GamePanel;
import org.parabot.environment.servers.ServerProvider;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;

/**
 * @author JKetelaar
 */
public class GameUI {

    private GamePanel gamePanel;
    private Dimension appletSize;

    public Dimension getAppletSize() {
        if (this.appletSize != null) {
            return this.appletSize;
        } else {
            return new Dimension();
        }
    }

    private void init() {
        Context context = Core.getInjector().getInstance(Context.class);
        context.load();

        ServerProvider serverProvider = context.getServerProvider();
        Applet         gameApplet     = serverProvider.fetchApplet();

        context.setClientInstance(gameApplet);

        gamePanel = Core.getInjector().getInstance(GamePanel.class);
        this.appletSize = serverProvider.getGameDimensions();

        gamePanel.setSize(appletSize);

        gamePanel.removeComponents();
        gameApplet.setSize(appletSize);
        gamePanel.add(gameApplet);

        Thread runnable = new Thread(() -> {
            gameApplet.init();
            gameApplet.start();

            gameApplet.setBounds(0, 0, appletSize.width, appletSize.height);
        });

        SwingUtilities.invokeLater(runnable);

//        try {
//            runnable.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public GamePanel getContent() {
        if (gamePanel == null) {
            this.init();
        }

        return gamePanel;
    }
}
