/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.request.json;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import java.util.Objects;

public class JsonIntegerSchema
implements JsonSchemaElement {
    private final String description;

    public JsonIntegerSchema() {
        this.description = null;
    }

    public JsonIntegerSchema(Builder builder) {
        this.description = builder.description;
    }

    @Override
    public String description() {
        return this.description;
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
        JsonIntegerSchema that = (JsonIntegerSchema)o;
        return Objects.equals(this.description, that.description);
    }

    public int hashCode() {
        return Objects.hash(this.description);
    }

    public String toString() {
        return "JsonIntegerSchema {description = " + Utils.quoted(this.description) + " }";
    }

    public static class Builder {
        private String description;

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public JsonIntegerSchema build() {
            return new JsonIntegerSchema(this);
        }
    }
}

