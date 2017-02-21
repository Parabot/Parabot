package org.parabot.environment.scripts.executers;

import org.parabot.environment.scripts.Script;

import java.lang.reflect.Constructor;

/**
 * 
 * Loads a locally stored script
 * 
 * @author Everel
 *
 */
public class LocalScriptExecuter extends ScriptExecuter {
	private Constructor<?> scriptConstructor;
	
	public LocalScriptExecuter(final Constructor<?> scriptConstructor) {
		this.scriptConstructor = scriptConstructor;
	}

	@Override
	public void run(ThreadGroup tg) {
		try {
			super.finalize(tg, (Script) scriptConstructor.newInstance());
        } catch (Throwable t) {
            t.printStackTrace();
        }
	}

}
