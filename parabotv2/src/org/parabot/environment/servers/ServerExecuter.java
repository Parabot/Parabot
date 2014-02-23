package org.parabot.environment.servers;

import org.parabot.core.Context;
import org.parabot.core.ui.components.BotToolbar;
import org.parabot.core.ui.components.PaintComponent;

/**
 * 
 * Executes a server provider
 * 
 * @author Everel
 *
 */
public abstract class ServerExecuter {

	public abstract void run();

	public void finalize(final ServerProvider provider, final String serverName) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Context context = Context.getInstance(provider);
					BotToolbar.getInstance().addTab(context, serverName);
					context.load();
					//PaintComponent.getInstance().startPainting(context.getPaintDebugger());
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}).start();
	}

}
