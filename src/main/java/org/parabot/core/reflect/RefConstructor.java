package org.parabot.core.reflect;

import java.lang.reflect.Constructor;

/**
 * A <code>RefConstructor</code> class represent a constructor method of a
 * <code>RefClass</code>.
 *
 * @author Everel
 */
public class RefConstructor extends RefModifiers {
    private final Constructor<?> constructor;

    public RefConstructor(Constructor<?> constructor) {
        super(constructor.getModifiers());
        this.constructor = constructor;
    }

    /**
     * Creates a new instance of this class by invoking this constructor
     *
     * @return the instance of the class
     */
    public RefClass newInstance() {
        return newInstance(new Object[]{});
    }

    /**
     * Creates a new instance of this class by invoking this constructor
     *
     * @param args the arguments for the constructor
     *
     * @return the instance of the class
     */
    public RefClass newInstance(Object... args) {
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        try {
            Object instance = constructor.newInstance(args);
            return new RefClass(instance);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    /**
     * Get the value of the accessible flag for this object.
     *
     * @return the value of the object's accessible flag
     */
    public boolean isAccessible() {
        return constructor.isAccessible();
    }

    /**
     * Returns <code>true</code> if this constructor is a synthetic constructor;
     * returns <code>false</code> otherwise.
     *
     * @return <code>true</code> if this constructor is a synthetic constructor;
     * returns <code>false</code> otherwise
     */
    public boolean isSynthetic() {
        return constructor.isSynthetic();
    }

    /**
     * Returns the name of the constructor.
     *
     * @return name of the constructor
     */
    public String getName() {
        return constructor.getName();
    }

    /**
     * Returns an array of the parameter types of this constructor
     *
     * @return an array of the parameter types of this constructor
     */
    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    /**
     * Gets the java reflection API constructor representation
     *
     * @return constructor
     */
    public Constructor<?> getConstructor() {
        return this.constructor;
    }

    public String toGenericString() {
        return constructor.toGenericString();
    }

    public String toString() {
        return constructor.toString();
    }

}
