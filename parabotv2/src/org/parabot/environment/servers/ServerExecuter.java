
package org.parabot.environment.servers;

import org.parabot.core.Context;
import org.parabot.core.ui.components.PaintComponent;

/**
 * Executes a server provider
 * 
 * @author Everel
 */
public abstract class ServerExecuter
{

	public abstract void run();


	public void finalize( final ServerProvider provider, final String serverName )
	{
		new Thread( new Runnable()
		{

			@Override
			public void run()
			{
				try {
					Context context = Context.getInstance( provider );
					context.load();
					PaintComponent.getInstance().startPainting( context );
				} catch( Throwable t ) {
					t.printStackTrace();
				}
			}
		} ).start();
	}

}
