/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.chat.Capability
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.ollama;

import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.InternalOllamaHelper;
import dev.langchain4j.model.ollama.OllamaBaseChatModel;
import dev.langchain4j.model.ollama.OllamaChatRequest;
import dev.langchain4j.model.ollama.OllamaChatRequestParameters;
import dev.langchain4j.model.ollama.OllamaChatResponse;
import dev.langchain4j.model.ollama.spi.OllamaChatModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class OllamaChatModel
extends OllamaBaseChatModel
implements ChatModel {
    private final int maxRetries;

    public OllamaChatModel(OllamaChatModelBuilder builder) {
        this.init(builder);
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
    }

    public ChatResponse doChat(ChatRequest chatRequest) {
        this.validate(chatRequest.parameters());
        OllamaChatRequest ollamaChatRequest = InternalOllamaHelper.toOllamaChatRequest(chatRequest, false);
        OllamaChatResponse ollamaChatResponse = (OllamaChatResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.chat(ollamaChatRequest), (int)this.maxRetries);
        return ChatResponse.builder().aiMessage(InternalOllamaHelper.aiMessageFrom(ollamaChatResponse.getMessage(), this.returnThinking)).metadata(InternalOllamaHelper.chatResponseMetadataFrom(ollamaChatResponse)).build();
    }

    public OllamaChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.OLLAMA;
    }

    public Set<Capability> supportedCapabilities() {
        return this.supportedCapabilities;
    }

    public static OllamaChatModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(OllamaChatModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            OllamaChatModelBuilderFactory factory = (OllamaChatModelBuilderFactory)iterator.next();
            return (OllamaChatModelBuilder)factory.get();
        }
        return new OllamaChatModelBuilder();
    }

    public static class OllamaChatModelBuilder
    extends OllamaBaseChatModel.Builder<OllamaChatModel, OllamaChatModelBuilder> {
        private Integer maxRetries;

        @Override
        protected OllamaChatModelBuilder self() {
            return this;
        }

        public OllamaChatModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        @Override
        public OllamaChatModel build() {
            return new OllamaChatModel(this);
        }
    }
}

