/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.request.json;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import java.util.Objects;

public class JsonSchema {
    private final String name;
    private final JsonSchemaElement rootElement;

    private JsonSchema(Builder builder) {
        this.name = builder.name;
        this.rootElement = builder.rootElement;
    }

    public String name() {
        return this.name;
    }

    public JsonSchemaElement rootElement() {
        return this.rootElement;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        JsonSchema that = (JsonSchema)o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.rootElement, that.rootElement);
    }

    public int hashCode() {
        return Objects.hash(this.name, this.rootElement);
    }

    public String toString() {
        return "JsonSchema { name = " + Utils.quoted(this.name) + ", rootElement = " + this.rootElement + " }";
    }

    public static class Builder {
        private String name;
        private JsonSchemaElement rootElement;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder rootElement(JsonSchemaElement rootElement) {
            this.rootElement = rootElement;
            return this;
        }

        public JsonSchema build() {
            return new JsonSchema(this);
        }
    }
}

