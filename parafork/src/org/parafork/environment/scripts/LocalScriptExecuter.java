package org.parafork.environment.scripts;

/**
 * 
 * Loads a locally stored script
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
