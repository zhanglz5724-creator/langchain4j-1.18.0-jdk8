/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.exception.LangChain4jException
 */
package dev.langchain4j.store.embedding.azure.cosmos.nosql;

import dev.langchain4j.exception.LangChain4jException;

public class AzureCosmosDBNoSqlRuntimeException
extends LangChain4jException {
    public AzureCosmosDBNoSqlRuntimeException(String message) {
        super(message);
    }

    public AzureCosmosDBNoSqlRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}

