/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.request.json;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import java.util.Objects;

public class JsonRawSchema
implements JsonSchemaElement {
    private final String schema;

    public JsonRawSchema(Builder builder) {
        this.schema = ValidationUtils.ensureNotBlank(builder.schema, "schema");
    }

    @Override
    public String description() {
        return null;
    }

    public String schema() {
        return this.schema;
    }

    public static JsonRawSchema from(String schema) {
        return JsonRawSchema.builder().schema(schema).build();
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
        JsonRawSchema that = (JsonRawSchema)o;
        return Objects.equals(this.schema, that.schema);
    }

    public int hashCode() {
        return Objects.hash(this.schema);
    }

    public String toString() {
        return "JsonRawSchema {schema = " + Utils.quoted(this.schema) + " }";
    }

    public static class Builder {
        public String schema;

        public Builder schema(String schema) {
            this.schema = schema;
            return this;
        }

        public JsonRawSchema build() {
            return new JsonRawSchema(this);
        }
    }
}

