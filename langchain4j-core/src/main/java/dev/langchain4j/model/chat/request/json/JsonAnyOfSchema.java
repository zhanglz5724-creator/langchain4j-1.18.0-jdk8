/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.request.json;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class JsonAnyOfSchema
implements JsonSchemaElement {
    private final String description;
    private final List<JsonSchemaElement> anyOf;

    public JsonAnyOfSchema(Builder builder) {
        this.description = builder.description;
        this.anyOf = Utils.copy(ValidationUtils.ensureNotEmpty(builder.anyOf, "anyOf"));
    }

    @Override
    public String description() {
        return this.description;
    }

    public List<JsonSchemaElement> anyOf() {
        return this.anyOf;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JsonAnyOfSchema)) {
            return false;
        }
        JsonAnyOfSchema that = (JsonAnyOfSchema)o;
        return Objects.equals(this.description, that.description) && Objects.equals(this.anyOf, that.anyOf);
    }

    public int hashCode() {
        return Objects.hash(this.description, this.anyOf);
    }

    public String toString() {
        return "JsonAnyOfSchema {description = " + Utils.quoted(this.description) + ", anyOf = " + this.anyOf + " }";
    }

    public static class Builder {
        private String description;
        private List<JsonSchemaElement> anyOf;

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder anyOf(List<JsonSchemaElement> anyOf) {
            this.anyOf = anyOf;
            return this;
        }

        public Builder anyOf(JsonSchemaElement ... anyOf) {
            return this.anyOf(Arrays.asList(anyOf));
        }

        public JsonAnyOfSchema build() {
            return new JsonAnyOfSchema(this);
        }
    }
}

