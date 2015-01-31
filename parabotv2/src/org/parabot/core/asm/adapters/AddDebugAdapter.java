package org.parabot.core.asm.adapters;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class AddDebugAdapter {
	private MethodNode mn;
	
	public AddDebugAdapter(MethodNode mn) {
		this.mn = mn;
	}
	
	public void inject() {
		
		int i = 20;
		for(AbstractInsnNode node : mn.instructions.toArray().clone()) {
			if(node.getType() == AbstractInsnNode.METHOD_INSN || node.getOpcode() == Opcodes.PUTFIELD || node.getOpcode() == Opcodes.ASTORE || node.getOpcode() == Opcodes.ISTORE) {
				i++;
				InsnList inject = new InsnList();
				inject.add(new IntInsnNode(Opcodes.BIPUSH, i));
				inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
					"org/parabot/core/Core", "debug",
					"(I)V"));
				mn.instructions.insertBefore(node.getNext(), inject);
			}
		}
	}

}
