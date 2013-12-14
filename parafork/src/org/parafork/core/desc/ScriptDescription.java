package org.parafork.core.desc;

/**
 * Holds information about a script
 * 
 * @author Everel
 * 
 */
public class ScriptDescription {
	public String scriptName = null;
	public String author = null;
	public String category = null;
	public double version = 0;
	public String description = null;
	public String[] servers = null;
	public String isVip = null;
	public String isPremium = null;
	public int sdnId = -1;
	public String jarName = null;

	/**
	 * The ScriptManifest
	 * 
	 * @param scriptName
	 * @param author
	 * @param category
	 * @param version
	 * @param description
	 * @param servers
	 */
	public ScriptDescription(final String scriptName, final String author,
			final String category, final double version,
			final String description, final String[] servers) {
		this(scriptName, author, category, version, description, servers, null,
				null, -1, null);
	}

	/**
	 * Used for SDN script (see SDNScripts parser)
	 * 
	 * @param jarName
	 * @param scriptName
	 * @param author
	 * @param category
	 * @param version
	 * @param description
	 * @param servers
	 * @param sdnId
	 */
	public ScriptDescription(final String jarName, final String scriptName,
			final String author, final String category, final double version,
			final String description, final String[] servers, final int sdnId) {
		this(scriptName, author, category, version, description, servers, null,
				null, sdnId, jarName);
	}

	/**
	 * Used by bot (java scripts and python scripts) and SDN Manager (sdn
	 * manager is a private program)
	 * 
	 * @param scriptName
	 * @param author
	 * @param category
	 * @param version
	 * @param description
	 * @param servers
	 * @param vip
	 * @param premium
	 */
	public ScriptDescription(final String scriptName, final String author,
			final String category, final double version,
			final String description, final String[] servers, final String vip,
			final String premium) {
		this(scriptName, author, category, version, description, servers, vip,
				premium, -1, null);
	}

	/**
	 * Main constructor
	 * 
	 * @param scriptName
	 * @param author
	 * @param category
	 * @param version
	 * @param description
	 * @param servers
	 * @param vip
	 * @param premium
	 * @param sdnId
	 * @param jarName
	 */
	public ScriptDescription(final String scriptName, final String author,
			final String category, final double version,
			final String description, final String[] servers, final String vip,
			final String premium, final int sdnId, final String jarName) {
		this.scriptName = scriptName;
		this.author = author;
		this.category = category;
		this.version = version;
		this.description = description;
		this.servers = servers;
		this.isVip = vip;
		this.isPremium = premium;
		this.sdnId = sdnId;
		this.jarName = jarName;
	}

	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append("[name: ").append(this.scriptName).append(", author: ")
				.append(this.author).append(", category: ")
				.append(this.category).append(", version: ")
				.append(this.version).append(", description: ")
				.append(this.description).append(", servers: ");
		for (int i = 0; i < this.servers.length; i++) {
			b.append(this.servers[i]);
			if (i < (this.servers.length - 1)) {
				b.append(" ");
			}
		}
		b.append(", vip: ")
				.append(this.isVip == null ? "unknown" : this.isVip)
				.append(", premium: ")
				.append(this.isPremium == null ? "unknown" : this.isPremium)
				.append(", sdn id: ")
				.append(this.sdnId == -1 ? "unknown" : Integer
						.toString(this.sdnId)).append(", jarname: ")
				.append(this.jarName == null ? "unknown" : this.jarName)
				.append("]");

		return b.toString();
	}
}
