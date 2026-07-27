/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.spi.data.document.parser;

import dev.langchain4j.Internal;
import dev.langchain4j.data.document.DocumentParser;

@Internal
public interface DocumentParserFactory {
    public DocumentParser create();
}

