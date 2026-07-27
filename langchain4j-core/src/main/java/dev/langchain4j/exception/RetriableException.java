/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.exception;

import dev.langchain4j.exception.LangChain4jException;

public class RetriableException
extends LangChain4jException {
    public RetriableException(String message) {
        super(message);
    }

    public RetriableException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public RetriableException(String message, Throwable cause) {
        super(message, cause);
    }
}

