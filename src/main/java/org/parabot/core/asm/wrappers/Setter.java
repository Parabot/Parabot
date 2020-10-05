package org.parabot.core.asm.wrappers;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.parabot.core.asm.ASMUtils;
import org.parabot.core.asm.adapters.AddSetterAdapter;
import org.parabot.core.asm.interfaces.Injectable;

/**
 * This class is used for injecting a setter for a specific field
 *
 * @author Everel
 */
public class Setter implements Injectable {
    private final ClassNode fieldLocation;
    private final ClassNode into;
    private final FieldNode field;
    private final String name;
    private final String desc;
    private final boolean methodStatic;

    public Setter(final String fieldLocation, String into, final String fieldName, final String methodName, final String desc, final boolean methodStatic, final String fieldDesc) {
        this.fieldLocation = ASMUtils.getClass(fieldLocation);
        into = (into == null) ? fieldLocation : into;
        this.into = ASMUtils.getClass(into);
        this.field = ASMUtils.getField(this.fieldLocation, fieldName, fieldDesc);
        this.name = methodName;
        this.desc = (desc == null && this.field != null) ? this.field.desc : desc;
        this.methodStatic = methodStatic;
    }

    public Setter(final String fieldLocation, final String fieldName, final String methodName) {
        this(fieldLocation, null, fieldName, methodName, null, false, null);
    }

    /**
     * Short route for getAdaptar().inject();
     *
     * @see AddSetterAdapter#inject
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
    public AddSetterAdapter getAdapter() {
        return new AddSetterAdapter(fieldLocation, into, field, name, desc, methodStatic);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Injectable type: Setter");

        if (fieldLocation.interfaces.size() > 0) {
            sb.append(", accessor type: ").append(fieldLocation.interfaces.get(0).toString().replace('/', '.'));
        }

        if (field != null) {
            sb.append(", field: ").append(field.name);
        }

        sb.append(", method name: ").append(name);

        return sb.toString();
    }
}
