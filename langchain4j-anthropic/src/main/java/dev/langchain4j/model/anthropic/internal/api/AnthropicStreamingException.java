/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.exception.LangChain4jException
 */
package dev.langchain4j.model.anthropic.internal.api;

import dev.langchain4j.exception.LangChain4jException;

public class AnthropicStreamingException
extends LangChain4jException {
    private final String type;

    public AnthropicStreamingException(String message, String type) {
        super(message);
        this.type = type;
    }

    public String type() {
        return this.type;
    }
}

