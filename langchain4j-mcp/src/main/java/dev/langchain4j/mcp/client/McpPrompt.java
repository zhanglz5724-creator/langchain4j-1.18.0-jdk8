/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.mcp.client.McpIcon;
import dev.langchain4j.mcp.client.McpPromptArgument;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class McpPrompt {
    private final String name;
    private final String description;
    private final List<McpPromptArgument> arguments;
    private final Map<String, Object> metadata;
    private final List<McpIcon> icons;

    public McpPrompt(@JsonProperty(value="name") String name, @JsonProperty(value="description") String description, @JsonProperty(value="arguments") List<McpPromptArgument> arguments) {
        this(name, description, arguments, null, null);
    }

    @JsonCreator
    public McpPrompt(@JsonProperty(value="name") String name, @JsonProperty(value="description") String description, @JsonProperty(value="arguments") List<McpPromptArgument> arguments, @JsonProperty(value="_meta") Map<String, Object> metadata, @JsonProperty(value="icons") List<McpIcon> icons) {
        this.name = name;
        this.description = description;
        this.arguments = arguments;
        this.metadata = Utils.copy(metadata);
        this.icons = icons == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<McpIcon>(icons));
    }

    public String name() {
        return this.name;
    }

    public String description() {
        return this.description;
    }

    public List<McpPromptArgument> arguments() {
        return this.arguments;
    }

    public Map<String, Object> metadata() {
        return this.metadata;
    }

    public List<McpIcon> icons() {
        return this.icons;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        McpPrompt that = (McpPrompt)obj;
        return Objects.equals(this.name, that.name) && Objects.equals(this.description, that.description) && Objects.equals(this.arguments, that.arguments) && Objects.equals(this.metadata, that.metadata) && Objects.equals(this.icons, that.icons);
    }

    public int hashCode() {
        return Objects.hash(this.name, this.description, this.arguments, this.metadata, this.icons);
    }

    public String toString() {
        return "McpPrompt[name=" + this.name + ", description=" + this.description + ", arguments=" + this.arguments + ", metadata=" + this.metadata + ", icons=" + this.icons + ']';
    }
}

