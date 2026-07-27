/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.content;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.ContentMetadata;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class DefaultContent
implements Content {
    private final TextSegment textSegment;
    private final Map<ContentMetadata, Object> metadata;

    public DefaultContent(TextSegment textSegment, Map<ContentMetadata, Object> metadata) {
        this.textSegment = ValidationUtils.ensureNotNull(textSegment, "textSegment");
        this.metadata = Utils.copy(metadata);
    }

    public DefaultContent(String text) {
        this(TextSegment.from(text));
    }

    public DefaultContent(TextSegment textSegment) {
        this(textSegment, Collections.emptyMap());
    }

    @Override
    public TextSegment textSegment() {
        return this.textSegment;
    }

    @Override
    public Map<ContentMetadata, Object> metadata() {
        return this.metadata;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Content that = (Content)o;
        return Objects.equals(this.textSegment, that.textSegment());
    }

    public int hashCode() {
        return Objects.hash(this.textSegment);
    }

    public String toString() {
        return "DefaultContent { textSegment = " + this.textSegment + ", metadata = " + this.metadata + " }";
    }
}

