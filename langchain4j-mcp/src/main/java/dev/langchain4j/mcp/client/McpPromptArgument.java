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
import java.util.Objects;

public class McpPromptArgument {
    private final String name;
    private final String description;
    private final boolean required;

    @JsonCreator
    public McpPromptArgument(@JsonProperty(value="name") String name, @JsonProperty(value="description") String description, @JsonProperty(value="required") boolean required) {
        this.name = name;
        this.description = description;
        this.required = required;
    }

    public String name() {
        return this.name;
    }

    public String description() {
        return this.description;
    }

    public boolean required() {
        return this.required;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        McpPromptArgument that = (McpPromptArgument)obj;
        return Objects.equals(this.name, that.name) && Objects.equals(this.description, that.description) && this.required == that.required;
    }

    public int hashCode() {
        return Objects.hash(this.name, this.description, this.required);
    }

    public String toString() {
        return "McpPromptArgument[name=" + this.name + ", description=" + this.description + ", required=" + this.required + ']';
    }
}

