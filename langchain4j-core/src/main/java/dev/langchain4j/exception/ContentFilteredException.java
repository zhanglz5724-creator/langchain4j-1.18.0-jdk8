/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.exception;

import dev.langchain4j.exception.InvalidRequestException;

public class ContentFilteredException
extends InvalidRequestException {
    public ContentFilteredException(String message) {
        super(message);
    }

    public ContentFilteredException(Throwable cause) {
        super(cause);
    }

    public ContentFilteredException(String message, Throwable cause) {
        super(message, cause);
    }
}

