/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.segment;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Objects;

public class TextSegment {
    private final String text;
    private final Metadata metadata;

    public TextSegment(String text, Metadata metadata) {
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
        TextSegment that = (TextSegment)o;
        return Objects.equals(this.text, that.text) && Objects.equals(this.metadata, that.metadata);
    }

    public int hashCode() {
        return Objects.hash(this.text, this.metadata);
    }

    public String toString() {
        return "TextSegment { text = " + Utils.quoted(this.text) + " metadata = " + this.metadata.toMap() + " }";
    }

    public static TextSegment from(String text) {
        return new TextSegment(text, new Metadata());
    }

    public static TextSegment from(String text, Metadata metadata) {
        return new TextSegment(text, metadata);
    }

    public static TextSegment textSegment(String text) {
        return TextSegment.from(text);
    }

    public static TextSegment textSegment(String text, Metadata metadata) {
        return TextSegment.from(text, metadata);
    }
}

