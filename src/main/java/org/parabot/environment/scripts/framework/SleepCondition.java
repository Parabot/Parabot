package org.parabot.environment.scripts.framework;

/**
 * Keeps sleeping till a condition is valid
 *
 * @author Everel
 */
public interface SleepCondition {

    /**
     * Determine if condition is valid
     *
     * @return <b>true</b> if valid, otherwise <b>false</b>.
     */
    boolean isValid();

}

