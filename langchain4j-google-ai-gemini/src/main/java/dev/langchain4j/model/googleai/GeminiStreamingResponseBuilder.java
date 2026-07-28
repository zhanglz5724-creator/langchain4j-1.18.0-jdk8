/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.googleai;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.googleai.FinishReasonMapper;
import dev.langchain4j.model.googleai.GeminiContent;
import dev.langchain4j.model.googleai.GeminiGenerateContentResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatResponseMetadata;
import dev.langchain4j.model.googleai.GoogleAiGeminiTokenUsage;
import dev.langchain4j.model.googleai.PartsAndContentsMapper;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

class GeminiStreamingResponseBuilder {
    private final boolean includeCodeExecutionOutput;
    private final Boolean returnThinking;
    private final StringBuilder contentBuilder;
    private final StringBuilder thoughtBuilder;
    private final Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();
    private final List<ToolExecutionRequest> functionCalls;
    private final AtomicReference<String> id = new AtomicReference();
    private final AtomicReference<String> modelName = new AtomicReference();
    private final AtomicReference<TokenUsage> tokenUsage = new AtomicReference();
    private final AtomicReference<FinishReason> finishReason = new AtomicReference();

    GeminiStreamingResponseBuilder(boolean includeCodeExecutionOutput, Boolean returnThinking) {
        this.includeCodeExecutionOutput = includeCodeExecutionOutput;
        this.returnThinking = returnThinking;
        this.contentBuilder = new StringBuilder();
        this.thoughtBuilder = new StringBuilder();
        this.functionCalls = new ArrayList<ToolExecutionRequest>();
    }

    TextAndTools append(GeminiGenerateContentResponse partialResponse) {
        if (partialResponse == null) {
            return new TextAndTools(Optional.empty(), Optional.empty(), Collections.emptyList());
        }
        List<GeminiGenerateContentResponse.GeminiCandidate> candidates = partialResponse.candidates();
        if (candidates == null || candidates.isEmpty()) {
            return new TextAndTools(Optional.empty(), Optional.empty(), Collections.emptyList());
        }
        GeminiGenerateContentResponse.GeminiCandidate firstCandidate = candidates.get(0);
        this.updateId(partialResponse);
        this.updateModelName(partialResponse);
        this.updateFinishReason(firstCandidate);
        this.updateTokenUsage(partialResponse.usageMetadata());
        GeminiContent content = firstCandidate.content();
        if (content == null || content.parts() == null) {
            return new TextAndTools(Optional.empty(), Optional.empty(), Collections.emptyList());
        }
        AiMessage message = PartsAndContentsMapper.fromGPartsToAiMessage(content.parts(), this.includeCodeExecutionOutput, this.returnThinking);
        this.updateContentAndFunctionCalls(message);
        return new TextAndTools(Optional.ofNullable(message.text()), Optional.ofNullable(message.thinking()), message.toolExecutionRequests());
    }

    ChatResponse build() {
        AiMessage aiMessage = this.createAiMessage();
        return ChatResponse.builder().aiMessage(aiMessage).metadata((ChatResponseMetadata)((GoogleAiGeminiChatResponseMetadata.Builder)((GoogleAiGeminiChatResponseMetadata.Builder)((GoogleAiGeminiChatResponseMetadata.Builder)((GoogleAiGeminiChatResponseMetadata.Builder)GoogleAiGeminiChatResponseMetadata.builder().id(this.id.get())).modelName(this.modelName.get())).tokenUsage(this.tokenUsage.get())).finishReason(aiMessage.hasToolExecutionRequests() ? FinishReason.TOOL_EXECUTION : this.finishReason.get())).build()).build();
    }

    private void updateId(GeminiGenerateContentResponse response) {
        if (!Utils.isNullOrBlank((String)response.responseId())) {
            this.id.set(response.responseId());
        }
    }

    private void updateModelName(GeminiGenerateContentResponse response) {
        if (!Utils.isNullOrBlank((String)response.modelVersion())) {
            this.modelName.set(response.modelVersion());
        }
    }

    private void updateTokenUsage(GeminiGenerateContentResponse.GeminiUsageMetadata usageMetadata) {
        if (usageMetadata != null) {
            GoogleAiGeminiTokenUsage tokenUsage = GoogleAiGeminiTokenUsage.builder().inputTokenCount(usageMetadata.promptTokenCount()).outputTokenCount(usageMetadata.candidatesTokenCount()).totalTokenCount(usageMetadata.totalTokenCount()).cachedContentTokenCount(usageMetadata.cachedContentTokenCount()).thoughtsTokenCount(usageMetadata.thoughtsTokenCount()).build();
            this.tokenUsage.set(tokenUsage);
        }
    }

    private void updateFinishReason(GeminiGenerateContentResponse.GeminiCandidate candidate) {
        if (candidate.finishReason() != null) {
            this.finishReason.set(FinishReasonMapper.fromGFinishReasonToFinishReason(candidate.finishReason()));
        }
    }

    private void updateContentAndFunctionCalls(AiMessage message) {
        Optional.ofNullable(message.text()).ifPresent(this.contentBuilder::append);
        Optional.ofNullable(message.thinking()).ifPresent(this.thoughtBuilder::append);
        this.attributes.putAll(message.attributes());
        if (message.hasToolExecutionRequests()) {
            this.functionCalls.addAll(message.toolExecutionRequests());
        }
    }

    private AiMessage createAiMessage() {
        String text = this.contentBuilder.toString();
        String thought = this.thoughtBuilder.toString();
        return AiMessage.builder().text(text.isEmpty() ? null : text).thinking(thought.isEmpty() ? null : thought).toolExecutionRequests(this.functionCalls).attributes(this.attributes).build();
    }

    static final class TextAndTools {
        private final Optional<String> maybeText;
        private final Optional<String> maybeThought;
        private final List<ToolExecutionRequest> tools;

        TextAndTools(Optional<String> maybeText, Optional<String> maybeThought, List<ToolExecutionRequest> tools) {
            this.maybeText = maybeText;
            this.maybeThought = maybeThought;
            this.tools = tools;
        }

        Optional<String> maybeText() {
            return this.maybeText;
        }

        Optional<String> maybeThought() {
            return this.maybeThought;
        }

        List<ToolExecutionRequest> tools() {
            return this.tools;
        }
    }
}

