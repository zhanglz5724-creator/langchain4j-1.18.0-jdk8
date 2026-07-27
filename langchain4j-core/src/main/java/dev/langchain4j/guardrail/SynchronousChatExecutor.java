/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.Internal;
import dev.langchain4j.guardrail.AbstractChatExecutor;
import dev.langchain4j.guardrail.ChatExecutor;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;

@Internal
final class SynchronousChatExecutor
extends AbstractChatExecutor {
    private final ChatModel chatModel;

    protected SynchronousChatExecutor(ChatExecutor.SynchronousBuilder builder) {
        super(builder);
        this.chatModel = ValidationUtils.ensureNotNull(builder.chatModel, "chatModel");
    }

    @Override
    protected ChatResponse execute(ChatRequest chatRequest) {
        return this.chatModel.chat(chatRequest);
    }
}

