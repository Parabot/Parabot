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

    @Override
    public void updateMessage(String message) {

    }

    @Override
    public void updateMessageAndProgress(String message, double progress) {

    }

    @Override
    public double getCurrentProgress() {
        return 0;
    }
}
