/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.document;

import dev.langchain4j.data.document.Document;
import java.io.InputStream;

public interface DocumentParser {
    public Document parse(InputStream var1);
}

