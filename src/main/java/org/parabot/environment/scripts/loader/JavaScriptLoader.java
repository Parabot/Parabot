package org.parabot.environment.scripts.loader;

import org.objectweb.asm.tree.ClassNode;
import org.parabot.core.asm.ASMClassLoader;
import org.parabot.core.classpath.ClassPath;
import org.parabot.environment.scripts.Script;

import java.util.ArrayList;
import java.util.List;

/**
 * An environment to load a script
 *
 * @author Everel, JKetelaar
 */
public class JavaScriptLoader extends ASMClassLoader {
    private ClassPath classPath;

    public JavaScriptLoader(ClassPath classPath) {
        super(classPath);
        this.classPath = classPath;
    }

    /**
     * Gets all classes that extends ServerProvider
     *
     * @return string array of class names that extends ServerProvider
     */
    public final String[] getScriptClassNames() {
        final List<String> classNames = new ArrayList<>();
        for (ClassNode c : classPath.classes.values()) {
            if (isScriptClass(c)) {
                classNames.add(c.name.replace('/', '.'));
            } else {
                ClassNode superClass = findClassNodeForName(c.superName);
                if (superClass != null && isScriptClass(superClass)) {
                    classNames.add(c.name.replace('/', '.'));
                }
            }
        }

        String[] classes = new String[classNames.size()];
        for (int i = 0; i < classNames.size(); i++) {
            classes[i] = classNames.get(i);
        }

        return classes;
    }

    /**
     * Checks if given ClassNode is Script class
     *
     * @param classNode
     *
     * @return
     */
    private boolean isScriptClass(ClassNode classNode) {
        return classNode
                .superName
                .replace('/', '.')
                .equals(Script.class.getName());
    }

    /**
     * Finds a ClassNode instance for a given class name
     *
     * @param name
     *
     * @return
     */
    private ClassNode findClassNodeForName(String name) {
        for (ClassNode classNode : classPath.classes.values()) {
            if (classNode.name.equals(name)) {
                return classNode;
            }
        }

        return null;
    }
}

