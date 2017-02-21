package org.parabot.environment.randoms;

/**
 * @author JKetelaar
 */
public interface Random {

    /**
     * Determines whether this random should activate
     *
     * @return <b>true</b> if this random should be activate
     */
    boolean activate();

    /**
     * Executes this random
     */
    void execute();

    /**
     * Returns the name of the random
     *
     * @return Name of the random
     */
    String getName();

    /**
     * Returns the name of the server which the random is made for
     *
     * @return Name of the server
     */
    String getServer();

    /**
     * Returns the RandomType of the random
     *
     * @return The RandomType of the random
     */
    RandomType getRandomType();

}
