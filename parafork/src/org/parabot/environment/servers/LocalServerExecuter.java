package org.parabot.environment.servers;

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
	private final ServerProvider serverProvider;
	private ClassPath classPath = null;
	private String serverName = null;

	public LocalServerExecuter(ServerProvider serverProvider,
			ClassPath classPath, final String serverName) {
		this.serverProvider = serverProvider;
		this.classPath = classPath;
		this.serverName = serverName;
	}

	@Override
	public void run(ThreadGroup tg) {
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
		super.finalize(tg, this.serverProvider, this.serverName);
	}

}
