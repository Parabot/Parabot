package org.parabot.core.classpath;

import java.io.File;
import java.io.FileOutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

/**
 * 
 * @author Clisprail
 *
 */
public class JarDumper {
	
	/**
	 * Dumps classnodes to a jar file
	 * @param classPath
	 * @param fileName
	 */
	public static void dump(final ClassPath classPath, final String fileName) {
		try {
			FileOutputStream stream = new FileOutputStream(new File(fileName));
			JarOutputStream out = new JarOutputStream(stream);
			for (ClassNode cn : classPath.classes.values()) {
				JarEntry je = new JarEntry(cn.name + ".class");
				out.putNextEntry(je);
				ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
				cn.accept(cw);
				out.write(cw.toByteArray());
			}
			out.close();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
