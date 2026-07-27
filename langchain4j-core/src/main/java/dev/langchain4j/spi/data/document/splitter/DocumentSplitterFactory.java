/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.spi.data.document.splitter;

import dev.langchain4j.Internal;
import dev.langchain4j.data.document.DocumentSplitter;

@Internal
public interface DocumentSplitterFactory {
    public DocumentSplitter create();
}

