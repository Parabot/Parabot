package org.parabot.environment.api.utils;

/**
 * A simple class to filter things
 * 
 * @author Everel
 * 
 * @param <F>
 */
public interface Filter<F>
{
	public boolean accept(F f);
}

