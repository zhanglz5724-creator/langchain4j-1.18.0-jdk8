/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.guardrail.StreamingToSynchronousChatExecutor;
import dev.langchain4j.guardrail.SynchronousChatExecutor;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.observability.api.AiServiceListenerRegistrar;
import java.util.List;
import java.util.function.Consumer;

public interface ChatExecutor {
    public ChatResponse execute();

    public ChatResponse execute(List<ChatMessage> var1);

    public static SynchronousBuilder builder(ChatModel chatModel) {
        return new SynchronousBuilder(chatModel);
    }

    public static StreamingToSynchronousBuilder builder(StreamingChatModel streamingChatModel) {
        return new StreamingToSynchronousBuilder(streamingChatModel);
    }

    public static class StreamingToSynchronousBuilder
    extends AbstractBuilder<StreamingToSynchronousBuilder> {
        protected final StreamingChatModel streamingChatModel;
        protected Consumer<Throwable> errorHandler;

        protected StreamingToSynchronousBuilder(StreamingChatModel streamingChatModel) {
            this.streamingChatModel = ValidationUtils.ensureNotNull(streamingChatModel, "streamingChatModel");
        }

        public StreamingToSynchronousBuilder errorHandler(Consumer<Throwable> errorHandler) {
            this.errorHandler = errorHandler;
            return this;
        }

        @Override
        public ChatExecutor build() {
            return new StreamingToSynchronousChatExecutor(this);
        }
    }

    public static class SynchronousBuilder
    extends AbstractBuilder<SynchronousBuilder> {
        protected final ChatModel chatModel;

        protected SynchronousBuilder(ChatModel chatModel) {
            this.chatModel = ValidationUtils.ensureNotNull(chatModel, "chatModel");
        }

        @Override
        public ChatExecutor build() {
            return new SynchronousChatExecutor(this);
        }
    }

    public static abstract class AbstractBuilder<T extends AbstractBuilder<T>> {
        protected ChatRequest chatRequest;
        protected InvocationContext invocationContext;
        protected AiServiceListenerRegistrar eventListenerRegistrar;

        protected AbstractBuilder() {
        }

        public AbstractBuilder<T> chatRequest(ChatRequest chatRequest) {
            this.chatRequest = chatRequest;
            return this;
        }

        public AbstractBuilder<T> invocationContext(InvocationContext invocationContext) {
            this.invocationContext = invocationContext;
            return this;
        }

        public AbstractBuilder<T> eventListenerRegistrar(AiServiceListenerRegistrar eventListenerRegistrar) {
            this.eventListenerRegistrar = eventListenerRegistrar;
            return this;
        }

        public abstract ChatExecutor build();
    }
}

