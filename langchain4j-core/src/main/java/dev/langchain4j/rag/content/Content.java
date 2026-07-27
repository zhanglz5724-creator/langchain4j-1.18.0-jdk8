/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.content;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.rag.content.ContentMetadata;
import dev.langchain4j.rag.content.DefaultContent;
import java.util.Map;

public interface Content {
    public TextSegment textSegment();

    public Map<ContentMetadata, Object> metadata();

    public static Content from(String text) {
        return new DefaultContent(text);
    }

    public static Content from(TextSegment textSegment) {
        return new DefaultContent(textSegment);
    }

    public static Content from(TextSegment textSegment, Map<ContentMetadata, Object> metadata) {
        return new DefaultContent(textSegment, metadata);
    }
}

