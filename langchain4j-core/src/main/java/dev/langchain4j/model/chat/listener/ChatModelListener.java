/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.listener;

import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;

public interface ChatModelListener {
    default public void onRequest(ChatModelRequestContext requestContext) {
    }

    default public void onResponse(ChatModelResponseContext responseContext) {
    }

    default public void onError(ChatModelErrorContext errorContext) {
    }
}

