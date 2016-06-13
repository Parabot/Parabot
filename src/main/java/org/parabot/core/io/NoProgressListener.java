package org.parabot.core.io;

/**
 * @author JKetelaar
 */
public class NoProgressListener implements ProgressListener {
    @Override
    public void onProgressUpdate(double value) {

    }

    @Override
    public void updateDownloadSpeed(double mbPerSecond) {

    }
}
