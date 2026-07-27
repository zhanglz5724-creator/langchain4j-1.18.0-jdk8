/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.request.json;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import java.util.Objects;

public class JsonArraySchema
implements JsonSchemaElement {
    private final String description;
    private final JsonSchemaElement items;

    public JsonArraySchema(Builder builder) {
        this.description = builder.description;
        this.items = builder.items;
    }

    @Override
    public String description() {
        return this.description;
    }

    public JsonSchemaElement items() {
        return this.items;
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
        JsonArraySchema that = (JsonArraySchema)o;
        return Objects.equals(this.description, that.description) && Objects.equals(this.items, that.items);
    }

    public int hashCode() {
        return Objects.hash(this.description, this.items);
    }

    public String toString() {
        return "JsonArraySchema {description = " + Utils.quoted(this.description) + ", items = " + this.items + " }";
    }

    public static class Builder {
        private String description;
        private JsonSchemaElement items;

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder items(JsonSchemaElement items) {
            this.items = items;
            return this;
        }

        public JsonArraySchema build() {
            return new JsonArraySchema(this);
        }
    }
}

