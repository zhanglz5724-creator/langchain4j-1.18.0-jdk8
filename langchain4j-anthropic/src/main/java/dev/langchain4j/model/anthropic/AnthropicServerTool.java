/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.model.anthropic;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.Utils;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Experimental
public class AnthropicServerTool {
    private final String type;
    private final String name;
    private final Map<String, Object> attributes;

    public AnthropicServerTool(Builder builder) {
        this.type = builder.type;
        this.name = builder.name;
        this.attributes = Utils.copy((Map)builder.attributes);
    }

    public String type() {
        return this.type;
    }

    public String name() {
        return this.name;
    }

    public Map<String, Object> attributes() {
        return this.attributes;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AnthropicServerTool that = (AnthropicServerTool)o;
        return Objects.equals(this.type, that.type) && Objects.equals(this.name, that.name) && Objects.equals(this.attributes, that.attributes);
    }

    public int hashCode() {
        return Objects.hash(this.type, this.name, this.attributes);
    }

    public String toString() {
        return "AnthropicServerTool{type='" + this.type + '\'' + ", name='" + this.name + '\'' + ", attributes=" + this.attributes + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String type;
        private String name;
        private Map<String, Object> attributes;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder attributes(Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }

        public Builder addAttribute(String key, Object value) {
            if (this.attributes == null) {
                this.attributes = new LinkedHashMap<String, Object>();
            }
            this.attributes.put(key, value);
            return this;
        }

        public AnthropicServerTool build() {
            return new AnthropicServerTool(this);
        }
    }
}

