/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat;

import dev.langchain4j.model.ModelDisabledException;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;

public class DisabledChatModel
implements ChatModel {
    @Override
    public ChatResponse doChat(ChatRequest chatRequest) {
        throw new ModelDisabledException("ChatModel is disabled");
    }
}

