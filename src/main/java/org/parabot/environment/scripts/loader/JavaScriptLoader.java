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
 * @author Everel
 */
public class JavaScriptLoader extends ASMClassLoader {
    private final ClassPath classPath;

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
        final List<String> classNames = new ArrayList<String>();
        for (ClassNode c : classPath.classes.values()) {
            if (c.superName.replace('/', '.').equals(
                    Script.class.getName())) {
                classNames.add(c.name.replace('/', '.'));
            }
        }
        return classNames.toArray(new String[classNames.size()]);
    }

}

