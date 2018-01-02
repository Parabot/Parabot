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

}
