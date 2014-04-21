
package org.parabot.core.asm.adapters;

import java.lang.reflect.Modifier;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.parabot.core.asm.ASMUtils;
import org.parabot.core.asm.interfaces.Injectable;

/**
 * Injects a callback, invokes a given static method
 * 
 * @author Everel
 */
public class AddCallbackAdapter implements Injectable, Opcodes
{

	private MethodNode method;
	private String invokeClass;
	private String invokeMethod;
	private String desc;
	private int[] args;


	public AddCallbackAdapter( final MethodNode method,
			final String invokeClass, final String invokeMethod,
			final String desc, final int[] args )
	{
		this.method = method;
		this.invokeClass = invokeClass;
		this.invokeMethod = invokeMethod;
		this.desc = desc;
		this.args = args;
	}


	@Override
	public void inject()
	{
		final Type[] types = Type.getArgumentTypes( this.method.desc );
		InsnList inject = new InsnList();
		Label l0 = new Label();
		inject.add( new LabelNode( l0 ) );
		for( int arg: args ) {
			inject.add( new VarInsnNode( ASMUtils.getLoadOpcode( types[arg]
					.getDescriptor() ), Modifier.isStatic( method.access ) ? arg: arg + 1 ) );
		}
		inject.add( new MethodInsnNode( INVOKESTATIC,
				this.invokeClass, this.invokeMethod,
				this.desc ) );
		this.method.instructions.insert( inject );
	}

}
