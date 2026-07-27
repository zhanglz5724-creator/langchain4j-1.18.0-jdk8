/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.request.json;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.json.JsonBooleanSchema;
import dev.langchain4j.model.chat.request.json.JsonEnumSchema;
import dev.langchain4j.model.chat.request.json.JsonIntegerSchema;
import dev.langchain4j.model.chat.request.json.JsonNumberSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.request.json.JsonStringSchema;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JsonObjectSchema
implements JsonSchemaElement {
    private final String description;
    private final Map<String, JsonSchemaElement> properties;
    private final List<String> required;
    private final Boolean additionalProperties;
    private final Map<String, JsonSchemaElement> definitions;

    public JsonObjectSchema(Builder builder) {
        this.description = builder.description;
        this.properties = Utils.copy(builder.properties);
        this.required = Utils.copy(builder.required);
        this.additionalProperties = builder.additionalProperties;
        this.definitions = Utils.copy(builder.definitions);
    }

    @Override
    public String description() {
        return this.description;
    }

    public Map<String, JsonSchemaElement> properties() {
        return this.properties;
    }

    public List<String> required() {
        return this.required;
    }

    public Boolean additionalProperties() {
        return this.additionalProperties;
    }

    public Map<String, JsonSchemaElement> definitions() {
        return this.definitions;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return JsonObjectSchema.builder().description(this.description).addProperties(this.properties).required((List<String>)(this.required != null ? new ArrayList<String>(this.required) : null)).additionalProperties(this.additionalProperties).definitions((Map<String, JsonSchemaElement>)(this.definitions != null ? new LinkedHashMap<String, JsonSchemaElement>(this.definitions) : null));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        JsonObjectSchema that = (JsonObjectSchema)o;
        return Objects.equals(this.description, that.description) && Objects.equals(this.properties, that.properties) && Objects.equals(this.required, that.required) && Objects.equals(this.additionalProperties, that.additionalProperties) && Objects.equals(this.definitions, that.definitions);
    }

    public int hashCode() {
        return Objects.hash(this.description, this.properties, this.required, this.additionalProperties, this.definitions);
    }

    public String toString() {
        return "JsonObjectSchema {description = " + Utils.quoted(this.description) + ", properties = " + this.properties + ", required = " + this.required + ", additionalProperties = " + this.additionalProperties + ", definitions = " + this.definitions + " }";
    }

    public static class Builder {
        private String description;
        private final Map<String, JsonSchemaElement> properties = new LinkedHashMap<String, JsonSchemaElement>();
        private List<String> required;
        private Boolean additionalProperties;
        private Map<String, JsonSchemaElement> definitions;

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder addProperties(Map<String, JsonSchemaElement> properties) {
            this.properties.putAll(properties);
            return this;
        }

        public Builder addProperty(String name, JsonSchemaElement jsonSchemaElement) {
            this.properties.put(name, jsonSchemaElement);
            return this;
        }

        public Builder addStringProperty(String name) {
            this.properties.put(name, new JsonStringSchema());
            return this;
        }

        public Builder addStringProperty(String name, String description) {
            this.properties.put(name, JsonStringSchema.builder().description(description).build());
            return this;
        }

        public Builder addIntegerProperty(String name) {
            this.properties.put(name, new JsonIntegerSchema());
            return this;
        }

        public Builder addIntegerProperty(String name, String description) {
            this.properties.put(name, JsonIntegerSchema.builder().description(description).build());
            return this;
        }

        public Builder addNumberProperty(String name) {
            this.properties.put(name, new JsonNumberSchema());
            return this;
        }

        public Builder addNumberProperty(String name, String description) {
            this.properties.put(name, JsonNumberSchema.builder().description(description).build());
            return this;
        }

        public Builder addBooleanProperty(String name) {
            this.properties.put(name, new JsonBooleanSchema());
            return this;
        }

        public Builder addBooleanProperty(String name, String description) {
            this.properties.put(name, JsonBooleanSchema.builder().description(description).build());
            return this;
        }

        public Builder addEnumProperty(String name, List<String> enumValues) {
            this.properties.put(name, JsonEnumSchema.builder().enumValues(enumValues).build());
            return this;
        }

        public Builder addEnumProperty(String name, List<String> enumValues, String description) {
            this.properties.put(name, JsonEnumSchema.builder().enumValues(enumValues).description(description).build());
            return this;
        }

        public Builder required(List<String> required) {
            this.required = required;
            return this;
        }

        public Builder required(String ... required) {
            return this.required(Arrays.asList(required));
        }

        public Builder additionalProperties(Boolean additionalProperties) {
            this.additionalProperties = additionalProperties;
            return this;
        }

        public Builder definitions(Map<String, JsonSchemaElement> definitions) {
            this.definitions = definitions;
            return this;
        }

        public JsonObjectSchema build() {
            return new JsonObjectSchema(this);
        }
    }
}

