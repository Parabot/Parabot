package org.parabot.core.desc;

public class ServerDescription {
	public String serverName = null;
	public String author = null;
	public int revision = 0;
	public int providerIndex = -1;
	
	public ServerDescription(final String serverName, final String author, final int revision, final int providerIndex) {
		this.serverName = serverName;
		this.author = author;
		this.revision = revision;
		this.providerIndex = providerIndex;
	}

}
