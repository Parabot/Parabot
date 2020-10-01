package org.parabot.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Everel
 */
public class SizeInputStream extends InputStream {
    private final ProgressListener l;
    private final InputStream in;
    private final long startTime;
    private final double size;
    public int bytesRead;

    public SizeInputStream(InputStream in, int size, ProgressListener l) {
        this.in = in;
        this.size = size;
        this.l = l;
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
        updateListener();
        return b;
    }

    public int read(byte[] b) throws IOException {
        int read = in.read(b);
        bytesRead += read;
        updateListener();
        return read;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int read = in.read(b, off, len);
        bytesRead += read;
        updateListener();
        return read;
    }

    private void updateListener() {
        if (l != null) {
            double percent = (bytesRead / size) * 100.0D;
            l.onProgressUpdate(percent);

            long curTime = System.currentTimeMillis();
            double timeSeconds = (curTime - startTime) / 1000.0D;
            double speed = bytesRead / (1024.0D * 1024.0D) / timeSeconds;
            l.updateDownloadSpeed(speed);
        }
    }
}

