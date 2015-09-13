package org.parabot.core.asm.redirect;

import org.parabot.core.asm.RedirectClassAdapter;

public class RuntimeRedirect {
	
	public static Runtime getRuntime(){
		return Runtime.getRuntime();
	}
	
	public static int availableProcessors(Runtime r){
		//lol faking it, fuck ikov
		return 2;
	}
	
	public static Process exec(Runtime r,String s){
		System.out.println("Blocked attempted command:" + s);
		throw RedirectClassAdapter.createSecurityException();
	}

}
