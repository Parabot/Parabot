package org.parabot.core.asm.wrappers;

import org.parabot.core.asm.adapters.AddInterfaceAdapter;
import org.parabot.core.asm.interfaces.Injectable;

/**
 * 
 * @author Clisprail
 *
 */
public class Interface implements Injectable {
	private String className;
	private String interfaceClass;
	
	public Interface(String className, String interfaceClass) {
		this.className = className;
		this.interfaceClass = interfaceClass;
	}
	
	/**
	 * Adds the interface to the class
	 * Short route for getAdapter#inject();
	 */
	@Override
	public void inject() {
		getAdapter().inject();
	}
	
	/**
	 * Gets the add interface adapter
	 * @return AddInterface adapter
	 */
	public AddInterfaceAdapter getAdapter() {
		return new AddInterfaceAdapter(className, interfaceClass);
	}
	
	@Override
	public String toString() {
		return String.format("%s implements %s%s", className, AddInterfaceAdapter.getAccessorPackage().replaceAll("/", "."), interfaceClass);
	}

}
