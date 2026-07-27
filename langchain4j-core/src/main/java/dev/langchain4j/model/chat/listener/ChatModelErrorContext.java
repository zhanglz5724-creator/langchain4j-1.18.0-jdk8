/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.listener;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.request.ChatRequest;
import java.util.Map;

public class ChatModelErrorContext {
    private final Throwable error;
    private final ChatRequest chatRequest;
    private final ModelProvider modelProvider;
    private final Map<Object, Object> attributes;

    public ChatModelErrorContext(Throwable error, ChatRequest chatRequest, ModelProvider modelProvider, Map<Object, Object> attributes) {
        this.error = ValidationUtils.ensureNotNull(error, "error");
        this.chatRequest = ValidationUtils.ensureNotNull(chatRequest, "chatRequest");
        this.modelProvider = modelProvider;
        this.attributes = ValidationUtils.ensureNotNull(attributes, "attributes");
    }

    public Throwable error() {
        return this.error;
    }

    public ChatRequest chatRequest() {
        return this.chatRequest;
    }

    public ModelProvider modelProvider() {
        return this.modelProvider;
    }

    public Map<Object, Object> attributes() {
        return this.attributes;
    }
}

