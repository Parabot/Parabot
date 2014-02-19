package org.parabot.environment.randoms;

/**
 * 
 * @author Everel
 *
 */
public interface Random {
	
	/**
	 * Determines whether this random should activate
	 * @return <b>true</b> if this random should activate
	 */
	public boolean activate();
	
	/**
	 * Executes this random
	 */
	public void execute();

}
