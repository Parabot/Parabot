package org.parabot.core.paint;

import org.parabot.environment.api.interfaces.Paintable;

/**
 * 
 * Abstract class for debugging in game values & more
 * 
 * @author Everel
 *
 */
public abstract class AbstractDebugger implements Paintable {
	
	/**
	 * Toggles this debugger
	 */
	public abstract void toggle();

	/**
	 *
	 * @return True if this debugger is enabled, otherwise false
     */
	public abstract boolean isEnabled();

}
