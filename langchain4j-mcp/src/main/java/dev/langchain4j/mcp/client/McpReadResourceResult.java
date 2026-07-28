/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.mcp.client.McpResourceContents;
import java.util.List;
import java.util.Objects;

public class McpReadResourceResult {
    private final List<McpResourceContents> contents;

    @JsonCreator
    public McpReadResourceResult(@JsonProperty(value="contents") List<McpResourceContents> contents) {
        this.contents = contents;
    }

    public List<McpResourceContents> contents() {
        return this.contents;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        McpReadResourceResult that = (McpReadResourceResult)obj;
        return Objects.equals(this.contents, that.contents);
    }

    public int hashCode() {
        return Objects.hash(this.contents);
    }

    public String toString() {
        return "McpReadResourceResult[contents=" + this.contents + ']';
    }
}

