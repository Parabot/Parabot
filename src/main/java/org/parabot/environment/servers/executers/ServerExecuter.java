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
 * @author Everel
 */
public abstract class ServerExecuter {

    public abstract void run();

    public void finalize(final ServerProvider provider) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BotUI.getInstance().getCacheClear().setEnabled(false);

                    Context context = Core.getInjector().getInstance(Context.class);
                    context.setServerProvider(provider);
                    context.load();

                    Core.getInjector().getInstance(PaintComponent.class).startPainting(context);

                    Core.getInjector().getInstance(RandomHandler.class).init();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }).start();
    }

}
