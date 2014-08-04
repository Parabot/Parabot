package org.parabot.core.reflect;

import java.util.ArrayList;

/**
 * 
 * A RefClass represents a class or an instance of that class, if no instance is
 * provided this class can only get values from static fields and only invoke
 * static methods
 * 
 * @author Everel
 * 
 */
public class RefClass {
	private Object instance;
	private Class<?> clazz;

	public RefClass(Class<?> clazz) {
		this.clazz = clazz;
	}

	public RefClass(Object instance) {
		setInstance(instance);
	}
	
	public RefClass(Class<?> clazz, Object instance) {
		this.clazz = clazz;
		setInstance(instance);
	}

	/**
	 * Sets the instance of this class so now non static fields values can be retrieved and non static methods can be invoked
	 * @param instance instance of this class.
	 */
	public void setInstance(Object instance) {
		if(instance == null) {
			this.instance = null;
			return;
		}
		if (this.clazz != null) {
			if (!clazz.isInstance(instance)) {
				throw new IllegalArgumentException(instance
						+ " is not an instance of the class " + clazz);
			}
		}
		this.instance = instance;
		if (this.clazz == null) {
			this.clazz = instance.getClass();
		}
	}
	
	/**
	 * 
	 * Gets the instance of this class
	 * 
	 * @return if an instance of this class is known it will return that instance, otherwise it will return null.
	 */
	public Object getInstance() {
		return this.instance;
	}
	
	/**
	 * Gets the class' fields
	 * @return all fields if an instance is provided, otherwise only static fields
	 */
	public RefField[] getFields() {
		ArrayList<RefField> fields = new ArrayList<RefField>();
		if(this.instance == null) {
			return null;
		}
		return fields.toArray(new RefField[fields.size()]);
	}

}
