/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.embedding.request;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Objects;

@Experimental
public class EmbeddingParameter<T> {
    private final String name;
    private final Class<T> type;

    public EmbeddingParameter(String name, Class<T> type) {
        this.name = ValidationUtils.ensureNotBlank(name, "name");
        this.type = ValidationUtils.ensureNotNull(type, "type");
    }

    public String name() {
        return this.name;
    }

    public Class<T> type() {
        return this.type;
    }

    public T cast(Object value) {
        return this.type.cast(value);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        EmbeddingParameter that = (EmbeddingParameter)o;
        return Objects.equals(this.name, that.name);
    }

    public int hashCode() {
        return Objects.hash(this.name);
    }

    public String toString() {
        return this.name;
    }
}

