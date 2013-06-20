package org.parabot.core.paint;

import org.parabot.environment.api.interfaces.Paintable;

public abstract class AbstractDebugger implements Paintable {
	
	public abstract void toggle();
	
	public abstract boolean isEnabled();

}
