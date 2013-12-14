package org.parabot.environment.api.utils;

/**
 * A simple class to filter things out of an collection
 * 
 * @author Everel
 * 
 * @param <F>
 */
public interface Filter<F> {
	/**
	 * Determines if this object should be accepted
	 * @param f
	 * @return <b>true</b> to include this object, otherwise <b>false</b> to exclude.
	 */
	public boolean accept(F f);
}
