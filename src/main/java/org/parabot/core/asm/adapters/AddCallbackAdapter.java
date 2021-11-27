package org.parabot.core.asm.adapters;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.parabot.core.asm.ASMUtils;
import org.parabot.core.asm.interfaces.Injectable;

import java.lang.reflect.Modifier;

/**
 * Injects a callback, invokes a given static method
 *
 * @author Everel
 */
public class AddCallbackAdapter implements Injectable, Opcodes {
    private final MethodNode method;
    private final String invokeClass;
    private final String invokeMethod;
    private final String desc;
    private final int[] args;
    private final boolean conditional;

    public AddCallbackAdapter(final MethodNode method,
                              final String invokeClass, final String invokeMethod,
                              final String desc, final int[] args, final boolean conditional) {
        this.method = method;
        this.invokeClass = invokeClass;
        this.invokeMethod = invokeMethod;
        this.desc = desc;
        this.args = args;
        this.conditional = conditional;
    }

    @Override
    public void inject() {
        final Type[] types = Type.getArgumentTypes(this.method.desc);
        InsnList inject = new InsnList();
        Label l0 = new Label();
        inject.add(new LabelNode(l0));
        int offset = 0;
        if (args != null) {
            for (int arg : args) {
                if (Modifier.isStatic(method.access)) {
                    int loadOpcode = ASMUtils.getLoadOpcode(types[arg]
                            .getDescriptor());
                    inject.add(new VarInsnNode(loadOpcode, arg + offset));
                    if (loadOpcode == Opcodes.LLOAD) {
                        offset++;
                    }
                } else {
                    inject.add(new VarInsnNode(ASMUtils.getLoadOpcode(types[arg]
                            .getDescriptor()), arg + 1));
                }
            }
        }
        inject.add(new MethodInsnNode(INVOKESTATIC,
                this.invokeClass, this.invokeMethod,
                this.desc, false));
        if (this.conditional) {
            LabelNode ln = new LabelNode(new Label());
            inject.add(new JumpInsnNode(IFEQ, ln));
            if (Type.getReturnType(method.desc).equals(Type.BOOLEAN_TYPE)) {
                inject.add(new InsnNode(ICONST_1));
                inject.add(new InsnNode(IRETURN));
            } else {
                inject.add(new InsnNode(RETURN));
            }
            inject.add(ln);
        }

        if (method.name.startsWith("<") && !Modifier.isStatic(method.access)) {
            // find target
            AbstractInsnNode target = null;
            for (AbstractInsnNode node : this.method.instructions.toArray()) {
                if (node.getOpcode() == Opcodes.INVOKESPECIAL) {
                    target = node;
                    break;
                }
            }

            if (target != null) {
                this.method.instructions.insert(target, inject);
            }
        } else {
            this.method.instructions.insert(inject);
        }
    }

}
