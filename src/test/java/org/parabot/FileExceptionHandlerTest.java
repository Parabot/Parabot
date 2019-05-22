package org.parabot;

import org.junit.Test;
import org.parabot.environment.handlers.exceptions.ExceptionHandler;
import org.parabot.environment.handlers.exceptions.FileExceptionHandler;

public class FileExceptionHandlerTest {

    @Test
    public void manualTest() {
        FileExceptionHandler handler = new FileExceptionHandler(ExceptionHandler.ExceptionType.CLIENT);
        handler.setIgnored(true);

        Exception exception = new NullPointerException("Manual test");
        handler.handle(exception);
    }

    @Test
    public void threadHandlerTest() {
        FileExceptionHandler handler = new FileExceptionHandler(ExceptionHandler.ExceptionType.CLIENT);
        handler.setIgnored(true);

        Thread thread = new Thread() {
            @Override
            public void run() throws NullPointerException {
                throw new NullPointerException("Thread test");
            }
        };

        thread.setUncaughtExceptionHandler(handler);
        thread.start();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
