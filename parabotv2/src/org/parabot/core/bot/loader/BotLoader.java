package org.parabot.core.bot.loader;

import org.objectweb.asm.tree.ClassNode;
import org.parabot.core.asm.ASMClassLoader;
import org.parabot.core.classpath.ClassPath;

/**
 * Handles client class calls
 * 
 * @author Clisprail
 * 
 */
public class BotLoader extends ASMClassLoader {
	private ASMClassLoader serverProvider = null;

	public BotLoader(ClassPath classPath, ASMClassLoader serverProvider) {
		super(classPath);
		this.serverProvider = serverProvider;
	}
	
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return findClass(name);
	}

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		String key = name.replace('.', '/');
		if(serverProvider.classCache.containsKey(key)) {
			return serverProvider.classCache.get(key);
		}
		ClassNode node = serverProvider.classPath.classes.get(key);
		if (node != null) {
			serverProvider.classPath.classes.remove(key);
			Class<?>c = serverProvider.nodeToClass(node);
			serverProvider.classCache.put(key, c);
			return c;
		}
		return super.findClass(name);
	}

}
