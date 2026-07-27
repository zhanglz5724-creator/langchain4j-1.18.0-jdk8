/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.Internal;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.guardrail.ChatExecutor;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.observability.api.AiServiceListenerRegistrar;
import dev.langchain4j.observability.api.event.AiServiceRequestIssuedEvent;
import java.util.List;

@Internal
abstract class AbstractChatExecutor
implements ChatExecutor {
    protected final ChatRequest chatRequest;
    protected final InvocationContext invocationContext;
    protected final AiServiceListenerRegistrar eventListenerRegistrar;

    protected AbstractChatExecutor(ChatExecutor.AbstractBuilder<?> builder) {
        this.chatRequest = ValidationUtils.ensureNotNull(builder.chatRequest, "chatRequest");
        this.invocationContext = ValidationUtils.ensureNotNull(builder.invocationContext, "invocationContext");
        this.eventListenerRegistrar = builder.eventListenerRegistrar;
    }

    @Override
    public ChatResponse execute(List<ChatMessage> chatMessages) {
        ChatRequest newChatRequest = this.chatRequest.toBuilder().messages(chatMessages).build();
        return this.executeInternal(newChatRequest);
    }

    @Override
    public ChatResponse execute() {
        return this.executeInternal(this.chatRequest);
    }

    protected void fireRequestIssuedEvent(ChatRequest chatRequest) {
        if (this.eventListenerRegistrar != null) {
            this.eventListenerRegistrar.fireEvent(AiServiceRequestIssuedEvent.builder().invocationContext(this.invocationContext).request(chatRequest).build());
        }
    }

    private ChatResponse executeInternal(ChatRequest chatRequest) {
        this.fireRequestIssuedEvent(chatRequest);
        return this.execute(chatRequest);
    }

    protected abstract ChatResponse execute(ChatRequest var1);
}

