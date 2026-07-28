/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.response.ChatResponse
 */
package dev.langchain4j.agentic.agent;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;

public interface ChatMessagesAccess {
    public UserMessage lastUserMessage(Object var1);

    public ChatRequest lastChatRequest(Object var1);

    public ChatResponse lastChatResponse(Object var1);

    public void removeLastResponseEvent(Object var1);
}

