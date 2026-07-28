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
import dev.langchain4j.mcp.client.McpPromptMessage;
import java.util.List;
import java.util.Objects;

public class McpGetPromptResult {
    private final String description;
    private final List<McpPromptMessage> messages;

    @JsonCreator
    public McpGetPromptResult(@JsonProperty(value="description") String description, @JsonProperty(value="messages") List<McpPromptMessage> messages) {
        this.description = description;
        this.messages = messages;
    }

    public String description() {
        return this.description;
    }

    public List<McpPromptMessage> messages() {
        return this.messages;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        McpGetPromptResult that = (McpGetPromptResult)obj;
        return Objects.equals(this.description, that.description) && Objects.equals(this.messages, that.messages);
    }

    public int hashCode() {
        return Objects.hash(this.description, this.messages);
    }

    public String toString() {
        return "McpGetPromptResult[description=" + this.description + ", messages=" + this.messages + ']';
    }
}

