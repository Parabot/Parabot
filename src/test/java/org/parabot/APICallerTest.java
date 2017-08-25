package org.parabot;

import org.junit.Assert;
import org.junit.Test;
import org.parabot.api.io.Directories;
import org.parabot.core.bdn.api.APICaller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author JKetelaar
 */
public class APICallerTest {

    @Test
    public void test() throws IOException {
        InputStream stream = (InputStream) APICaller.callPoint(APICaller.APIPoint.DOWNLOAD_PROVIDER.setPointParams("default-provider", false));
        Assert.assertNotNull(stream);

        File tempFile = File.createTempFile("parabot_tmp", ".tmp", Directories.getDefaultDirectory());
        tempFile.deleteOnExit();

        // Checking if file is equal or less than 1 kb
        Assert.assertTrue(tempFile.length() / 1024 <= 1);

        APICaller.downloadFile(stream, tempFile);

        // Checking if file is more than 1 kb
        Assert.assertTrue(tempFile.length() / 1024 > 1);
    }
}
