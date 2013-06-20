package org.parabot.core.bot.loader;

import org.parabot.core.asm.ASMClassLoader;
import org.parabot.core.classpath.ClassPath;

/**
 * Handles client class calls
 * 
 * @author Clisprail
 * 
 */
public class BotLoader extends ASMClassLoader {

	public BotLoader(ClassPath classPath, ASMClassLoader serverProvider) {
		super(classPath);
	}

}
