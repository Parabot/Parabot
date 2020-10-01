package org.parabot.core.desc;

/**
 * Holds information about a server
 *
 * @author Everel
 */
public class ServerDescription implements Comparable<ServerDescription> {
    private final String serverName;
    private final String author;
    private final double revision;
    public int uuid;

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

    @Override
    public int compareTo(ServerDescription o) {
        if (this.getServerName().equalsIgnoreCase(o.getServerName())) {
            if (getAuthor().equals(o.getAuthor())) {
                return Double.compare(o.getRevision(), getRevision());
            }
            return getAuthor().compareTo(o.getAuthor());
        }
        return this.getServerName().compareTo(o.getServerName());
    }

}
