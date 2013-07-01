package org.parabot.core.desc;

public class ScriptDescription {
	public String scriptName = null;
	public String author = null;
	public String category = null;
	public double version = 0;
	public String description = null;
	public String[] servers = null;
	public int scriptIndex = -1;

	public ScriptDescription(final String scriptName, final String author,
			final String category, final double version,
			final String description, final String[] servers,
			final int scriptIndex) {
		this.scriptName = scriptName;
		this.author = author;
		this.category = category;
		this.version = version;
		this.description = description;
		this.servers = servers;
		this.scriptIndex = scriptIndex;
	}

}
