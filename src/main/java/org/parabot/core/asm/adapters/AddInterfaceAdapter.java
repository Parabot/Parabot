package org.parabot.core.asm.adapters;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.parabot.core.Core;
import org.parabot.core.asm.ASMUtils;
import org.parabot.core.asm.interfaces.Injectable;

/**
 * This class appends an interface to a class
 *
 * @author Everel
 */
public class AddInterfaceAdapter implements Injectable {
    private static String accessorPackage;

    private final ClassNode node;
    private final String interfaceClass;

    public AddInterfaceAdapter(ClassNode node, String interfaceClass) {
        this.node = node;
        this.interfaceClass = interfaceClass;
    }

    public AddInterfaceAdapter(String className, String interfaceClass) {
        this.node = ASMUtils.getClass(className);
        this.interfaceClass = interfaceClass;
    }

    public static String getAccessorPackage() {
        return accessorPackage;
    }

    public static void setAccessorPackage(String packageName) {
        accessorPackage = packageName;
    }

    @Override
    public void inject() {
        Core.verbose("Injecting: " + this.toString());
        addInterface(node, accessorPackage + interfaceClass);
    }

    @Override
    public String toString() {
        return new StringBuilder("[Injectable: interface, into classname: ").append(node.name).append(", interface: ").append(accessorPackage).append(interfaceClass).append("]").toString();
    }

    protected static void addInterface(ClassNode node, String i) {
        ASMUtils.makePublic(node);
        for (Object mn : node.methods) {
            MethodNode methodNode = (MethodNode) mn;
            if (methodNode.name.startsWith("<init")) {
                ASMUtils.makePublic(methodNode);
            }
        }
        node.interfaces.add(i);
    }
}
