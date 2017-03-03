package org.parabot.core.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.parabot.core.Context;
import org.parabot.core.exceptions.FieldNotFoundException;
import org.parabot.core.ui.components.VerboseLoader;

import java.lang.reflect.Modifier;

/**
 * 
 * A collection of various asm util methods
 * 
 * @author Everel
 * 
 */
public class ASMUtils implements Opcodes {

	public static FieldNode getField(ClassNode node, String fieldName) {
		for (final Object fieldNode : node.fields) {
			FieldNode fieldNodeObject = (FieldNode) fieldNode;
			if (fieldNodeObject.name.equals(fieldName)) {
				return fieldNodeObject;
			}
		}
		return null;
	}
	
	public static FieldNode getField(ClassNode node, String fieldName, String desc) throws FieldNotFoundException {
		if(desc == null) {
			return getField(node, fieldName);
		}
		for (final Object fieldNode : node.fields) {
			FieldNode fieldNodeObject = (FieldNode) fieldNode;
			if (fieldNodeObject.name.equals(fieldName) && fieldNodeObject.desc.equals(desc)) {
				return fieldNodeObject;
			}
		}
		VerboseLoader.setState("Failed to find field");
		throw new FieldNotFoundException("Failed to find field");
	}

	public static ClassNode getClass(String className) {
		Context context = Context.getInstance();
		for (ClassNode node : context.getClassPath().classes.values()) {
			if (node.name.equals(className)) {
				return node;
			}
		}
		return null;
	}

	public static MethodNode getMethod(final String className,
			final String methodName, final String methodDesc) {
		return getMethod(getClass(className), methodName, methodDesc);
	}

	public static MethodNode getMethod(final ClassNode location,
			final String methodName, final String methodDesc) {
		for (Object mn : location.methods) {
			MethodNode methodNode = (MethodNode) mn;
			if (methodNode.name.equals(methodName) && methodNode.desc.equals(methodDesc)) {
				return methodNode;
			}
		}
		return null;
	}

	/**
	 * Return right opcode for desc
	 * 
	 * @param desc
	 * @return return opcode
	 */
	public static int getReturnOpcode(String desc) {
		desc = desc.substring(desc.indexOf("L") + 1);
		if (desc.length() > 1) {
			return ARETURN;
		}
		final char c = desc.charAt(0);
		switch (c) {
		case 'I':
		case 'Z':
		case 'B':
		case 'S':
		case 'C':
			return IRETURN;
		case 'J':
			return LRETURN;
		case 'F':
			return FRETURN;
		case 'D':
			return DRETURN;
		case 'V': // void, method desc
			return RETURN;
		}
		throw new RuntimeException("Wrong desc type: " + c);
	}

	public static int getLoadOpcode(String desc) {
		desc = desc.substring(desc.indexOf("L") + 1);
		if (desc.length() > 1) {
			return ALOAD;
		}
		final char c = desc.charAt(0);
		switch (c) {
		case 'I':
		case 'Z':
		case 'B':
		case 'S':
		case 'C':
			return ILOAD;
		case 'J':
			return LLOAD;
		case 'F':
			return FLOAD;
		case 'D':
			return DLOAD;
		}
		throw new RuntimeException("eek " + c);
	}
	
	public static void makePublic(ClassNode node) {
		if (!Modifier.isPublic(node.access)) {
			if (Modifier.isPrivate(node.access)) {
				node.access = node.access & (~Opcodes.ACC_PRIVATE);
			}
			if (Modifier.isProtected(node.access)) {
				node.access = node.access & (~Opcodes.ACC_PROTECTED);
			}
			node.access = node.access | Opcodes.ACC_PUBLIC;
		}
	}
	
	public static void makePublic(MethodNode node) {
		if (!Modifier.isPublic(node.access)) {
			if (Modifier.isPrivate(node.access)) {
				node.access = node.access & (~Opcodes.ACC_PRIVATE);
			}
			if (Modifier.isProtected(node.access)) {
				node.access = node.access & (~Opcodes.ACC_PROTECTED);
			}
			node.access = node.access | Opcodes.ACC_PUBLIC;
		}
	}
	
	public static void makePublic(FieldNode node) {
		if (!Modifier.isPublic(node.access)) {
			if (Modifier.isPrivate(node.access)) {
				node.access = node.access & (~Opcodes.ACC_PRIVATE);
			}
			if (Modifier.isProtected(node.access)) {
				node.access = node.access & (~Opcodes.ACC_PROTECTED);
			}
			node.access = node.access | Opcodes.ACC_PUBLIC;
		}
	}

}
