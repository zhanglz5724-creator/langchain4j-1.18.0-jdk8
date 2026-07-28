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
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ResponseFormatType
 *  dev.langchain4j.model.chat.response.ChatResponse
 */
package dev.langchain4j.model.googleai;

import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.BaseGeminiChatModel;
import dev.langchain4j.model.googleai.GeminiGenerateContentRequest;
import dev.langchain4j.model.googleai.GeminiGenerateContentResponse;
import dev.langchain4j.model.googleai.GeminiService;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatRequestParameters;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GoogleAiGeminiChatModel
extends BaseGeminiChatModel
implements ChatModel {
    private final Set<Capability> supportedCapabilities;
    private final Integer maximumRetries;

    public GoogleAiGeminiChatModel(GoogleAiGeminiChatModelBuilder builder) {
        this(builder, GoogleAiGeminiChatModel.buildGeminiService(builder));
    }

    GoogleAiGeminiChatModel(GoogleAiGeminiChatModelBuilder builder, GeminiService geminiService) {
        super(builder, geminiService);
        this.maximumRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        this.supportedCapabilities = Utils.copy((Set)builder.supportedCapabilities);
    }

    public static GoogleAiGeminiChatModelBuilder builder() {
        return new GoogleAiGeminiChatModelBuilder();
    }

    public GoogleAiGeminiChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    public ChatResponse doChat(ChatRequest chatRequest) {
        GeminiGenerateContentRequest request = this.createGenerateContentRequest(chatRequest);
        GeminiGenerateContentResponse geminiResponse = (GeminiGenerateContentResponse)RetryUtils.withRetryMappingExceptions(() -> this.geminiService.generateContent(chatRequest.modelName(), request), (int)this.maximumRetries);
        return this.processResponse(geminiResponse);
    }

    public Set<Capability> supportedCapabilities() {
        HashSet<Capability> capabilities = new HashSet<Capability>(this.supportedCapabilities);
        ResponseFormat responseFormat = this.defaultRequestParameters.responseFormat();
        if (responseFormat != null && ResponseFormatType.JSON.equals((Object)responseFormat.type())) {
            capabilities.add(Capability.RESPONSE_FORMAT_JSON_SCHEMA);
        }
        return capabilities;
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.GOOGLE_AI_GEMINI;
    }

    public static final class GoogleAiGeminiChatModelBuilder
    extends BaseGeminiChatModel.GoogleAiGeminiChatModelBaseBuilder<GoogleAiGeminiChatModelBuilder> {
        private Integer maxRetries;
        private Set<Capability> supportedCapabilities;

        private GoogleAiGeminiChatModelBuilder() {
        }

        public GoogleAiGeminiChatModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public GoogleAiGeminiChatModelBuilder supportedCapabilities(Set<Capability> supportedCapabilities) {
            this.supportedCapabilities = supportedCapabilities;
            return this;
        }

        public GoogleAiGeminiChatModelBuilder supportedCapabilities(Capability ... supportedCapabilities) {
            return this.supportedCapabilities(new HashSet<Capability>(Arrays.asList(supportedCapabilities)));
        }

        public GoogleAiGeminiChatModel build() {
            return new GoogleAiGeminiChatModel(this);
        }

        GoogleAiGeminiChatModel build(GeminiService geminiService) {
            return new GoogleAiGeminiChatModel(this, geminiService);
        }
    }
}

