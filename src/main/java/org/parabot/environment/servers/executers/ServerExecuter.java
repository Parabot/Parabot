package org.parabot.environment.servers.executers;

import org.parabot.core.Context;
import org.parabot.core.ui.BotUI;
import org.parabot.core.ui.components.PaintComponent;
import org.parabot.environment.handlers.exceptions.ExceptionHandler;
import org.parabot.environment.handlers.exceptions.FileExceptionHandler;
import org.parabot.environment.servers.ServerProvider;

import javax.swing.*;

/**
 * Executes a server provider
 *
 * @author Everel
 */
public abstract class ServerExecuter {

    public abstract void run();

    public void finalize(final ServerProvider provider, final String serverName) {
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BotUI.getInstance().getCacheClear().setEnabled(false);
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                    Context context = Context.getInstance(provider);
                            context.load();
                    PaintComponent.getInstance().startPainting(context);
                        }
                    });
                    Context.getInstance().getRandomHandler().init();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });

        serverThread.setUncaughtExceptionHandler(new FileExceptionHandler(ExceptionHandler.ExceptionType.SERVER));

        serverThread.start();
    }

}
