package org.parabot.core.parsers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.desc.ScriptDescription;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.loader.ScriptLoader;

/**
 * 
 * @author Clisprail
 * 
 */
public class ScriptManifestParser {
	
	public static Map<ScriptDescription, Script> scriptCache = new HashMap<ScriptDescription, Script>();

	/**
	 * Gets server descriptions
	 * 
	 * @return list of descriptions
	 */
	public ScriptDescription[] getDescriptions() {
		scriptCache.clear();
		if (Core.inDebugMode()) {
			return localDesc();
		}
		return publicDesc();
	}

	private ScriptDescription[] publicDesc() {
		return null;
	}

	private ScriptDescription[] localDesc() {
		// parse classes in server directories
		final ClassPath path = new ClassPath();
		path.loadClasses(Directories.getScriptCompiledPath(), null);

		// init the script loader
		final ScriptLoader loader = new ScriptLoader(path);

		// list of scripts
		final List<Script> scripts = new ArrayList<Script>();

		// list of descriptions
		final List<ScriptDescription> descs = new ArrayList<ScriptDescription>();

		// loop through all classes which extends the 'Script' class
		for (final String className : loader.getScriptClassNames()) {
			try {
				// get class
				final Class<?> scriptClass = loader.loadClass(className);
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
				final Script script = (Script) con.newInstance();
				scripts.add(script);
				final ScriptDescription desc = new ScriptDescription(manifest.name(), manifest
						.author(), manifest.category().toString(), manifest.version(), manifest.description(),
						manifest.servers(), scripts.size() - 1);
				scriptCache.put(desc, script);
				descs.add(desc);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		if (scripts.isEmpty()) {
			return null;
		}
		return descs.toArray(new ScriptDescription[descs.size()]);
	}

}
