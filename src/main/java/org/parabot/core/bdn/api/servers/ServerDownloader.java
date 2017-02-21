package org.parabot.core.bdn.api.servers;

import com.google.inject.Inject;
import org.parabot.api.io.SizeInputStream;
import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.bdn.api.APICaller;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.user.SharedUserAuthenticator;
import org.parabot.core.user.implementations.UserAuthenticatorAccess;
import org.parabot.environment.api.utils.StringUtils;

import java.io.*;

/**
 * @author JKetelaar
 */
public class ServerDownloader implements UserAuthenticatorAccess, IServerDownloader {

    private SharedUserAuthenticator userAuthenticator;

    @Override
    public void downloadServer(ServerDescription description){
        InputStream inputStream = (InputStream) APICaller.callPoint(APICaller.APIPoint.DOWNLOAD_SERVER.setPointParams(description.getId()), userAuthenticator);
        Core.verbose("Downloading server...");
        try {
            File file = new File(Directories.getCachePath() + "/" + StringUtils.toMD5(description.getServerName() + description.getRevision()) + ".jar");
            if (!file.exists()){
                file.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            Core.verbose("Downloaded server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Inject
    public void setUserAuthenticator(SharedUserAuthenticator userAuthenticator) {
        this.userAuthenticator = userAuthenticator;
    }
}
