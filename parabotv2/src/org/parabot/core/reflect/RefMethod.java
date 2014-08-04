package org.parabot.core.reflect;

import java.lang.reflect.Method;

/**
 * 
 * A <code>RefMethod</code> class represent a method of a <code>RefClass</code>.
 * 
 * @author Everel
 *
 */
public class RefMethod extends RefModifiers {
	private Method method;

	public RefMethod(Method method) {
		super(method.getModifiers());
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
	 * Returns <code>true</code> if this method is a synthetic method; returns
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this method is a synthetic method; returns
	 *         <code>false</code> otherwise
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
	 * @return an array of the parameter types of this method
	 */
	public Class<?>[] getParameterTypes() {
		return method.getParameterTypes();
	}
	
	/**
	 * Gets the return type of this class
	 * @return return type of this class
	 */
	public Class<?> getReturnType() {
		return method.getReturnType();
	}
	
	/**
	 * Gets the return type of this class
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
	
	public String toGenericString() {
		return method.toGenericString();
	}
	
	public String toString() {
		return method.toString();
	}
}
