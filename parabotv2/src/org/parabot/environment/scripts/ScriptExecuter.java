package org.parabot.environment.scripts;

public abstract class ScriptExecuter {
	
	public abstract void run(final ThreadGroup tg);
	
	public final void finalize(final ThreadGroup tg, final Script script) {
		new Thread(tg, script).start();
	}


}
