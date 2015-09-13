package org.parabot.core.asm.redirect;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.parabot.core.asm.RedirectClassAdapter;

public class ClassRedirect {
	
	public static Object newInstance(Class<?>c){
		throw RedirectClassAdapter.createSecurityException();
	}
	
	public static Field getDeclaredField(Class<?> c, String s) throws NoSuchFieldException, SecurityException{
		System.out.println(c.getName() + "." + c.getDeclaredField(s) + " Blocked.");
		throw RedirectClassAdapter.createSecurityException();
	}
	
	public static Method getDeclaredMethod(Class<?>c,String name,Class<?>... params) throws NoSuchMethodException, SecurityException{
		System.out.println(c.getName() + "#" + c.getDeclaredMethod(name,params) + " Blocked.");
		throw RedirectClassAdapter.createSecurityException();
	}
	
	public static Class<?> forName(String name) throws ClassNotFoundException{
		if(name.contains("parabot"))
			throw new ClassNotFoundException();
		return Class.forName(name);
	}
	
	public static ClassLoader getClassLoader(Class<?>c){
		throw RedirectClassAdapter.createSecurityException();
	}
	
	public static String getName(Class<?>c){
		if(c.getName().contains("parabot"))
			return "java.lang.String";
		return c.getName();
	}
	
	public static InputStream getResourceAsStream(Class<?>c, String res){
		if(res.contains(".class"))
			throw RedirectClassAdapter.createSecurityException();
		return c.getResourceAsStream(res);
	}

}
