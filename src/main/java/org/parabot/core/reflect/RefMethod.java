package org.parabot.core.reflect;

import java.lang.reflect.Method;

/**
 * A <code>RefMethod</code> class represent a method of a <code>RefClass</code>.
 *
 * @author Everel
 */
public class RefMethod extends RefModifiers {
    private final Method method;
    private final Object instance;

    public RefMethod(Method method) {
        this(method, null);
    }

    public RefMethod(Method method, Object instance) {
        super(method.getModifiers());
        this.method = method;
        this.instance = instance;
    }

    /**
     * Get the value of the accessible flag for this object.
     *
     * @return the value of the object's accessible flag
     */
    public boolean isAccessible() {
        return method.isAccessible();
    }

    /**
     * Determines if this method is a bridge method.
     *
     * @return <code>true</code> if this method is a bridge method, otherwise
     * <code>false</code>
     */
    public boolean isBridge() {
        return method.isBridge();
    }

    /**
     * Determines if this method can take a variable amount of arguments
     *
     * @return <code>true</code> if this method can take a variable amount of
     * arguments
     */
    public boolean isVarArgs() {
        return method.isVarArgs();
    }

    /**
     * Returns <code>true</code> if this method is a synthetic method; returns
     * <code>false</code> otherwise.
     *
     * @return <code>true</code> if this method is a synthetic method; returns
     * <code>false</code> otherwise
     */
    public boolean isSynthetic() {
        return method.isSynthetic();
    }

    /**
     * Returns the name of the method.
     *
     * @return name of the method
     */
    public String getName() {
        return method.getName();
    }

    /**
     * Returns an array of the parameter types of this method
     *
     * @return an array of the parameter types of this method
     */
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    /**
     * Gets the return type of this class
     *
     * @return return type of this class
     */
    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    /**
     * Gets the return type of this class
     *
     * @return return type of this class
     */
    public org.objectweb.asm.Type getASMReturnType() {
        return org.objectweb.asm.Type.getType(getReturnType());
    }

    /**
     * Gets the java reflection API method representation
     *
     * @return constructor
     */
    public Method getMethod() {
        return this.method;
    }

    /**
     * Invokes the method and returns it returned object
     *
     * @return object returned by the method
     */
    public Object invoke() {
        return invoke(new Object[]{});
    }

    /**
     * Invokes the method and returns it returned object
     *
     * @param args arguments for the invokable method
     *
     * @return object returned by the method
     */
    public Object invoke(Object... args) {
        if (!isStatic() && instance == null) {
            throw new IllegalStateException(
                    "Can not invoke non static method without an instance.");
        }
        if (!isAccessible()) {
            method.setAccessible(true);
        }
        try {
            Object retObject = method.invoke(instance, args);
            return retObject;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    public String toGenericString() {
        return method.toGenericString();
    }

    public String toString() {
        return method.toString();
    }
}
