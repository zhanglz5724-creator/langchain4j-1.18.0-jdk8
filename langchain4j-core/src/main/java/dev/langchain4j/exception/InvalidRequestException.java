/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.exception;

import dev.langchain4j.exception.NonRetriableException;

public class InvalidRequestException
extends NonRetriableException {
    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}

