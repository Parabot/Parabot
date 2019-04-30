package org.parabot.core.asm.adapters;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.parabot.core.Core;

public class AddDebugAdapter {
    private ClassNode  owner;
    private MethodNode mn;

    public AddDebugAdapter(ClassNode owner, MethodNode mn) {
        this.owner = owner;
        this.mn = mn;
    }

    public AddDebugAdapter(MethodNode mn) {
        this.mn = mn;
    }

    @Override
    public String toString() {
        return "AddDebugAdapter{" +
                "owner=" + owner +
                ", mn=" + mn +
                '}';
    }

    public void inject() {
        Core.verbose("Injecting: " + this.toString());
        InsnList inject = new InsnList();
        Label    l0     = new Label();
        inject.add(new LabelNode(l0));

        String      callString = owner.name + "." + mn.name + " " + mn.desc;
        LdcInsnNode ldc        = new LdcInsnNode(callString);

        MethodInsnNode methodNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "org/parabot/core/Core", "debug",
                "(Ljava/lang/String;)V");

        inject.add(ldc);
        inject.add(methodNode);

        mn.instructions.insert(inject);

    }

}
