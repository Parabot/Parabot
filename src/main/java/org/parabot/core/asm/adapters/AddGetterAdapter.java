package org.parabot.core.asm.adapters;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.parabot.core.Core;
import org.parabot.core.asm.ASMUtils;
import org.parabot.core.asm.interfaces.Injectable;

import java.lang.reflect.Modifier;

/**
 * Adds a method into a Classnode which returns a field
 *
 * @author Everel
 */
public class AddGetterAdapter implements Opcodes, Injectable {
    private final ClassNode into;
    private final ClassNode fieldLocation;
    private final FieldNode fieldNode;
    private final String methodName;
    private final String returnDesc;
    private final boolean staticField;
    private final boolean staticMethod;
    private long multiplier;

    /**
     * @param into          - classnode to inject getter method in
     * @param fieldLocation - classnode where field is located
     * @param fieldNode     - field name to get
     * @param methodName    - method name of getter
     * @param returnDesc    - return type of method, can be null for default return
     * @param staticMethod  - pass true if you want the method to be static
     * @param multiplier    - if this field requires a multipli
     */
    public AddGetterAdapter(final ClassNode into,
                            final ClassNode fieldLocation, final FieldNode fieldNode,
                            final String methodName, final String returnDesc,
                            final boolean staticMethod, final long multiplier) {
        this.into = into;
        this.fieldLocation = fieldLocation;
        this.fieldNode = fieldNode;
        this.methodName = methodName;
        this.returnDesc = returnDesc == null ? fieldNode.desc : returnDesc;
        this.staticField = Modifier.isStatic(fieldNode.access);
        this.staticMethod = staticMethod;
        this.multiplier = multiplier;
    }

    /**
     * @param fieldLocation
     * @param fieldNode
     * @param methodName
     */
    public AddGetterAdapter(final ClassNode fieldLocation,
                            final FieldNode fieldNode, final String methodName) {
        this.into = fieldLocation;
        this.fieldLocation = fieldLocation;
        this.fieldNode = fieldNode;
        this.methodName = methodName;
        this.returnDesc = fieldNode.desc;
        this.staticField = Modifier.isStatic(fieldNode.access);
        this.staticMethod = false;
    }

    /**
     * Validates if this getter can be injected, if not a runtime exception is
     * thrown
     */
    public void validate() {
        if (methodName == null) {
            throw new RuntimeException("Null method name");
        }
        if (into == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Into ClassNode is null, at : ").append(methodName)
                    .append("()");
            throw new RuntimeException(sb.toString());
        }
        if (fieldNode == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("FieldLocation ClassNode is null, at : ")
                    .append(methodName).append("()");
            throw new RuntimeException(sb.toString());
        }
        if (fieldNode == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("FieldNode is null, at : ").append(methodName)
                    .append("()");
            throw new RuntimeException(sb.toString());
        }
        for (final Object methodNode : into.methods) {
            MethodNode methodNodeObject = (MethodNode) methodNode;
            if (methodNodeObject.name.equals(methodName)) {
                final Type[] args = Type.getArgumentTypes(methodNodeObject.desc);
                if (args != null && args.length != 0) {
                    continue;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Duplicated method detected. ").append(methodName)
                        .append("() in ").append(into.name);
                throw new RuntimeException(sb.toString());
            }
        }
    }

    /**
     * Injects this the method getter
     */
    @Override
    public void inject() {
        Core.verbose("Injecting: " + this.toString());

        MethodNode method = new MethodNode(ACC_PUBLIC
                | (staticMethod ? ACC_STATIC : 0), methodName, "()"
                + returnDesc, null, null);
        if (!staticField) {
            method.visitVarInsn(ALOAD, 0);
        }
        if (staticField) {
            ASMUtils.makePublic(fieldNode);
        }
        method.visitFieldInsn(staticField ? GETSTATIC : GETFIELD,
                fieldLocation.name, fieldNode.name, fieldNode.desc);
        if (!fieldNode.desc.equals(returnDesc)) {
            if (returnDesc.contains("L")) {
                if (!returnDesc.contains("[")) {
                    method.visitTypeInsn(CHECKCAST,
                            returnDesc.replaceFirst("L", "")
                                    .replaceAll(";", ""));
                } else {
                    method.visitTypeInsn(CHECKCAST, returnDesc);
                }
            }
        }

        if (multiplier != 0) {
            if (fieldNode.desc.equals("I") || fieldNode.desc.equals("S")) {
                method.visitInsn(I2L);
            }
            method.visitLdcInsn(new Long(multiplier));
            method.visitInsn(LMUL);
            if (returnDesc.equals("I") || returnDesc.equals("S")) {
                method.visitInsn(L2I);
            }
            if (returnDesc.equals("S")) {
                method.visitInsn(I2S);
            }
        } else if (fieldNode.desc.equals("J") && returnDesc.equals("I")) {
            method.visitInsn(L2I);
        } else if (fieldNode.desc.equals("I") && returnDesc.equals("J")) {
            method.visitInsn(I2L);
        }

        method.visitInsn(ASMUtils.getReturnOpcode(returnDesc));
        method.visitMaxs(1, 1);
        into.methods.add(method);
    }

    @Override
    public String toString() {
        return new StringBuilder("[Injectable: getter, into classname: ")
                .append(into.name).append(", field classname: ")
                .append(fieldLocation.name).append(", field name: ")
                .append(fieldNode.name).append(", field desc: ")
                .append(fieldNode.desc).append(", method name: ")
                .append(methodName).append(", return desc: ")
                .append(returnDesc).append(", static method: ")
                .append(staticMethod).append(", static field: ")
                .append(staticField).append("]").toString();
    }
}