/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.chat.Capability
 *  dev.langchain4j.model.chat.StreamingChatModel
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.ollama;

import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.ollama.OllamaBaseChatModel;
import dev.langchain4j.model.ollama.OllamaChatRequestParameters;
import dev.langchain4j.model.ollama.spi.OllamaStreamingChatModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class OllamaStreamingChatModel
extends OllamaBaseChatModel
implements StreamingChatModel {
    public OllamaStreamingChatModel(OllamaStreamingChatModelBuilder builder) {
        this.init(builder);
    }

    public void doChat(ChatRequest chatRequest, StreamingChatResponseHandler handler) {
        this.validate(chatRequest.parameters());
        this.client.streamingChat(chatRequest, this.returnThinking, handler);
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

    public static OllamaStreamingChatModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(OllamaStreamingChatModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            OllamaStreamingChatModelBuilderFactory factory = (OllamaStreamingChatModelBuilderFactory)iterator.next();
            return (OllamaStreamingChatModelBuilder)factory.get();
        }
        return new OllamaStreamingChatModelBuilder();
    }

    public static class OllamaStreamingChatModelBuilder
    extends OllamaBaseChatModel.Builder<OllamaStreamingChatModel, OllamaStreamingChatModelBuilder> {
        @Override
        protected OllamaStreamingChatModelBuilder self() {
            return this;
        }

        @Override
        public OllamaStreamingChatModel build() {
            return new OllamaStreamingChatModel(this);
        }
    }
}

