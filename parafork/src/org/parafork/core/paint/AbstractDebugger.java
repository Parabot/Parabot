package org.parafork.core.paint;

import org.parafork.environment.api.interfaces.Paintable;

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
	 * @return <b>true</b> if this debugger is enabled, otherwise <b>false</b>
	 */
	public abstract boolean isEnabled();

}
