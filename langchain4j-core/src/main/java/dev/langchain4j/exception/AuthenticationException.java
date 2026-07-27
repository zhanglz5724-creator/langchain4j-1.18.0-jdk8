/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.exception;

import dev.langchain4j.exception.NonRetriableException;

public class AuthenticationException
extends NonRetriableException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}

