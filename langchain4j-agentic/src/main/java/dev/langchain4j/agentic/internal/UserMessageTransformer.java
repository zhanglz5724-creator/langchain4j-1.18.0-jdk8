/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.model.chat.request.ChatRequest
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@FunctionalInterface
public interface UserMessageTransformer
extends BiFunction<ChatRequest, Object, ChatRequest> {
    @Override
    default public ChatRequest apply(ChatRequest chatRequest, Object memoryId) {
        List messages = chatRequest.messages();
        for (int i = messages.size() - 1; i >= 0; --i) {
            if (!(messages.get(i) instanceof UserMessage)) continue;
            UserMessage userMessage = (UserMessage)messages.get(i);
            UserMessage transformedMessage = UserMessage.from((String)this.transformUserMessage(userMessage.singleText(), memoryId));
            List modifiedMessages = chatRequest.messages().stream().map(message -> message == userMessage ? transformedMessage : message).collect(Collectors.toList());
            return ChatRequest.builder().messages(modifiedMessages).parameters(chatRequest.parameters()).build();
        }
        return chatRequest;
    }

    public String transformUserMessage(String var1, Object var2);
}

