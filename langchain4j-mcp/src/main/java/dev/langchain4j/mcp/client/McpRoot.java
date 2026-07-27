package dev.langchain4j.mcp.client;
public class McpRoot {
    private final String name;
    private final String uri;

    public McpRoot(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        McpRoot that = (McpRoot) o;
        return java.util.Objects.equals(this.name, that.name) && java.util.Objects.equals(this.uri, that.uri);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, uri);
    }

    @Override
    public String toString() {
        return "McpRoot{"name=" + name + , "uri=" + uri + "}"";
    }

}
