
package org.parabot.environment.scripts.framework;

import org.parabot.environment.scripts.Script;

/**
 * Jython script, only supports a loop
 * 
 * @author Everel
 */
public abstract class PythonScript extends Script
{

	public abstract int loop();

}
