package org.parabot.environment.scripts.framework;

/**
 * A LoopTask interface is used to keep calling the loop method which should return the sleep time
 *
 * @author Everel
 */
public interface LoopTask {

    /**
     * @return sleepTime in ms
     */
    int loop();

}
