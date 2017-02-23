package org.parabot.core.desc;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.parabot.environment.api.utils.StringUtils;

import java.util.HashMap;

/**
 * Holds information about a server
 *
 * @author Everel
 */
public class ServerDescription implements Comparable<ServerDescription> {

    private int                     id;
    private String                  serverName;
    private String[]                authors;
    private double                  revision;
    private String                  description;
    private HashMap<String, String> details;

    public ServerDescription(final int id, final String serverName, final String[] authors,
                             final double revision, final String description, final JSONArray details) {
        this.id = id;
        this.serverName = serverName;
        this.authors = authors;
        this.revision = revision;
        this.description = description;

        this.details = new HashMap<>();
        for (Object o : details) {
            JSONObject detail = (JSONObject) o;

            String name  = (String) detail.get("name");
            String value = (String) detail.get("value");

            this.details.put(name, value);
        }
    }

    public ServerDescription(int id, final String serverName, final String author,
                             final double revision) {
        this.id = id;
        this.serverName = serverName;
        this.authors = new String[]{ author };
        this.revision = revision;
    }

    public int getId() {
        return id;
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

    public String getDescription() {
        return description;
    }

    public HashMap<String, String> getDetails() {
        return details;
    }

    public String getDetail(String key) {
        return details.get(key);
    }
}
