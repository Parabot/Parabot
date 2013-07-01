package org.parabot.core.parsers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.desc.ServerDescription;
import org.parabot.environment.servers.ServerManifest;
import org.parabot.environment.servers.ServerProvider;
import org.parabot.environment.servers.loader.ServerLoader;

/**
 * 
 * @author Clisprail
 * 
 */
public class ServerManifestParser {
	public static Map<ServerDescription, ServerCache> cache = new HashMap<ServerDescription, ServerCache>();

	/**
	 * Gets server descriptions
	 * 
	 * @return list of descriptions
	 */
	public ServerDescription[] getDescriptions() {
		if (Core.inDebugMode()) {
			return localDesc();
		}
		return publicDesc();
	}

	private ServerDescription[] publicDesc() {
		return null;
	}

	private ServerDescription[] localDesc() {
		// parse classes in server directories
		final ClassPath basePath = new ClassPath();
		basePath.parseJarFiles(false);
		basePath.loadClasses(Directories.getServerPath(), null);
		
		
		ArrayList<ClassPath> classPaths = new ArrayList<ClassPath>();
		classPaths.add(basePath);
		for (final ClassPath classPath : basePath.getJarFiles()) {
			classPaths.add(classPath);
		}
		// list of descriptions
		final List<ServerDescription> allDescs = new ArrayList<ServerDescription>();

		for (final ClassPath path : classPaths) {
			final List<ServerDescription> descs = new ArrayList<ServerDescription>();
			// init the server loader
			final ServerLoader loader = new ServerLoader(path);
			// list of providers
			final List<ServerProvider> providers = new ArrayList<ServerProvider>();

			// loop through all classes which extends the 'ServerProvider' class
			for (final String className : loader.getServerClassNames()) {
				try {
					// get class
					final Class<?> serverProviderClass = loader.loadClass(className);
					// get annotation
					final Object annotation = serverProviderClass
							.getAnnotation(ServerManifest.class);
					if (annotation == null) {
						throw new RuntimeException("Missing manifest at "
								+ className);
					}
					// cast object annotation to server manifest annotation
					final ServerManifest manifest = (ServerManifest) annotation;
					// get constructor
					final Constructor<?> con = serverProviderClass.getConstructor();
					final ServerProvider server = (ServerProvider) con.newInstance();
					providers.add(server);
					descs.add(new ServerDescription(manifest.name(), manifest
							.author(), 0, providers.size() - 1));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (providers.isEmpty()) {
				continue;
			}
			final ServerCache cachedServer = new ServerCache(loader, providers.toArray(new ServerProvider[providers.size()]));
			for (final ServerDescription desc : descs) {
				allDescs.add(desc);
				cache.put(desc, cachedServer);
			}
		}
		return allDescs.toArray(new ServerDescription[allDescs.size()]);
	}

	public class ServerCache {
		private ServerLoader serverLoader = null;
		private ServerProvider[] serverProviders = null;

		private ServerCache(final ServerLoader serverLoader,
				final ServerProvider[] serverProviders) {
			this.serverLoader = serverLoader;
			this.serverProviders = serverProviders;
		}

		public ServerLoader getLoader() {
			return serverLoader;
		}

		public ServerProvider[] getProviders() {
			return serverProviders;
		}
	}

}
