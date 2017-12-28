package org.parabot.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Everel
 */
public class SizeInputStream extends InputStream {
    public  int              bytesRead;
    private InputStream      in;
    private long             startTime;
    private double           size;

    public SizeInputStream(InputStream in, int size) {
        this.in = in;
        this.size = size;
        this.startTime = System.currentTimeMillis();
    }

    public int available() {
        return ((int) size - bytesRead);
    }

    public int read() throws IOException {
        int b = in.read();
        if (b != -1) {
            bytesRead++;
        }
        return b;
    }

    public int read(byte[] b) throws IOException {
        int read = in.read(b);
        bytesRead += read;
        return read;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int read = in.read(b, off, len);
        bytesRead += read;
        return read;
    }
}

