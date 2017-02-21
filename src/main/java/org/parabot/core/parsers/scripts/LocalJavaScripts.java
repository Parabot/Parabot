package org.parabot.core.parsers.scripts;

import org.parabot.api.io.Directories;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.desc.ScriptDescription;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.executers.LocalScriptExecuter;
import org.parabot.environment.scripts.loader.JavaScriptLoader;

import java.lang.reflect.Constructor;

/**
 * Parses locally stored java scripts
 *
 * @author Everel
 */
public class LocalJavaScripts extends ScriptParser {

    @Override
    public void execute() {
        // parse classes in server directories
        final ClassPath path = new ClassPath();
        path.addClasses(Directories.getScriptCompiledPath());

        // init the script loader
        final JavaScriptLoader loader = new JavaScriptLoader(path);

        // loop through all classes which extends the 'Script' class
        for (final String className : loader.getScriptClassNames()) {
            try {
                // get class
                final Class<?> scriptClass;
                try {
                    scriptClass = loader.loadClass(className);
                } catch (NoClassDefFoundError ignored) {
                    // script for an other server provider
                    continue;
                }
                // get annotation
                final Object annotation = scriptClass
                        .getAnnotation(ScriptManifest.class);
                if (annotation == null) {
                    throw new RuntimeException("Missing manifest at "
                            + className);
                }
                // cast object annotation to script manifest annotation
                final ScriptManifest manifest = (ScriptManifest) annotation;
                // get constructor
                final Constructor<?> con = scriptClass.getConstructor();
                final ScriptDescription desc = new ScriptDescription(
                        manifest.name(), manifest.author(), manifest.category()
                        .toString(), manifest.version(),
                        manifest.description(), manifest.servers(),
                        manifest.vip() ? "yes" : "no",
                        manifest.premium() ? "yes" : "no");
                SCRIPT_CACHE.put(desc, new LocalScriptExecuter(con));
            } catch (ClassNotFoundException ignored) {
            } catch (NoClassDefFoundError ignored) {
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

    }

}
