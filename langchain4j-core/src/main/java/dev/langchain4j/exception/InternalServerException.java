/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.exception;

import dev.langchain4j.exception.RetriableException;

public class InternalServerException
extends RetriableException {
    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }
}

