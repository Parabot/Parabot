package org.parabot.core.parsers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
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
	 * @return list of descriptions
	 */
	public ServerDescription[] getDescriptions() {
		if(Core.isDevMode()) {
			return localDesc();
		}
		return publicDesc(); 
	}

	private ServerDescription[] publicDesc() {
		return null;
	}

	private ServerDescription[] localDesc() {
		final ClassPath path = new ClassPath();
		path.loadClasses(Directories.getServerPath(), null);
		try {
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);
			method.invoke((URLClassLoader) ClassLoader.getSystemClassLoader(), Directories.getServerPath().toURI().toURL());
		} catch(Exception e) {
			e.printStackTrace();
		}
		final ServerLoader loader = new ServerLoader(path);
		final List<ServerProvider> providers = new ArrayList<ServerProvider>();
		final List<ServerDescription> descs = new ArrayList<ServerDescription>();
		for(final String className : loader.getServerClassNames()) {
			try {
				final Class<?> serverProviderClass = loader.loadClass(className);
				final Object annotation = serverProviderClass.getAnnotation(ServerManifest.class);
				if(annotation == null) {
					throw new RuntimeException("Missing manifest at " + className);
				}
				final ServerManifest manifest = (ServerManifest) annotation;
				final Constructor<?> con = serverProviderClass.getConstructor();
				final ServerProvider server = (ServerProvider) con.newInstance();
				providers.add(server);
				descs.add(new ServerDescription(manifest.name(), manifest.author(), 0, providers.size() - 1));
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		if(providers.isEmpty()) {
			return null;
		}
		final ServerCache cachedServer = new ServerCache(loader, providers.toArray(new ServerProvider[providers.size()]));
		for(final ServerDescription desc : descs) {
			cache.put(desc, cachedServer);
		}
		return descs.toArray(new ServerDescription[descs.size()]);
	}
	
	public class ServerCache {
		private ServerLoader serverLoader = null;
		private ServerProvider[] serverProviders = null;
			
		private ServerCache(final ServerLoader serverLoader, final ServerProvider[] serverProviders) {
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
