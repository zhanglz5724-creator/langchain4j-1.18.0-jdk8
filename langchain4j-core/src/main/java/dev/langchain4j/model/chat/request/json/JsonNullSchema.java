/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.request.json;

import dev.langchain4j.model.chat.request.json.JsonSchemaElement;

public class JsonNullSchema
implements JsonSchemaElement {
    @Override
    public String description() {
        return null;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o != null && this.getClass() == o.getClass();
    }

    public int hashCode() {
        return JsonNullSchema.class.hashCode();
    }

    public String toString() {
        return "JsonNullSchema {}";
    }
}

