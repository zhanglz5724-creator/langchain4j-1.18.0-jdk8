/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.document;

import dev.langchain4j.data.document.DefaultDocument;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;

public interface Document {
    public static final String FILE_NAME = "file_name";
    public static final String ABSOLUTE_DIRECTORY_PATH = "absolute_directory_path";
    public static final String URL = "url";

    public String text();

    public Metadata metadata();

    default public TextSegment toTextSegment() {
        if (this.metadata().containsKey("index")) {
            return TextSegment.from(this.text(), this.metadata().copy());
        }
        return TextSegment.from(this.text(), this.metadata().copy().put("index", "0"));
    }

    public static Document from(String text) {
        return new DefaultDocument(text);
    }

    public static Document from(String text, Metadata metadata) {
        return new DefaultDocument(text, metadata);
    }

    public static Document document(String text) {
        return Document.from(text);
    }

    public static Document document(String text, Metadata metadata) {
        return Document.from(text, metadata);
    }
}

