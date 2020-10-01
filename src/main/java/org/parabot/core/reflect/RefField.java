package org.parabot.core.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * A <code>RefField</code> represents a field in a <code>RefClass</code>
 *
 * @author Everel
 */
public class RefField extends RefModifiers {
    private final Field field;
    private final Object instance;

    public RefField(Field field) {
        this(field, null);
    }

    public RefField(Field field, Object instance) {
        super(field.getModifiers());
        this.field = field;
        this.instance = instance;
    }

    /**
     * Retrieves the field it's value as object
     *
     * @return the value of the field
     */
    public Object asObject() {
        if (instance == null && !isStatic()) {
            throw new IllegalStateException(
                    "Non static field cannot be fetched without an instance");
        }
        try {
            if (!isAccessible()) {
                field.setAccessible(true);
            }
            return field.get(instance);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves the field value as an integer
     *
     * @return integer value of field
     */
    public int asInt() {
        return (int) asObject();
    }

    /**
     * Retrieves the field value as a long
     *
     * @return long value of field
     */
    public long asLong() {
        return (long) asObject();
    }

    /**
     * Retrieves the field value as an double
     *
     * @return double value of field
     */
    public double asDouble() {
        return (double) asObject();
    }

    /**
     * Retrieves the field value as a float
     *
     * @return float value of field
     */
    public float asFloat() {
        return (float) asObject();
    }

    /**
     * Retrieves the field value as a boolean
     *
     * @return boolean value of field
     */
    public boolean asBoolean() {
        return (boolean) asObject();
    }

    /**
     * Retrieves the field value as a short
     *
     * @return short value of field
     */
    public short asShort() {
        return (short) asObject();
    }

    /**
     * Retrieves the field value as a byte
     *
     * @return byte value of field
     */
    public byte asByte() {
        return (byte) asObject();
    }

    /**
     * Retrieves the field value as a java/lang/String
     *
     * @return String value of field
     */
    public String asString() {
        return (String) asObject();
    }

    /**
     * Retrieves the field value as a character
     *
     * @return char value of field
     */
    public char asChar() {
        return (char) asObject();
    }

    /**
     * Sets the field value
     *
     * @param object object to set
     */
    public void set(Object object) {
        if (instance == null && !isStatic()) {
            throw new IllegalStateException(
                    "Non static field cannot be set without an instance");
        }
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            field.set(instance, object);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Sets the field integer value
     *
     * @param i value to set
     */
    public void setInt(int i) {
        set(i);
    }

    /**
     * Sets the field long value
     *
     * @param l value to set
     */
    public void setLong(long l) {
        set(l);
    }

    /**
     * Sets the field double value
     *
     * @param d value to set
     */
    public void setDouble(double d) {
        set(d);
    }

    /**
     * Sets the field float value
     *
     * @param f value to set
     */
    public void setFloat(float f) {
        set(f);
    }

    /**
     * Sets the field boolean value
     *
     * @param b value to set
     */
    public void setBoolean(boolean b) {
        set(b);
    }

    /**
     * Sets the field short value
     *
     * @param s value to set
     */
    public void setShort(short s) {
        set(s);
    }

    /**
     * Sets the byte integer value
     *
     * @param b value to set
     */
    public void setByte(byte b) {
        set(b);
    }

    /**
     * Sets the field char value
     *
     * @param c value to set
     */
    public void setChar(char c) {
        set(c);
    }

    /**
     * Gets the field type
     *
     * @return type of field
     */
    public Class<?> getType() {
        return field.getType();
    }

    /**
     * Gets the field type
     *
     * @return type of field
     */
    public org.objectweb.asm.Type getASMType() {
        return org.objectweb.asm.Type.getType(getType());
    }

    /**
     * Gets the field description
     *
     * @return desc of field
     */
    public String getTypeDesc() {
        return getASMType().getDescriptor();
    }

    /**
     * Gets the generic type of this field if any
     *
     * @return generic type
     */
    public Type getGenericType() {
        return field.getGenericType();
    }

    /**
     * Determines if this field is an array
     *
     * @return <code>true</code> if this field is an array (type)
     */
    public boolean isArray() {
        return getASMType().getSort() == org.objectweb.asm.Type.ARRAY;
    }

    /**
     * Returns the number of dimensions of this array type. This method should
     * only be used for an array type.
     *
     * @return the number of dimensions of this array type
     */
    public int getArrayDimensions() {
        return getASMType().getDimensions();
    }

    /**
     * Determines if field type is a primitive type
     *
     * @return <code>true</code> if the field is a primitive type, otherwise
     * <code>false</code>
     */
    public boolean isPrimitiveType() {
        return RefUtils.isPrimitive(getType());
    }

    /**
     * Determines if field type is a string type
     *
     * @return <code>true</code> if the field type is a string type, otherwise
     * <code>false</code>
     */
    public boolean isString() {
        return getType() == String.class;
    }

    /**
     * Sets the field string value
     *
     * @param s value to set
     */
    public void setString(String s) {
        set(s);
    }

    /**
     * Returns <code>true</code> if this field represents an element of an
     * enumerated type; returns <code>false</code> otherwise.
     *
     * @return <code>true</code> if and only if this field represents an element
     * of an enumerated type.
     */
    public boolean isEnumConstants() {
        return field.isEnumConstant();
    }

    /**
     * Get the value of the accessible flag for this object.
     *
     * @return the value of the object's accessible flag
     */
    public boolean isAccessible() {
        return field.isAccessible();
    }

    /**
     * Returns <code>true</code> if this field is a synthetic field; returns
     * <code>false</code> otherwise.
     *
     * @return <code>true</code> if this field is a synthetic field; returns
     * <code>false</code> otherwise
     */
    public boolean isSynthetic() {
        return field.isSynthetic();
    }

    /**
     * Returns the name of the field.
     *
     * @return name of the field
     */
    public String getName() {
        return field.getName();
    }

    /**
     * Gets the java reflection API field representation
     *
     * @return field
     */
    public Field getField() {
        return field;
    }

    /**
     * Gets the declaring <code>RefClass</code> of this field
     *
     * @return <code>RefClass</code> holding this field
     */
    public RefClass getOwner() {
        return new RefClass(field.getDeclaringClass(), instance);
    }

    public String toGenericString() {
        return field.toGenericString();
    }

    public String toString() {
        return field.toString();
    }

}
