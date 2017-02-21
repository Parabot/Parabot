package org.parabot.core.bdn.api.servers;

import org.parabot.core.desc.ServerDescription;

/**
 * @author JKetelaar
 */
public interface IServerDownloader {
    void downloadServer(ServerDescription description);
}
