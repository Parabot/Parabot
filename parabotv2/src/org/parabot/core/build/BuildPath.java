package org.parabot.core.build;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 
 * @author Clisprail
 *
 */
public class BuildPath {
	
	public static void add(final URL url) {
		try {
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);
			method.invoke((URLClassLoader) ClassLoader.getSystemClassLoader(), url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
