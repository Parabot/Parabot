package org.parabot;

import org.junit.Assert;
import org.junit.Test;
import org.parabot.core.Directories;

import java.io.File;
import java.io.IOException;

/**
 * @author JKetelaar
 */
public class CacheValidationTest {

    @Test
    public void test() throws IOException {
        Directories.validate();

        File fileOne = new File(Directories.getCachePath(), "should-exist.tmp");
        File fileTwo = new File(Directories.getCachePath(), "should-not-exist.tmp");

        fileOne.createNewFile();
        fileTwo.createNewFile();

        fileTwo.setLastModified(System.currentTimeMillis() / 1000 - 350000);

        Directories.clearCache(259200, false);

        Assert.assertTrue(fileOne.exists());
        Assert.assertTrue(!fileTwo.exists());
    }
}
