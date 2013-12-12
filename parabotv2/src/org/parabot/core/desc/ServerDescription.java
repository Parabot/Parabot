package org.parabot.core.desc;

/**
 * 
 * @author Everel
 * 
 */
public class ServerDescription {
	private String serverName = null;
	private String author = null;
	private double revision = 0;

	public ServerDescription(final String serverName, final String author,
			final double revision) {
		this.serverName = serverName;
		this.author = author;
		this.revision = revision;
	}

	public String getServerName() {
		return this.serverName;
	}

	public String getAuthor() {
		return this.author;
	}

	public double getRevision() {
		return this.revision;
	}

	@Override
	public String toString() {
		return String.format("[Server: %s, Author: %s, Revision: %.2f]",
				this.serverName, this.author, this.revision);
	}

}
