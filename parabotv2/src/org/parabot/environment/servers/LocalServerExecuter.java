package org.parabot.environment.servers;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;

import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.build.BuildPath;
import org.parabot.core.classpath.ClassPath;

/**
 * 
 * Loads locally stored server providers
 * 
 * @author Everel
 * 
 */
public class LocalServerExecuter extends ServerExecuter {
	private final Constructor<?> serverProviderConstructor;
	private ClassPath classPath;
	private String serverName;

	public LocalServerExecuter(Constructor<?> serverProviderConstructor,
			ClassPath classPath, final String serverName) {
		this.serverProviderConstructor = serverProviderConstructor;
		this.classPath = classPath;
		this.serverName = serverName;
	}

	@Override
	public void run() {
		// add jar or directory to buildpath.
		if (this.classPath.isJar()) {
			Core.verbose("Adding server provider jar to buildpath: "
					+ this.classPath.lastParsed.toString());
			this.classPath.addToBuildPath();
		} else {
			Core.verbose("Adding server providers directory to buildpath: "
					+ Directories.getServerPath().getPath());
			try {
				BuildPath.add(Directories.getServerPath().toURI().toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		// finalize
		try {
			super.finalize((ServerProvider) serverProviderConstructor.newInstance(), this.serverName);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
