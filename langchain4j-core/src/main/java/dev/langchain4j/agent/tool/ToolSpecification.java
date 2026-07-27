/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agent.tool;

import dev.langchain4j.internal.ToolSpecificationJsonUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ToolSpecification {
    public static final String METADATA_SEARCH_BEHAVIOR = "searchBehavior";
    private final String name;
    private final String description;
    private final JsonObjectSchema parameters;
    private final Map<String, Object> metadata;
    private final Boolean strict;

    private ToolSpecification(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.parameters = builder.parameters;
        this.metadata = Utils.copy(builder.metadata);
        this.strict = builder.strict;
    }

    public String name() {
        return this.name;
    }

    public String description() {
        return this.description;
    }

    public JsonObjectSchema parameters() {
        return this.parameters;
    }

    public Map<String, Object> metadata() {
        return this.metadata;
    }

    public Boolean strict() {
        return this.strict;
    }

    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof ToolSpecification && this.equalTo((ToolSpecification)another);
    }

    private boolean equalTo(ToolSpecification another) {
        return Objects.equals(this.name, another.name) && Objects.equals(this.description, another.description) && Objects.equals(this.parameters, another.parameters) && Objects.equals(this.metadata, another.metadata) && Objects.equals(this.strict, another.strict);
    }

    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.name);
        h += (h << 5) + Objects.hashCode(this.description);
        h += (h << 5) + Objects.hashCode(this.parameters);
        h += (h << 5) + Objects.hashCode(this.metadata);
        h += (h << 5) + Objects.hashCode(this.strict);
        return h;
    }

    public String toString() {
        return "ToolSpecification { name = " + Utils.quoted(this.name) + ", description = " + Utils.quoted(this.description) + ", parameters = " + this.parameters + ", metadata = " + this.metadata + ", strict = " + this.strict + " }";
    }

    public String toJson() {
        return ToolSpecificationJsonUtils.toJson(this);
    }

    public static ToolSpecification fromJson(String json) {
        return ToolSpecificationJsonUtils.fromJson(json);
    }

    public Builder toBuilder() {
        return ToolSpecification.builder().name(this.name).description(this.description).parameters(this.parameters).metadata(Utils.mutableCopy(this.metadata)).strict(this.strict);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private String description;
        private JsonObjectSchema parameters;
        private Map<String, Object> metadata = new HashMap<String, Object>();
        private Boolean strict;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder parameters(JsonObjectSchema parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder addMetadata(String key, Object value) {
            this.metadata.put(key, value);
            return this;
        }

        public Builder strict(Boolean strict) {
            this.strict = strict;
            return this;
        }

        public ToolSpecification build() {
            return new ToolSpecification(this);
        }
    }
}

