package org.parabot.environment.scripts.framework;

/**
 * Strategy framework for scripts
 *
 * @author Everel
 */
public interface Strategy {

    /**
     * Whether to activate this strategy
     *
     * @return <b>true</b> if this strategy should be executed, otherwise <b>false</b>.
     */
    boolean activate();

    /**
     * Executes this strategy
     */
    void execute();

}
