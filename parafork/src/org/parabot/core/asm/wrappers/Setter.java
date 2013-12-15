package org.parabot.core.asm.wrappers;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.parabot.core.asm.ASMUtils;
import org.parabot.core.asm.adapters.AddSetterAdapter;
import org.parabot.core.asm.interfaces.Injectable;

/**
 * 
 * This class is used for injecting a setter for a specific field
 * 
 * @author Everel
 *
 */
public class Setter implements Injectable {
	private ClassNode fieldLocation = null;
	private ClassNode into = null;
	private FieldNode field = null;
	private String name = null;
	private String desc = null;
	private boolean methodStatic = false;
	
	public Setter(final String fieldLocation, String into, final String fieldName, final String methodName, final String desc, final boolean methodStatic) {
		this.fieldLocation = ASMUtils.getClass(fieldLocation);
		into = (into == null) ? fieldLocation : into;
		this.into = ASMUtils.getClass(into);
		this.field = ASMUtils.getField(this.fieldLocation, fieldName);
		this.name = methodName;
		this.desc = (desc == null) ? this.field.desc : desc;
		this.methodStatic = methodStatic;
	}
	
	public Setter(final String fieldLocation, final String fieldName, final String methodName) {
		this(fieldLocation, null, fieldName, methodName, null, false);
	}
	
	/**
	 * Short route for getAdaptar().inject();
	 * @see AddSetterAdapter#inject
	 */
	@Override
	public void inject() {
		getAdapter().inject();
	}
	
	/**
	 * Gets the AddGetterAdapter
	 * @return AddGetterAdapter
	 */
	public AddSetterAdapter getAdapter() {
		return new AddSetterAdapter(fieldLocation, into, field, name, desc, methodStatic);
	}

}
