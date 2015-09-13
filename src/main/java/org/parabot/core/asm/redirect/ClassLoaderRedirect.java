package org.parabot.core.asm.redirect;

import org.parabot.core.asm.RedirectClassAdapter;

public class ClassLoaderRedirect {
	
	public static Class<?>loadClass(ClassLoader c,String name){
		throw RedirectClassAdapter.createSecurityException();
	}
	static int count = 0;
	public static ClassLoader getParent(ClassLoader c){
		throw RedirectClassAdapter.createSecurityException();
	}

}
