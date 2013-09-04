package org.parabot.core.io;

public interface ProgressListener {
	
	public void onProgressUpdate(double value);
	
	public void updateDownloadSpeed(double mbPerSecond);

}
