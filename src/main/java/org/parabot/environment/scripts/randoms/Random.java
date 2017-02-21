package org.parabot.environment.scripts.randoms;

/**
 * @author Everel
 * @see org.parabot.environment.randoms.Random
 * @deprecated
 */
@Deprecated
public interface Random {

    /**
     * Determines whether this random should activate
     *
     * @return <b>true</b> if this random should activate
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

}
