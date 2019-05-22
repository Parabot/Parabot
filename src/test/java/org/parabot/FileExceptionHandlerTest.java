package org.parabot;

import org.junit.Assert;
import org.junit.Test;
import org.parabot.environment.handlers.exceptions.ExceptionHandler;
import org.parabot.environment.handlers.exceptions.FileExceptionHandler;

import java.io.File;

public class FileExceptionHandlerTest {

    @Test
    public void test() {
        FileExceptionHandler serverHandler = new FileExceptionHandler(ExceptionHandler.ExceptionType.SERVER);
        serverHandler.setIgnored(true);

        File[] reports = serverHandler.getReportsDirectory().listFiles();
        int reportCount = 0;
        if (reports != null) {
            reportCount = reports.length;
        }

        Exception exception = new Exception("Test");
        serverHandler.handle(exception);

        Assert.assertTrue(serverHandler.getReportsDirectory().listFiles().length > reportCount);
    }
}
