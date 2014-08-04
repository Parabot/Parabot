package org.parabot.core.reflect;

import java.lang.reflect.Modifier;

/**
 * 
 * @author Everel
 *
 */
public class RefModifiers {
	private int modifiers;
	
	public RefModifiers() {
		
	}
	
	public RefModifiers(int modifiers) {
		setModifiers(modifiers);
	}
	
	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}
	
	public int getModifiers() {
		return this.modifiers;
	}
	
	public boolean isStatic() {
		return Modifier.isStatic(modifiers);
	}
	
	public boolean isAbstract() {
		return Modifier.isAbstract(modifiers);
	}
	
	public boolean isFinal() {
		return Modifier.isFinal(modifiers);
	}
	
	public boolean isInterface() {
		return Modifier.isInterface(modifiers);
	}
	
	public boolean isNative() {
		return Modifier.isNative(modifiers);
	}
	
	public boolean isPrivate() {
		return Modifier.isPrivate(modifiers);
	}
	
	public boolean isProtected() {
		return Modifier.isProtected(modifiers);
	}
	
	public boolean isPublic() {
		return Modifier.isPublic(modifiers);
	}
	
	public boolean isStrict() {
		return Modifier.isStrict(modifiers);
	}
	
	public boolean isSynchronized() {
		return Modifier.isSynchronized(modifiers);
	}
	
	public boolean isTransient() {
		return Modifier.isTransient(modifiers);
	}
	
	public boolean isVolatile() {
		return Modifier.isVolatile(modifiers);
	}

}
