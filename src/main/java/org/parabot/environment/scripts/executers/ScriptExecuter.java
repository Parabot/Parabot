package org.parabot.environment.scripts.executers;

import org.parabot.environment.scripts.Script;

/**
 * Executes a script
 *
 * @author Everel
 */
public abstract class ScriptExecuter {

    public abstract void run(final ThreadGroup tg);

    /**
     * Start script.
     *
     * @param tg
     * @param script
     */
    public final void finalize(final ThreadGroup tg, final Script script) {
        Thread thread = new Thread(tg, script);
        thread.start();
    }


}
