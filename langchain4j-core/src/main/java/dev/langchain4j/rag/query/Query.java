/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.query;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.rag.query.Metadata;
import java.util.Objects;

public class Query {
    private final String text;
    private final Metadata metadata;

    public Query(String text) {
        this.text = ValidationUtils.ensureNotBlank(text, "text");
        this.metadata = null;
    }

    public Query(String text, Metadata metadata) {
        this.text = ValidationUtils.ensureNotBlank(text, "text");
        this.metadata = ValidationUtils.ensureNotNull(metadata, "metadata");
    }

    public String text() {
        return this.text;
    }

    public Metadata metadata() {
        return this.metadata;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Query that = (Query)o;
        return Objects.equals(this.text, that.text) && Objects.equals(this.metadata, that.metadata);
    }

    public int hashCode() {
        return Objects.hash(this.text, this.metadata);
    }

    public String toString() {
        return "Query { text = " + Utils.quoted(this.text) + ", metadata = " + this.metadata + " }";
    }

    public static Query from(String text) {
        return new Query(text);
    }

    public static Query from(String text, Metadata metadata) {
        return new Query(text, metadata);
    }
}

