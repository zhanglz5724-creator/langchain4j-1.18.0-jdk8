/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.mcp.registryclient.model;

public class McpRepository {
    private String id;
    private String source;
    private String subfolder;
    private String url;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSubfolder() {
        return this.subfolder;
    }

    public void setSubfolder(String subfolder) {
        this.subfolder = subfolder;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        return "McpRepository{id='" + this.id + '\'' + ", source='" + this.source + '\'' + ", subfolder='" + this.subfolder + '\'' + ", url='" + this.url + '\'' + '}';
    }
}

