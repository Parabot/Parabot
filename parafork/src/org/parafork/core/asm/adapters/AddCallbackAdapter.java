package org.parafork.core.asm.adapters;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.parafork.core.asm.ASMUtils;
import org.parafork.core.asm.interfaces.Injectable;

/**
 * 
 * Injects a callback, invokes a given static method
 * 
 * @author Everel
 * 
 */
public class AddCallbackAdapter implements Injectable, Opcodes {
	private MethodNode method;
	private String invokeClass;
	private String invokeMethod;
	private String desc;
	private int[] args;

	public AddCallbackAdapter(final MethodNode method,
			final String invokeClass, final String invokeMethod,
			final String desc, final int[] args) {
		this.method = method;
		this.invokeClass = invokeClass;
		this.invokeMethod = invokeMethod;
		this.desc = desc;
		this.args = args;
	}

	@Override
	public void inject() {
		final Type[] types = Type.getArgumentTypes(this.method.desc);
		InsnList inject = new InsnList();
		Label l0 = new Label();
		inject.add(new LabelNode(l0));
		for (int arg : args) {
			inject.add(new VarInsnNode(ASMUtils.getLoadOpcode(types[arg - 1]
					.getDescriptor()), arg));
		}
		inject.add(new MethodInsnNode(INVOKESTATIC,
				this.invokeClass, this.invokeMethod,
				this.desc));
		this.method.instructions.insert(inject);
	}

}
