package org.parabot.environment.servers.executers;

import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.core.ui.BotUI;
import org.parabot.core.ui.components.PaintComponent;
import org.parabot.environment.randoms.RandomHandler;
import org.parabot.environment.servers.ServerProvider;

/**
 * Executes a server provider
 *
 * @author Everel, JKetelaar
 */
public abstract class ServerExecutor {

    public abstract void run();

    public void finalize(final ServerProvider provider) {
        Context context = Core.getInjector().getInstance(Context.class);
        context.setServerProvider(provider);

        new Thread(() -> {
            try {
//                Core.getInjector().getInstance(BotUI.class).getCacheClear().setEnabled(false);

                Core.getInjector().getInstance(PaintComponent.class).startPainting(context);

//                Core.getInjector().getInstance(RandomHandler.class).init();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }).start();
    }

}
