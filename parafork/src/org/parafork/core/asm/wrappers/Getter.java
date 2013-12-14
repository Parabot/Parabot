package org.parafork.core.asm.wrappers;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.parafork.core.asm.ASMUtils;
import org.parafork.core.asm.adapters.AddGetterAdapter;
import org.parafork.core.asm.interfaces.Injectable;

/**
 * 
 * This class injects a getter which gets a specific field
 * 
 * @author Everel
 *
 */
public class Getter implements Injectable {

	private ClassNode into = null;
	private ClassNode fieldLocation = null;
	private FieldNode fieldNode = null;
	private String methodName = null;
	private String returnDesc = null;
	private boolean staticMethod = false;
	
	/**
	 * 
	 * @param into - classnode to inject getter method in
	 * @param fieldLocation - classnode where field is located
	 * @param fieldName - field name to get
	 * @param methodName - method name of getter
	 * @param returnDesc - return type of method, can be null for default return
	 * @param staticMethod - pass true if you want the method to be static
	 */
	public Getter(final String into, final String fieldLocation, final String fieldNode,
			final String methodName, final String returnDesc, final boolean staticMethod) {
		this.into = ASMUtils.getClass(into);
		this.fieldLocation = ASMUtils.getClass(fieldLocation);
		this.fieldNode = ASMUtils.getField(ASMUtils.getClass(fieldLocation), fieldNode);
		this.methodName = methodName;
		this.returnDesc = returnDesc == null ? this.fieldNode.desc : returnDesc;
		this.staticMethod = staticMethod;
	}
	
	/**
	 * 
	 * @param fieldLocation
	 * @param fieldNode
	 * @param methodName
	 */
	public Getter(final String fieldLocation, final String fieldNode, final String methodName) {
		this.into = ASMUtils.getClass(fieldLocation);
		this.fieldLocation = this.into;
		this.fieldNode = ASMUtils.getField(this.into, fieldNode);
		this.methodName = methodName;
		this.returnDesc = this.fieldNode.desc;
		this.staticMethod = false;
	}
	
	/**
	 * Short route for getAdaptar().inject();
	 * @see AddGetterAdapter#inject
	 */
	@Override
	public void inject() {
		getAdapter().inject();
	}
	
	/**
	 * Gets the AddGetterAdapter
	 * @return AddGetterAdapter
	 */
	public AddGetterAdapter getAdapter() {
		return new AddGetterAdapter(into, fieldLocation, fieldNode, methodName, returnDesc, staticMethod);
	}

}
