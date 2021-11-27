package org.parabot.environment.servers;

import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.build.BuildPath;
import org.parabot.core.classpath.ClassPath;
import org.parabot.environment.servers.executers.ServerExecuter;

import java.net.MalformedURLException;

/**
 * Loads locally stored server providers
 *
 * @author Everel
 */
@SuppressWarnings("Duplicates")
@Deprecated
public class LocalServerExecuter extends ServerExecuter {
    private final ServerProvider serverProvider;
    private final ClassPath classPath;
    private final String serverName;

    public LocalServerExecuter(ServerProvider serverProvider,
                               ClassPath classPath, final String serverName) {
        this.serverProvider = serverProvider;
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
        super.finalize(this.serverProvider, this.serverName);
    }

}
