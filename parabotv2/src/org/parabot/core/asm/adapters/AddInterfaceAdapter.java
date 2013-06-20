package org.parabot.core.asm.adapters;

import org.objectweb.asm.tree.ClassNode;
import org.parabot.core.asm.ASMUtils;
import org.parabot.core.asm.interfaces.Injectable;

/**
 * 
 * @author Clisprail
 * 
 */
public class AddInterfaceAdapter implements Injectable {

	private static String accessorPackage = null;
	private ClassNode node = null;
	private String interfaceClass = null;

	public AddInterfaceAdapter(ClassNode node, String interfaceClass) {
		this.node = node;
		this.interfaceClass = interfaceClass;
	}

	public AddInterfaceAdapter(String className, String interfaceClass) {
		this.node = ASMUtils.getClass(className);
		this.interfaceClass = interfaceClass;
	}

	public static void setAccessorPackage(String packageName) {
		accessorPackage = packageName;
	}

	public static String getAccessorPackage() {
		return accessorPackage;
	}

	@Override
	public void inject() {
		addInterface(node, accessorPackage + interfaceClass);
	}

	protected static void addInterface(ClassNode cg, String i) {
		System.out.println(" ^ " + cg.name + " implements " + i);
		cg.interfaces.add(i);
	}
}
