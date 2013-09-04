package org.parabot.core.desc;

/**
 * 
 * @author Everel
 *
 */
public class ServerDescription {
	public String serverName = null;
	public String author = null;
	public double revision = 0;

	public ServerDescription(final String serverName, final String author,
			final double revision) {
		this.serverName = serverName;
		this.author = author;
		this.revision = revision;
	}
	
	@Override
	public String toString() {
		return String.format("[Server: %s, Author: %s, Revision: %.1f]", this.serverName, this.author, this.revision);
	}

}
