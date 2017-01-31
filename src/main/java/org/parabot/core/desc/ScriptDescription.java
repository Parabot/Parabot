package org.parabot.core.desc;

/**
 * Holds information about a script
 *
 * @author Everel
 */
public class ScriptDescription implements Comparable<ScriptDescription> {
    public String scriptName;
    public String author;
    public String category;
    public double version;
    public String description;
    public String[] servers;
    public String isVip;
    public String isPremium;
    public int bdnId;

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
                null, -1);
    }

    /**
     * Used for BDN script (see BDNScripts parser)
     *
     * @param scriptName
     * @param author
     * @param category
     * @param version
     * @param description
     * @param servers
     * @param bdnId
     */
    public ScriptDescription(final String scriptName,
                             final String author, final String category, final double version,
                             final String description, final String[] servers, final int bdnId) {
        this(scriptName, author, category, version, description, servers, null,
                null, bdnId);
    }

    /**
     * Used by bot (java scripts and python scripts) and BDN Manager (bdn
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
                premium, -1);
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
     * @param bdnId
     */
    public ScriptDescription(final String scriptName, final String author,
                             final String category, final double version,
                             final String description, final String[] servers, final String vip,
                             final String premium, final int bdnId) {
        this.scriptName = scriptName;
        this.author = author;
        this.category = category;
        this.version = version;
        this.description = description;
        this.servers = servers;
        this.isVip = vip;
        this.isPremium = premium;
        this.bdnId = bdnId;
    }

    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder();
        b.append("[name: ").append(this.scriptName).append(", author: ")
                .append(this.author).append(", category: ")
                .append(this.category).append(", version: ")
                .append(this.version).append(", description: ")
                .append(this.description).append(", servers: ");
        if (this.servers != null) {
            for (int i = 0; i < this.servers.length; i++) {
                b.append(this.servers[i]);
                if (i < (this.servers.length - 1)) {
                    b.append(" ");
                }
            }
        }
        b.append(", vip: ")
                .append(this.isVip == null ? "unknown" : this.isVip)
                .append(", premium: ")
                .append(this.isPremium == null ? "unknown" : this.isPremium)
                .append(", bdn id: ")
                .append(this.bdnId == -1 ? "unknown" : Integer
                        .toString(this.bdnId))
                .append("]");

        return b.toString();
    }

    @Override
    public int compareTo(ScriptDescription o) {
        return scriptName.compareTo(o.scriptName);
    }
}
