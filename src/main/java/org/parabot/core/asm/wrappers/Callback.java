package org.parabot.core.asm.wrappers;

import org.objectweb.asm.tree.MethodNode;
import org.parabot.core.asm.ASMUtils;
import org.parabot.core.asm.adapters.AddCallbackAdapter;
import org.parabot.core.asm.interfaces.Injectable;

/**
 * This class is used for injecting a callback into a methodnode
 *
 * @author Everel
 */
public class Callback implements Injectable {
    private final MethodNode method;
    private final String invokeClass;
    private final String invokeMethod;
    private final String desc;
    private final boolean conditional;
    private int[] args;

    public Callback(final String className, final String methodName,
                    final String methodDesc, final String callbackClass,
                    final String callbackMethod, final String callbackDesc, String args, final boolean conditional) {
        this.method = ASMUtils.getMethod(className, methodName, methodDesc);
        this.invokeClass = callbackClass;
        this.invokeMethod = callbackMethod;
        this.desc = callbackDesc;
        this.conditional = conditional;
        if (args.length() > 0) {
            if (args.contains(",")) {
                final String[] strArgs = args.split(",");
                this.args = new int[strArgs.length];
                for (int i = 0; i < this.args.length; i++) {
                    this.args[i] = Integer.parseInt(strArgs[i]);
                }
            } else {
                this.args = new int[]{ Integer.parseInt(args) };
            }
        }
    }

    @Override
    public void inject() {
        getAdapter().inject();
    }

    public AddCallbackAdapter getAdapter() {
        return new AddCallbackAdapter(this.method, this.invokeClass,
                this.invokeMethod, this.desc, this.args, this.conditional);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Injectable type: Callback");

        if (method != null) {
            sb.append(", intercepts method: ").append(method.name);
        }

        sb.append(", calls class: ").append(invokeClass)
                .append(", calls method: ").append(invokeMethod);

        return sb.toString();
    }
}
