/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.request.json;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import java.util.Objects;

public class JsonReferenceSchema
implements JsonSchemaElement {
    private final String reference;

    public JsonReferenceSchema(Builder builder) {
        this.reference = builder.reference;
    }

    public String reference() {
        return this.reference;
    }

    @Override
    public String description() {
        return null;
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
        JsonReferenceSchema that = (JsonReferenceSchema)o;
        return Objects.equals(this.reference, that.reference);
    }

    public int hashCode() {
        return Objects.hash(this.reference);
    }

    public String toString() {
        return "JsonReferenceSchema {reference = " + Utils.quoted(this.reference) + " }";
    }

    public static class Builder {
        private String reference;

        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        public JsonReferenceSchema build() {
            return new JsonReferenceSchema(this);
        }
    }
}

