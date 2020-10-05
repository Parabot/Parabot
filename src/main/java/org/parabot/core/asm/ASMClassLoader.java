package org.parabot.core.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.parabot.core.classpath.ClassPath;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * Makes classnodes into runnable classes
 *
 * @author Everel
 * @author Matt
 */
public class ASMClassLoader extends ClassLoader {

    private final Map<String, Class<?>> classCache;
    public ClassPath classPath;

    public ASMClassLoader(final ClassPath classPath) {
        this.classCache = new HashMap<String, Class<?>>();
        this.classPath = classPath;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return findClass(name);
    }

    @Override
    protected URL findResource(String name) {
        if (getSystemResource(name) == null) {
            if (classPath.resources.containsKey(name)) {
                try {
                    return classPath.resources.get(name).toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }
        return getSystemResource(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return getSystemClassLoader().loadClass(name);
        } catch (Exception ignored) {

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
        return getSystemClassLoader().loadClass(name);
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
        CodeSource code = null;
        try {
            code = new CodeSource(new URL("http://www.url.com/"), (Certificate[]) null);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new ProtectionDomain(code, getPermissions());
    }

    private final Permissions getPermissions() {
        Permissions permissions = new Permissions();
        permissions.add(new AllPermission());
        return permissions;
    }

}

