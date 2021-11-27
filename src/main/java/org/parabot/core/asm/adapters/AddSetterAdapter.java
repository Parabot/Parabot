package org.parabot.core.asm.adapters;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.parabot.core.Core;
import org.parabot.core.asm.interfaces.Injectable;

/**
 * Injects methods which sets a field
 *
 * @author Everel
 */
public class AddSetterAdapter implements Opcodes, Injectable {
    private final ClassNode fieldLocation;
    private final ClassNode into;
    private final FieldNode field;
    private final String name;
    private final String desc;
    private final boolean methodStatic;

    public AddSetterAdapter(ClassNode fieldLocation, ClassNode into,
                            FieldNode field, String name, String desc, boolean methodStatic) {
        this.fieldLocation = fieldLocation;
        this.into = into;
        this.field = field;
        this.name = name;
        this.desc = desc;
        this.methodStatic = methodStatic;
    }

    public AddSetterAdapter(ClassNode fieldLocation, ClassNode into,
                            FieldNode field, String name, String desc) {
        this(fieldLocation, into, field, name, desc, false);
    }

    /**
     * Injects the setter
     */
    @Override
    public void inject() {
        Core.verbose("Injecting: " + this.toString());
        addSetter(fieldLocation, into, field, name, desc, methodStatic);
    }

    @Override
    public String toString() {
        return new StringBuilder("[Injectable: setter, into classname: ")
                .append(into.name).append(", field classname: ")
                .append(fieldLocation.name).append(", field name: ")
                .append(field.name).append(", field desc: ").append(field.desc)
                .append(", method name: ").append(name)
                .append(", method desc: ").append(desc)
                .append(", static method: ").append(methodStatic).append("]")
                .toString();
    }

    private static void addSetter(ClassNode fieldLocation, ClassNode into,
                                  FieldNode field, String name, String desc, boolean methodStatic) {
        if (desc.contains("L") && !desc.endsWith("Ljava/lang/String;")) {
            desc = "Ljava/lang/Object;";
        }
        MethodNode method = new MethodNode(ACC_PUBLIC
                | (methodStatic ? ACC_STATIC : 0), name, "(" + desc + ")V",
                null, null);
        boolean isStatic = (field.access & ACC_STATIC) > 0;
        if (!isStatic) {
            method.visitVarInsn(ALOAD, 0);
        }
        if (desc.equals("I")) {
            method.visitVarInsn(ILOAD, 1);
        } else if (desc.equals("J")) {
            method.visitVarInsn(Opcodes.LLOAD, 1);
        } else {
            method.visitVarInsn(ALOAD, 1);
        }
        method.visitFieldInsn(isStatic ? PUTSTATIC : PUTFIELD,
                fieldLocation.name, field.name, field.desc);
        method.visitInsn(RETURN);
        method.visitMaxs(2, 2);
        into.methods.add(method);
    }

}
