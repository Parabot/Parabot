package org.parabot.core.asm.adapters;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.parabot.core.Core;
import org.parabot.core.asm.ASMUtils;
import org.parabot.core.asm.interfaces.Injectable;

import java.util.ListIterator;

/**
 * This class is used for changing the super class of a class
 *
 * @author Everel
 */
public class AddSuperAdapter implements Injectable {
    private final ClassNode node;
    private final String superClass;

    public AddSuperAdapter(final ClassNode node, final String superClass) {
        this.node = node;
        this.superClass = superClass;
    }

    public AddSuperAdapter(final String className, final String superClass) {
        this.node = ASMUtils.getClass(className);
        this.superClass = superClass;
    }

    @Override
    public void inject() {
        Core.verbose("Injecting: " + this.toString());
        setSuper(node, superClass);
    }

    @Override
    public String toString() {
        return new StringBuilder("[Injectable: super, class name: ")
                .append(node.name).append(", super: ").append(superClass)
                .append("]").toString();
    }

    private static final void setSuper(final ClassNode node,
                                       final String superClass) {
        ListIterator<?> mli = node.methods.listIterator();
        while (mli.hasNext()) {
            MethodNode mn = (MethodNode) mli.next();
            if (mn.name.equals("<init>")) {
                ListIterator<?> ili = mn.instructions.iterator();
                while (ili.hasNext()) {
                    AbstractInsnNode ain = (AbstractInsnNode) ili.next();
                    if (ain.getOpcode() == Opcodes.INVOKESPECIAL) {
                        MethodInsnNode min = (MethodInsnNode) ain;
                        if (!min.owner.equals(node.name)) {
                            min.owner = superClass;
                        }
                        break;
                    }
                }
            }
        }
        node.superName = superClass;
    }

}
