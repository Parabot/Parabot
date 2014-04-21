
package org.parabot.core.parsers.servers;

import org.parabot.core.Directories;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.desc.ServerDescription;
import org.parabot.environment.servers.LocalServerExecuter;
import org.parabot.environment.servers.ServerManifest;
import org.parabot.environment.servers.ServerProvider;
import org.parabot.environment.servers.loader.ServerLoader;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Parses local server providers located in the servers directory
 * 
 * @author Everel
 */
public class LocalServers extends ServerParser
{

	@Override
	public void execute()
	{
		// parse classes in server directories
		final ClassPath basePath = new ClassPath();
		basePath.parseJarFiles( false );
		basePath.addClasses( Directories.getServerPath() );

		final ArrayList<ClassPath> classPaths = new ArrayList<ClassPath>();
		classPaths.add( basePath );
		for( final ClassPath classPath: basePath.getJarFiles() ) {
			classPaths.add( classPath );
		}

		for( final ClassPath path: classPaths ) {
			// init the server loader
			final ServerLoader loader = new ServerLoader( path );

			// loop through all classes which extends the 'ServerProvider' class
			for( final String className: loader.getServerClassNames() ) {
				try {
					// get class
					final Class< ? > serverProviderClass = loader
							.loadClass( className );
					// get annotation
					final Object annotation = serverProviderClass
							.getAnnotation( ServerManifest.class );
					if( annotation == null ) {
						throw new RuntimeException( "Missing manifest at "
								+ className );
					}
					// cast object annotation to server manifest annotation
					final ServerManifest manifest = ( ServerManifest )annotation;
					// get constructor
					final Constructor< ? > con = serverProviderClass
							.getConstructor();
					final ServerProvider serverProvider = ( ServerProvider )con
							.newInstance();

					SERVER_CACHE.put(
							new ServerDescription( manifest.name(), manifest
									.author(), manifest.version() ),
							new LocalServerExecuter( serverProvider, path,
									manifest.name() ) );
				} catch( Throwable t ) {
					t.printStackTrace();
				}
			}
		}

	}

}
