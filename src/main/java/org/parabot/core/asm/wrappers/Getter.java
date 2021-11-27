package org.parabot.core.asm.wrappers;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.parabot.core.Core;
import org.parabot.core.asm.ASMUtils;
import org.parabot.core.asm.adapters.AddGetterAdapter;
import org.parabot.core.asm.interfaces.Injectable;

/**
 * This class injects a getter which gets a specific field
 *
 * @author Everel
 */
public class Getter implements Injectable {
    private final ClassNode into;
    private final ClassNode fieldLocation;
    private final FieldNode fieldNode;
    private final String methodName;
    private final String returnDesc;
    private final boolean staticMethod;
    private long multiplier;

    /**
     * @param into          - classnode to inject getter method in
     * @param fieldLocation - classnode where field is located
     * @param fieldNode     - field name to get
     * @param methodName    - method name of getter
     * @param returnDesc    - return type of method, can be null for default return
     * @param staticMethod  - pass true if you want the method to be static
     * @param multiplier    - if there is one, otherwise 0L
     * @param fieldDesc     - desc of the field, null if there are no duplicate field names
     */
    public Getter(final String into, final String fieldLocation, final String fieldNode,
                  final String methodName, final String returnDesc, final boolean staticMethod, final long multiplier,
                  final String fieldDesc) {
        this.into = ASMUtils.getClass(into);
        this.fieldLocation = ASMUtils.getClass(fieldLocation);
        this.fieldNode = ASMUtils.getField(ASMUtils.getClass(fieldLocation), fieldNode, fieldDesc);
        this.methodName = methodName;
        this.returnDesc = (returnDesc == null && this.fieldNode != null) ? this.fieldNode.desc : returnDesc;
        this.staticMethod = staticMethod;
        this.multiplier = multiplier;
        Core.verbose(methodName + "[" + fieldLocation + "." + fieldNode + "]");
    }

    /**
     * @param fieldLocation
     * @param fieldNode
     * @param methodName
     */
    public Getter(final String fieldLocation, final String fieldNode, final String methodName) {
        this.into = ASMUtils.getClass(fieldLocation);
        this.fieldLocation = this.into;
        this.fieldNode = ASMUtils.getField(this.into, fieldNode);
        this.methodName = methodName;
        this.returnDesc = this.fieldNode.desc;
        this.staticMethod = false;
    }

    /**
     * Short route for getAdaptar().inject();
     *
     * @see AddGetterAdapter#inject
     */
    @Override
    public void inject() {
        getAdapter().inject();
    }

    /**
     * Gets the AddGetterAdapter
     *
     * @return AddGetterAdapter
     */
    public AddGetterAdapter getAdapter() {
        return new AddGetterAdapter(into, fieldLocation, fieldNode, methodName, returnDesc, staticMethod, multiplier);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Injectable type: Getter");

        if (fieldLocation.interfaces.size() > 0) {
            sb.append(", accessor type: ").append(fieldLocation.interfaces.get(0).toString().replace('/', '.'));
        }

        if (fieldNode != null) {
            sb.append(", field: ").append(fieldNode.name);
        }

        sb.append(", method name: ").append(methodName);

        return sb.toString();
    }
}
