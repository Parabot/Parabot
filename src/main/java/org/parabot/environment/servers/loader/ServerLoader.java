package org.parabot.environment.servers.loader;

import org.objectweb.asm.tree.ClassNode;
import org.parabot.core.asm.ASMClassLoader;
import org.parabot.core.classpath.ClassPath;
import org.parabot.environment.servers.ServerProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * An environment to load a server
 *
 * @author Everel
 */
public class ServerLoader extends ASMClassLoader {
    private final ClassPath classPath;

    public ServerLoader(ClassPath classPath) {
        super(classPath);
        this.classPath = classPath;
    }

    /**
     * Gets all classes that extends ServerProvider
     *
     * @return string array of class names that extends ServerProvider
     */
    public final String[] getServerClassNames() {
        final List<String> classNames = new ArrayList<String>();
        for (ClassNode c : classPath.classes.values()) {
            if (c.superName
                    .replace('/', '.')
                    .equals(ServerProvider.class.getName())) {
                classNames.add(c.name.replace('/', '.'));
            }
        }
        return classNames.toArray(new String[classNames.size()]);
    }

    public ClassPath getClassPath() {
        return classPath;
    }

}
