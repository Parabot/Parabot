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
    public boolean activate();

    /**
     * Executes this random
     */
    public void execute();

    /**
     * Returns the name of the random
     *
     * @return Name of the random
     */
    public String getName();

    /**
     * Returns the name of the server which the random is made for
     *
     * @return Name of the server
     */
    public String getServer();

    /**
     * Returns the RandomType of the random
     *
     * @return The RandomType of the random
     */
    public RandomType getRandomType();

}
