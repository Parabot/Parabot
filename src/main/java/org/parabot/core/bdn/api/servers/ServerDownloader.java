package org.parabot.core.bdn.api.servers;

import org.parabot.core.bdn.api.APICaller;
import org.parabot.core.desc.ServerDescription;

/**
 * @author JKetelaar
 */
public class ServerDownloader {
    public void downloadServer(ServerDescription description){
        APICaller.callPoint(APICaller.APIPoint.DOWNLOAD_SERVER.setPointParams(description.getId()));
    }
}
