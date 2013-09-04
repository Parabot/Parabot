package org.parabot.core.asm.wrappers;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.parabot.core.asm.ASMUtils;
import org.parabot.core.asm.adapters.AddInvokerAdapter;
import org.parabot.core.asm.interfaces.Injectable;

/**
 * 
 * @author Everel
 *
 */
public class Invoker implements Injectable {
	private ClassNode into = null;
	private ClassNode methodLocation = null;
	private MethodNode mn = null;
	private String argsDesc = null;
	private String returnDesc = null;
	private String methodName = null;

	public Invoker(String methodLoc, String invMethName, String argsDesc,
			String returnDesc, String methodName) {
		this(methodLoc, methodLoc, invMethName, argsDesc, returnDesc,
				methodName);
	}

	public Invoker(String into, String methodLoc, String invMethName,
			String argsDesc, String returnDesc, String methodName) {
		this.into = ASMUtils.getClass(into);
		this.methodLocation = ASMUtils.getClass(methodLoc);
		this.mn = getMethod(this.methodLocation, invMethName, argsDesc);
		this.returnDesc = returnDesc;
		this.methodName = methodName;
		this.argsDesc = argsDesc;
	}

	private static MethodNode getMethod(ClassNode into, String name, String desc) {
		for (MethodNode m : into.methods) {
			String s = m.desc.substring(0, m.desc.indexOf(')') + 1);
			if (m.name.equals(name) && s.equals(desc)) {
				return m;
			}
		}
		return null;
	}

	/**
	 * Short route for getAdaptar().inject();
	 * 
	 * @see AddInvokerAdapter#inject
	 */
	@Override
	public void inject() {
		getAdapter().inject();
	}

	/**
	 * Gets the AddInvokerAdapter
	 * 
	 * @return AddInvokerAdapter
	 */
	public AddInvokerAdapter getAdapter() {
		return new AddInvokerAdapter(this.methodLocation, this.into, this.mn,
				this.argsDesc, this.returnDesc, this.methodName);
	}

}
