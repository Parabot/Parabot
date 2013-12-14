package org.parabot.environment.scripts;

/**
 * 
 * Executes a script
 * 
 * @author Everel
 *
 */
public abstract class ScriptExecuter {
	
	public abstract void run(final ThreadGroup tg);
	
	/**
	 * Start script.
	 * @param tg
	 * @param script
	 */
	public final void finalize(final ThreadGroup tg, final Script script) {
		new Thread(tg, script).start();
	}


}
