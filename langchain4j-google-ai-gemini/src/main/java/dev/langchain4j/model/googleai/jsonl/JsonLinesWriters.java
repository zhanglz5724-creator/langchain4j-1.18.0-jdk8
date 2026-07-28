/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.googleai.jsonl;

import dev.langchain4j.model.googleai.jsonl.JsonLinesWriter;
import dev.langchain4j.model.googleai.jsonl.StreamingJsonLinesWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

public final class JsonLinesWriters {
    private JsonLinesWriters() {
    }

    public static JsonLinesWriter streaming(Path path) throws IOException {
        return new StreamingJsonLinesWriter(path);
    }

    public static JsonLinesWriter streaming(OutputStream outputStream) {
        return new StreamingJsonLinesWriter(outputStream);
    }
}

