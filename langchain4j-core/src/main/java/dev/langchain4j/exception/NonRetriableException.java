/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.exception;

import dev.langchain4j.exception.LangChain4jException;

public class NonRetriableException
extends LangChain4jException {
    public NonRetriableException(String message) {
        super(message);
    }

    public NonRetriableException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public NonRetriableException(String message, Throwable cause) {
        super(message, cause);
    }
}

