package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Collections;

/**
 * The 'Icon' object from the MCP protocol schema.
 */
public class McpIcon {
    private final Object @JsonProperty("mimeType";

    public McpIcon(Object @JsonProperty("mimeType") {
        this.@JsonProperty("mimeType" = @JsonProperty("mimeType";
    }

    public Object get@JsonProperty("mimeType"() {
        return @JsonProperty("mimeType";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        McpIcon that = (McpIcon) o;
        return java.util.Objects.equals(this.@JsonProperty("mimeType", that.@JsonProperty("mimeType");
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(@JsonProperty("mimeType");
    }

    @Override
    public String toString() {
        return "McpIcon{"@JsonProperty("mimeType"=" + @JsonProperty("mimeType" + "}"";
    }


    @JsonCreator
    public McpIcon {
        sizes = sizes == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<>(sizes);
    }
}
