package org.parabot.environment.servers.executers;

import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.core.parsers.randoms.RandomParser;
import org.parabot.core.ui.components.PaintComponent;
import org.parabot.environment.servers.ServerProvider;

import java.io.PrintStream;

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
					context.load();
					PaintComponent.getInstance().startPainting(context);

					Context.getInstance().getRandomHandler().init();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}).start();
	}

}
