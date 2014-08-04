package org.parabot.core.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * 
 * A RefField represents a field in a class
 * 
 * @author Everel
 *
 */
public class RefField {
	private Field field;
	private Object instance;
	
	public RefField(Field field) {
		this(field, null);
	}
	
	public RefField(Field field, Object instance) {
		this.field = field;
		this.instance = instance;
	}
	
	public Object asObject() {
		if(instance == null && !isStatic()) {
			throw new IllegalStateException("Non static field cannot be fetched without an instance");
		}
		try {
			if(!isAccessible()) {
				field.setAccessible(true);
			}
			return field.get(instance);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
	
	public int asInt() {
		return (int) asObject();
	}
	
	public long asLong() {
		return (long) asObject();
	}
	
	public double asDouble() {
		return (double) asObject();
	}
	
	public float asFloat() {
		return (float) asObject();
	}
	
	public boolean asBoolean() {
		return (boolean) asObject();
	}
	
	public short asShort() {
		return (short) asObject();
	}
	
	public byte asByte() {
		return (byte) asObject();
	}
	
	public String asString() {
		return (String) asObject();
	}
	
	public char asChar() {
		return (char) asObject();
	}
	
	public Class<?> getType() {
		return field.getType();
	}
	
	public Type getGenericType() {
		return field.getGenericType();
	}
	
	public boolean isPrimitiveType() {
		return RefUtils.isPrimitive(getType());
	}
	
	public boolean isString() {
		return getType() == String.class;
	}
	
	public boolean isStatic() {
		return Modifier.isStatic(field.getModifiers());
	}
	
	public boolean isAbstract() {
		return Modifier.isAbstract(field.getModifiers());
	}
	
	public boolean isFinal() {
		return Modifier.isFinal(field.getModifiers());
	}
	
	public boolean isInterface() {
		return Modifier.isInterface(field.getModifiers());
	}
	
	public boolean isNative() {
		return Modifier.isNative(field.getModifiers());
	}
	
	public boolean isPrivate() {
		return Modifier.isPrivate(field.getModifiers());
	}
	
	public boolean isProtected() {
		return Modifier.isProtected(field.getModifiers());
	}
	
	public boolean isPublic() {
		return Modifier.isPublic(field.getModifiers());
	}
	
	public boolean isStrict() {
		return Modifier.isStrict(field.getModifiers());
	}
	
	public boolean isSynchronized() {
		return Modifier.isSynchronized(field.getModifiers());
	}
	
	public boolean isTransient() {
		return Modifier.isTransient(field.getModifiers());
	}
	
	public boolean isVolatile() {
		return Modifier.isVolatile(field.getModifiers());
	}
	
	public boolean isEnumConstants() {
		return field.isEnumConstant();
	}
	
	public boolean isAccessible() {
		return field.isAccessible();
	}
	
	public boolean isSynthetic() {
		return field.isSynthetic();
	}
	
	public String getName() {
		return field.getName();
	}
	
	public int getModifiers() {
		return field.getModifiers();
	}
	
	public Field getField() {
		return field;
	}
	
	public String toGenericString() {
		return field.toGenericString();
	}
	
	public String toString() {
		return field.toString();
	}

}
