package org.parabot.core.asm.wrappers;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.parabot.core.asm.ASMUtils;
import org.parabot.core.asm.adapters.AddInvokerAdapter;
import org.parabot.core.asm.interfaces.Injectable;

/**
 * This class is used for injecting an invoker into a methodnode
 *
 * @author Everel
 */
public class Invoker implements Injectable {
    private final ClassNode into;
    private final ClassNode methodLocation;
    private final MethodNode mn;
    private final String argsDesc;
    private final String returnDesc;
    private final String methodName;
    private final boolean isInterface;
    private final String instanceCast;
    private final String argsCheckCastDesc;

    private final String mName;
    private final String mDesc;

    public Invoker(String methodLoc, String invMethName, String argsDesc,
                   String returnDesc, String methodName) {
        this(methodLoc, methodLoc, invMethName, argsDesc, returnDesc,
                methodName, false, null, null);
    }

    public Invoker(String into, String methodLoc, String invMethName,
                   String argsDesc, String returnDesc, String methodName, boolean isInterface, String instanceCast, String argsCheckCastDesc) {
        this.into = ASMUtils.getClass(into);
        this.methodLocation = ASMUtils.getClass(methodLoc);
        this.mn = getMethod(this.methodLocation, invMethName, argsDesc, returnDesc);
        this.returnDesc = returnDesc;
        this.methodName = methodName;
        this.argsDesc = argsDesc;
        this.isInterface = isInterface;
        this.instanceCast = instanceCast;

        this.mName = invMethName;
        this.mDesc = argsDesc + returnDesc;
        this.argsCheckCastDesc = argsCheckCastDesc;
    }

    /**
     * Short route for getAdaptar().inject();
     *
     * @see AddInvokerAdapter#inject
     */
    @Override
    public void inject() {
        getAdapter().inject();
    }

    /**
     * Gets the AddInvokerAdapter
     *
     * @return AddInvokerAdapter
     */
    public AddInvokerAdapter getAdapter() {
        return new AddInvokerAdapter(this.methodLocation, this.into, this.mn, this.mName, this.mDesc,
                this.argsDesc, this.returnDesc, this.methodName, this.isInterface, this.instanceCast, this.argsCheckCastDesc);
    }

    @Override
    public String toString() {
        return String.format("Injectable type: Invoker, accessor: %s, method name: %s, invokes method: %s", methodLocation.name, methodName, mName);
    }

    private static MethodNode getMethod(ClassNode into, String name, String argsDesc, String returnDesc) {
        for (Object method : into.methods) {
            MethodNode m = (MethodNode) method;
            if (m.name.equals(name) && m.desc.substring(0, m.desc.indexOf(')') + 1).equals(argsDesc)
                    && (returnDesc == null || Type.getType(m.desc).getReturnType().getDescriptor().equals(returnDesc))) {
                return m;
            }
        }
        return null;
    }
}
