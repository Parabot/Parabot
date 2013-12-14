package org.parafork.core.asm.wrappers;

import org.parafork.core.asm.adapters.AddSuperAdapter;
import org.parafork.core.asm.interfaces.Injectable;
/**
 * 
 * This class is used for changing the super class of a class
 * 
 * @author Everel
 *
 */
public class Super implements Injectable {
	private String className = null;
	private String superClassName = null;
	
	public Super(String className, String superClassName) {
		this.className = className;
		this.superClassName = superClassName;
	}

	/**
	 * Adds a superclass to a class
	 * Short route for getAdapter().inject
	 */
	@Override
	public void inject() {
		getAdapter().inject();
	}
	
	public AddSuperAdapter getAdapter() {
		return new AddSuperAdapter(className, superClassName);
	}

}
