/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.document;

import dev.langchain4j.data.document.Metadata;
import java.io.IOException;
import java.io.InputStream;

public interface DocumentSource {
    public InputStream inputStream() throws IOException;

    public Metadata metadata();
}

