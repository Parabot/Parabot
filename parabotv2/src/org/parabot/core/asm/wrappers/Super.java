package org.parabot.core.asm.wrappers;

import org.parabot.core.asm.adapters.AddSuperAdapter;
import org.parabot.core.asm.interfaces.Injectable;
/**
 * 
 * @author Clisprail
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
