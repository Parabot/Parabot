package org.parabot.core.asm.wrappers;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.parabot.core.asm.ASMUtils;
import org.parabot.core.asm.adapters.AddInvokerAdapter;
import org.parabot.core.asm.interfaces.Injectable;

/**
 * 
 * This class is used for injecting an invoker into a methodnode
 * 
 * @author Everel
 *
 */
public class Invoker implements Injectable {
	private ClassNode into;
	private ClassNode methodLocation;
	private MethodNode mn;
	private String argsDesc;
	private String returnDesc;
	private String methodName;
	private boolean isInterface;
	private String instanceCast;
	private String argsCheckCastDesc;
	
	private String mName;
	private String mDesc;

	public Invoker(String methodLoc, String invMethName, String argsDesc,
			String returnDesc, String methodName) {
		this(methodLoc, methodLoc, invMethName, argsDesc, returnDesc,
				methodName, false, null, null);
	}

	public Invoker(String into, String methodLoc, String invMethName,
			String argsDesc, String returnDesc, String methodName, boolean isInterface, String instanceCast, String argsCheckCastDesc) {
		this.into = ASMUtils.getClass(into);
		this.methodLocation = ASMUtils.getClass(methodLoc);
		this.mn = getMethod(this.methodLocation, invMethName, argsDesc);
		this.returnDesc = returnDesc;
		this.methodName = methodName;
		this.argsDesc = argsDesc;
		this.isInterface = isInterface;
		this.instanceCast = instanceCast;
		
		this.mName = invMethName;
		this.mDesc = argsDesc + returnDesc;
		this.argsCheckCastDesc = argsCheckCastDesc;
	}

	private static MethodNode getMethod(ClassNode into, String name, String desc) {
		for (Object m : into.methods) {
			MethodNode methodNode = (MethodNode) m;
			String s = methodNode.desc.substring(0, methodNode.desc.indexOf(')') + 1);
			if (methodNode.name.equals(name) && s.equals(desc)) {
				return methodNode;
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
		return new AddInvokerAdapter(this.methodLocation, this.into, this.mn, this.mName, this.mDesc,
				this.argsDesc, this.returnDesc, this.methodName, this.isInterface, this.instanceCast, this.argsCheckCastDesc);
	}

}
