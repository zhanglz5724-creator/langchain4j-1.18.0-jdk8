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

public class JsonEnumSchema
implements JsonSchemaElement {
    private final String description;
    private final List<String> enumValues;

    public JsonEnumSchema(Builder builder) {
        this.description = builder.description;
        this.enumValues = Utils.copy(ValidationUtils.ensureNotEmpty(builder.enumValues, "enumValues"));
    }

    @Override
    public String description() {
        return this.description;
    }

    public List<String> enumValues() {
        return this.enumValues;
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
        JsonEnumSchema that = (JsonEnumSchema)o;
        return Objects.equals(this.description, that.description) && Objects.equals(this.enumValues, that.enumValues);
    }

    public int hashCode() {
        return Objects.hash(this.description, this.enumValues);
    }

    public String toString() {
        return "JsonEnumSchema {description = " + Utils.quoted(this.description) + ", enumValues = " + this.enumValues + " }";
    }

    public static class Builder {
        private String description;
        private List<String> enumValues;

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder enumValues(List<String> enumValues) {
            this.enumValues = enumValues;
            return this;
        }

        public Builder enumValues(String ... enumValues) {
            return this.enumValues(Arrays.asList(enumValues));
        }

        public JsonEnumSchema build() {
            return new JsonEnumSchema(this);
        }
    }
}

