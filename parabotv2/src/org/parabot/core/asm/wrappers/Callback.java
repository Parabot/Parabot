
package org.parabot.core.asm.wrappers;

import org.objectweb.asm.tree.MethodNode;
import org.parabot.core.asm.ASMUtils;
import org.parabot.core.asm.adapters.AddCallbackAdapter;
import org.parabot.core.asm.interfaces.Injectable;

/**
 * This class is used for injecting a callback into a methodnode
 * 
 * @author Everel
 */
public class Callback implements Injectable
{

	private MethodNode method;
	private String invokeClass;
	private String invokeMethod;
	private String desc;
	private int[] args;


	public Callback( final String className, final String methodName,
			final String methodDesc, final String callbackClass,
			final String callbackMethod, final String callbackDesc, String args )
	{
		this.method = ASMUtils.getMethod( className, methodName, methodDesc );
		this.invokeClass = callbackClass;
		this.invokeMethod = callbackMethod;
		this.desc = callbackDesc;
		if( args.contains( "," ) ) {
			final String[] strArgs = args.split( "," );
			this.args = new int[strArgs.length];
			for( int i = 0; i < this.args.length; i ++ ) {
				this.args[i] = Integer.parseInt( strArgs[i] );
			}
		} else {
			this.args = new int[] { Integer.parseInt( args ) };
		}
	}


	@Override
	public void inject()
	{
		getAdapter().inject();
	}


	public AddCallbackAdapter getAdapter()
	{
		return new AddCallbackAdapter( this.method, this.invokeClass,
				this.invokeMethod, this.desc, this.args );
	}

}
