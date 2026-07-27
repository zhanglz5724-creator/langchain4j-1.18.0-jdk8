/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.document;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Objects;

public class DefaultDocument
implements Document {
    private final String text;
    private final Metadata metadata;

    public DefaultDocument(String text, Metadata metadata) {
        this.text = ValidationUtils.ensureNotBlank(text, "text");
        this.metadata = ValidationUtils.ensureNotNull(metadata, "metadata");
    }

    public DefaultDocument(String text) {
        this(text, new Metadata());
    }

    @Override
    public String text() {
        return this.text;
    }

    @Override
    public Metadata metadata() {
        return this.metadata;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        DefaultDocument that = (DefaultDocument)obj;
        return Objects.equals(this.text, that.text) && Objects.equals(this.metadata, that.metadata);
    }

    public int hashCode() {
        return Objects.hash(this.text, this.metadata);
    }

    public String toString() {
        return "DefaultDocument { text = " + Utils.quoted(this.text) + ", metadata = " + this.metadata + " }";
    }
}

