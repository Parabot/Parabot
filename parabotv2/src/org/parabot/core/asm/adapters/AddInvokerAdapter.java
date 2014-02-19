package org.parabot.core.asm.adapters;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.parabot.core.asm.ASMUtils;
import org.parabot.core.asm.interfaces.Injectable;

/**
 * 
 * Injects a method which invokes an other method
 * 
 * @author Everel
 * 
 */
public class AddInvokerAdapter implements Opcodes, Injectable {
	private ClassNode into;
	private ClassNode methodLocation;
	private MethodNode mn;
	private String argsDesc;
	private String returnDesc;
	private String methodName;

	public AddInvokerAdapter(final ClassNode methodLocation,
			final ClassNode into, final MethodNode mn, final String argsDesc,
			final String returnDesc, final String methodName) {
		this.into = into;
		this.methodLocation = methodLocation;
		this.mn = mn;
		this.argsDesc = argsDesc;
		this.returnDesc = returnDesc;
		this.methodName = methodName;
	}

	@Override
	public void inject() {
		MethodNode m = new MethodNode(ACC_PUBLIC, this.methodName,
				this.argsDesc + this.returnDesc, null, null);

		boolean isStatic = (this.mn.access & ACC_STATIC) != 0;

		if (!isStatic)
			m.visitVarInsn(ALOAD, 0);

		if (!this.argsDesc.equals("()"))
			for (int i = 1; i < this.argsDesc.length() - 1; i++)
				m.visitVarInsn(ASMUtils.getLoadOpcode(this.argsDesc.substring(
						i, i + 1)), i);

		m.visitMethodInsn(isStatic ? INVOKESTATIC : INVOKEVIRTUAL,
				methodLocation.name, mn.name, mn.desc);
		if (this.returnDesc.contains("L")) {
			if (!this.returnDesc.contains("[")) {
				m.visitTypeInsn(CHECKCAST, this.returnDesc
						.replaceFirst("L", "").replaceAll(";", ""));
			} else {
				m.visitTypeInsn(CHECKCAST, this.returnDesc);
			}
		}

		m.visitInsn(ASMUtils.getReturnOpcode(this.returnDesc));
		m.visitMaxs(0, 0);
		this.into.methods.add(m);
	}

}
