package org.parabot.core.asm;


import java.net.URL;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;

import org.matt123337.proxy.ClassRemapper;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.RemappingClassAdapter;
import org.objectweb.asm.tree.ClassNode;
import org.parabot.core.classpath.ClassPath;

/**
 * 
 * Makes classnodes into runnable classes
 * 
 * @author Everel
 * @author Matt
 *
 */
public class ASMClassLoader extends ClassLoader {

	private Map<String, Class<?>> classCache = new HashMap<String, Class<?>>();
	public ClassPath classPath = null;
	
	public ASMClassLoader(final ClassPath classPath) {
		this.classPath = classPath;
	}

	@Override
	protected URL findResource(String name) {
		if (getSystemResource(name) == null) {
			if (classPath.resources.containsKey(name)) {
				return classPath.resources.get(name);
			} else {
				return null;
			}
		}
		return getSystemResource(name);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return findClass(name);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			return super.getSystemClassLoader().loadClass(name);
		} catch (Exception e) {
			
		}
		String key = name.replace('.', '/');
		if (classCache.containsKey(key)) {
			return classCache.get(key);
		}
		ClassNode node = classPath.classes.get(key);
		if (node != null) {
			classPath.classes.remove(key);
			Class<?> c = nodeToClass(node);
			classCache.put(key, c);
			return c;
		}
		return super.getSystemClassLoader().loadClass(name);
	}

	private final Class<?> nodeToClass(ClassNode node) {
		if (super.findLoadedClass(node.name) != null) {
			return findLoadedClass(node.name);
		}
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		node.accept(cw);
		byte[] b = cw.toByteArray();
		return defineClass(node.name.replace('/', '.'), b, 0, b.length,
				getDomain());
	}

	private final ProtectionDomain getDomain() {
		CodeSource code = new CodeSource(null, (Certificate[]) null);
		return new ProtectionDomain(code, getPermissions());
	}

	private final Permissions getPermissions() {
		Permissions permissions = new Permissions();
		permissions.add(new AllPermission());
		return permissions;
	}

}

