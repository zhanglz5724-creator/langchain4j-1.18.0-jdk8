/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.chat.listener.ChatModelErrorContext
 *  dev.langchain4j.model.chat.listener.ChatModelRequestContext
 *  dev.langchain4j.model.chat.listener.ChatModelResponseContext
 *  io.micrometer.observation.Observation$Context
 */
package dev.langchain4j.observation.context;

import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import io.micrometer.observation.Observation;

public class ChatModelObservationContext
extends Observation.Context {
    private final ChatModelRequestContext requestContext;
    private ChatModelResponseContext responseContext;
    private ChatModelErrorContext errorContext;

    public ChatModelObservationContext(ChatModelRequestContext requestContext, ChatModelResponseContext responseContext, ChatModelErrorContext errorContext) {
        this.requestContext = requestContext;
        this.responseContext = responseContext;
        this.errorContext = errorContext;
    }

    public ChatModelRequestContext getRequestContext() {
        return this.requestContext;
    }

    public ChatModelResponseContext getResponseContext() {
        return this.responseContext;
    }

    public void setResponseContext(ChatModelResponseContext responseContext) {
        this.responseContext = responseContext;
    }

    public ChatModelErrorContext getErrorContext() {
        return this.errorContext;
    }

    public void setErrorContext(ChatModelErrorContext errorContext) {
        this.errorContext = errorContext;
    }
}

