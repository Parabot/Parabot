package org.parabot.environment.scripts.framework;

/**
 * Abstract framework for a script
 *
 * @author Everel
 */
public abstract class AbstractFramework {

    /**
     * Executes this frame
     *
     * @return <b>true</b> if it should keep executing this framework, otherwise <b>false</b>.
     */
    public abstract boolean execute();

}
