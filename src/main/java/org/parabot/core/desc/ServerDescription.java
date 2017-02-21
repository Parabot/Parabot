package org.parabot.core.desc;

import org.parabot.environment.api.utils.StringUtils;

/**
 * 
 * Holds information about a server
 * 
 * @author Everel
 * 
 */
public class ServerDescription implements Comparable<ServerDescription> {

	private String serverName;
	private String[] authors;
	private double revision;

	public ServerDescription(final String serverName, final String[] authors,
			final double revision) {
		this.serverName = serverName;
		this.authors = authors;
		this.revision = revision;
	}

	public ServerDescription(final String serverName, final String author,
							 final double revision) {
		this.serverName = serverName;
		this.authors = new String[]{author};
		this.revision = revision;
	}

	public String getServerName() {
		return this.serverName;
	}

	public String[] getAuthors() {
		return this.authors;
	}

	public double getRevision() {
		return this.revision;
	}

	@Override
	public String toString() {
		return String.format("[Server: %s, Author: %s, Revision: %.2f]",
				this.serverName, this.authors.length > 0 ? StringUtils.implode(", ", this.authors) : "", this.revision);
	}

	@Override
	public int compareTo(ServerDescription o) {
		return this.getServerName().compareTo(o.getServerName());
	}

}
