package org.parabot.core.asm.adapters;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class AddDebugAdapter {
    private final MethodNode mn;
    private ClassNode owner;

    public AddDebugAdapter(ClassNode owner, MethodNode mn) {
        this.owner = owner;
        this.mn = mn;
    }

    public AddDebugAdapter(MethodNode mn) {
        this.mn = mn;
    }

    public void inject() {
        InsnList inject = new InsnList();
        Label l0 = new Label();
        inject.add(new LabelNode(l0));

        String callString = owner.name + "." + mn.name + " " + mn.desc;
        LdcInsnNode ldc = new LdcInsnNode(callString);

        MethodInsnNode methodNode = new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "org/parabot/core/Core",
                "debug",
                "(Ljava/lang/String;)V",
                false
        );

        inject.add(ldc);
        inject.add(methodNode);

        mn.instructions.insert(inject);

    }

}
