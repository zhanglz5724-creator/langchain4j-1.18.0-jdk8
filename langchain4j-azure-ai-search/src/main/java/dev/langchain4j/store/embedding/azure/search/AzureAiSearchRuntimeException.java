/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.exception.LangChain4jException
 */
package dev.langchain4j.store.embedding.azure.search;

import dev.langchain4j.exception.LangChain4jException;

public class AzureAiSearchRuntimeException
extends LangChain4jException {
    public AzureAiSearchRuntimeException(String message) {
        super(message);
    }

    public AzureAiSearchRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}

