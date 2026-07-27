/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.listener;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import java.util.Map;

public class ChatModelResponseContext {
    private final ChatResponse chatResponse;
    private final ChatRequest chatRequest;
    private final ModelProvider modelProvider;
    private final Map<Object, Object> attributes;

    public ChatModelResponseContext(ChatResponse chatResponse, ChatRequest chatRequest, ModelProvider modelProvider, Map<Object, Object> attributes) {
        this.chatResponse = ValidationUtils.ensureNotNull(chatResponse, "chatResponse");
        this.chatRequest = ValidationUtils.ensureNotNull(chatRequest, "chatRequest");
        this.modelProvider = modelProvider;
        this.attributes = ValidationUtils.ensureNotNull(attributes, "attributes");
    }

    public ChatResponse chatResponse() {
        return this.chatResponse;
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

