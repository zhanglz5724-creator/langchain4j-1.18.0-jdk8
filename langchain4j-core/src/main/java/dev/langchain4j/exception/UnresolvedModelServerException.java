/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.exception;

import dev.langchain4j.exception.NonRetriableException;

public class UnresolvedModelServerException
extends NonRetriableException {
    public UnresolvedModelServerException(String message) {
        super(message);
    }

    public UnresolvedModelServerException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public UnresolvedModelServerException(String message, Throwable cause) {
        super(message, cause);
    }
}

