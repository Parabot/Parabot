package org.parabot.environment.scripts;

/**
 * 
 * @author Everel
 *
 */
public class LocalScriptExecuter extends ScriptExecuter {
	private Script script = null;
	
	public LocalScriptExecuter(final Script script) {
		this.script = script;
	}

	@Override
	public void run(ThreadGroup tg) {
		super.finalize(tg, this.script);
	}

}
