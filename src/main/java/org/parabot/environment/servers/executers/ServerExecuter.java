package org.parabot.environment.servers.executers;

import org.parabot.core.Context;
import org.parabot.core.parsers.randoms.RandomParser;
import org.parabot.core.ui.components.PaintComponent;
import org.parabot.environment.servers.ServerProvider;

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
					try{
						org.parabot.environment.api.utils.WindowsPreferences.userRoot().remove("Software\\JavaSoft\\Prefs");
					}catch (Exception e){
						// Ikov likes to creates preference keys, doesn't it?
					}

					Context context = Context.getInstance(provider);
					context.load();
					PaintComponent.getInstance().startPainting(context);
					RandomParser.enable();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}).start();
	}

}
