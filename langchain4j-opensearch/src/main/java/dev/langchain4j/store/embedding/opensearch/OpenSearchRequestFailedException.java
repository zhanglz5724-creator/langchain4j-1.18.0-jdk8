/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.exception.LangChain4jException
 */
package dev.langchain4j.store.embedding.opensearch;

import dev.langchain4j.exception.LangChain4jException;

public class OpenSearchRequestFailedException
extends LangChain4jException {
    public OpenSearchRequestFailedException(String message) {
        super(message);
    }

    public OpenSearchRequestFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}

