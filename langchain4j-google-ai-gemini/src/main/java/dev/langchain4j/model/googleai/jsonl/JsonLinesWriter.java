/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.googleai.jsonl;

import java.io.IOException;

public interface JsonLinesWriter
extends AutoCloseable {
    public void write(Object var1) throws IOException;

    public void write(Iterable<?> var1) throws IOException;

    public void flush() throws IOException;

    @Override
    public void close() throws IOException;
}

