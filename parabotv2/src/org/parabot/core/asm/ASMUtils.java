package org.parabot.core.asm;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.parabot.core.Context;

/**
 * 
 * @author Clisprail
 *
 */
public class ASMUtils {
	
	public static FieldNode getField(ClassNode node, String fieldName) {
		for(final FieldNode fieldNode : node.fields) {
			if(fieldNode.name.equals(fieldName)) {
				return fieldNode;
			}
		}
		return null;
	}
	
	public static ClassNode getClass(String className) {
		Context context = Context.resolve();
		for(ClassNode node : context.getClassPath().classes.values()) {
			if(node.name.equals(className)) {
				return node;
			}
		}
		return null;
	}

}
