/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.chat.StreamingChatModel
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 */
package dev.langchain4j.model.googleai;

import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.googleai.BaseGeminiChatModel;
import dev.langchain4j.model.googleai.GeminiGenerateContentRequest;
import dev.langchain4j.model.googleai.GeminiService;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatRequestParameters;
import java.util.List;

public class GoogleAiGeminiStreamingChatModel
extends BaseGeminiChatModel
implements StreamingChatModel {
    public GoogleAiGeminiStreamingChatModel(GoogleAiGeminiStreamingChatModelBuilder builder) {
        this(builder, GoogleAiGeminiStreamingChatModel.buildGeminiService(builder));
    }

    GoogleAiGeminiStreamingChatModel(GoogleAiGeminiStreamingChatModelBuilder builder, GeminiService geminiService) {
        super(builder, geminiService);
    }

    public static GoogleAiGeminiStreamingChatModelBuilder builder() {
        return new GoogleAiGeminiStreamingChatModelBuilder();
    }

    public GoogleAiGeminiChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    public void doChat(ChatRequest request, StreamingChatResponseHandler handler) {
        GeminiGenerateContentRequest geminiRequest = this.createGenerateContentRequest(request);
        this.geminiService.generateContentStream(request.modelName(), geminiRequest, this.includeCodeExecutionOutput, this.returnThinking, handler);
    }

    public List<ChatModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.GOOGLE_AI_GEMINI;
    }

    public static final class GoogleAiGeminiStreamingChatModelBuilder
    extends BaseGeminiChatModel.GoogleAiGeminiChatModelBaseBuilder<GoogleAiGeminiStreamingChatModelBuilder> {
        private GoogleAiGeminiStreamingChatModelBuilder() {
        }

        public GoogleAiGeminiStreamingChatModel build() {
            return new GoogleAiGeminiStreamingChatModel(this);
        }
    }
}

