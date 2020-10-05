package org.parabot.core.asm.adapters;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.parabot.core.asm.ASMUtils;
import org.parabot.core.asm.interfaces.Injectable;

import java.lang.reflect.Modifier;

/**
 * Injects a method which invokes an other method
 *
 * @author Everel
 */
public class AddInvokerAdapter implements Opcodes, Injectable {
    private final ClassNode into;
    private final ClassNode methodLocation;
    private final MethodNode mn;
    private final String argsDesc;
    private final String returnDesc;
    private final String methodName;
    private final boolean isInterface;
    private final String instanceCast;
    private final String mName;
    private final String mDesc;
    private final String argsCheckCast;

    private boolean isStatic;

    public AddInvokerAdapter(final ClassNode methodLocation,
                             final ClassNode into, final MethodNode mn, final String mName, final String mDesc, final String argsDesc,
                             final String returnDesc, final String methodName,
                             boolean isInterface, String instanceCast, String argsCheckCastDesc) {
        this.into = into;
        this.methodLocation = methodLocation;
        this.mName = mName;
        this.mDesc = mDesc;
        this.mn = mn;
        this.argsDesc = argsDesc;
        this.returnDesc = returnDesc;
        this.methodName = methodName;
        this.isInterface = isInterface;
        this.instanceCast = instanceCast;
        this.argsCheckCast = argsCheckCastDesc;

    }

    @Override
    public void inject() {
        String mArgsDesc = argsCheckCast == null ? this.argsDesc : this.argsCheckCast;

        MethodNode m = new MethodNode(ACC_PUBLIC, this.methodName,
                mArgsDesc + this.returnDesc, null, null);

        if (!isInterface) {
            isStatic = (this.mn.access & ACC_STATIC) != 0;

            if (!Modifier.isPublic(mn.access)) {
                if (Modifier.isPrivate(mn.access)) {
                    mn.access = mn.access & (~ACC_PRIVATE);
                }
                if (Modifier.isProtected(mn.access)) {
                    mn.access = mn.access & (~ACC_PROTECTED);
                }
                mn.access = mn.access | ACC_PUBLIC;
                //mn.access = mn.access | ACC_SYNCHRONIZED;
            }
        }

        if (!isStatic || isInterface) {
            m.visitVarInsn(ALOAD, 0);
        }

        if (instanceCast != null) {
            m.visitTypeInsn(CHECKCAST, instanceCast);
        }

        if (!this.argsDesc.equals("()")) {
            Type[] castArgs = argsCheckCast == null ? null : Type.getArgumentTypes(argsCheckCast + "V");
            Type[] methodArgs = Type.getArgumentTypes(argsDesc + "V");

            for (int i = 0; i < methodArgs.length; i++) {
                m.visitVarInsn(ASMUtils.getLoadOpcode(methodArgs[i].getDescriptor()), i + 1);
                if (castArgs != null && !castArgs[i].getDescriptor().equals(methodArgs[i].getDescriptor())) {
                    String cast = methodArgs[i].getDescriptor();
                    if (cast.startsWith("L")) {
                        cast = cast.substring(1).replace(";", "");
                    }
                    m.visitTypeInsn(CHECKCAST, cast);
                }
            }
        }

        if (isInterface) {
            m.visitMethodInsn(INVOKEINTERFACE, instanceCast, mName, mDesc, true);
        } else {
            m.visitMethodInsn(isStatic ? INVOKESTATIC : INVOKEVIRTUAL, methodLocation.name, mn.name, mn.desc, false);
        }
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
