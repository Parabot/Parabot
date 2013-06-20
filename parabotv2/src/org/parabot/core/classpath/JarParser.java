package org.parabot.core.classpath;

import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

/**
 * 
 * A class for parsing a jar file
 * 
 * @author Clisprail
 *
 */
public class JarParser {

	/**
	 * Parses a jar from an URL
	 * @param classPath
	 * @param jarLocation
	 */
	public static void parseJar(final ClassPath classPath, final String jarLocation) {
		try {
			URL jarURL = new URL("jar:" + jarLocation + "!/");
			JarURLConnection jarConnection = (JarURLConnection) jarURL
					.openConnection();
			JarFile theJar = jarConnection.getJarFile();
			Enumeration<?> en = theJar.entries();
			while (en.hasMoreElements()) {
				JarEntry entry = (JarEntry) en.nextElement();
				if (entry.getName().endsWith(".class")) {
					ClassReader cr = new ClassReader(
							theJar.getInputStream(entry));
					ClassNode cn = new ClassNode();
					cr.accept(cn, 0);
					classPath.classes.put(cn.name, cn);
				} else if (!entry.isDirectory()
						&& !entry.getName().startsWith("META-INF")) {
					Resources.loadResource(classPath, entry.getName(), theJar.getInputStream(entry));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
