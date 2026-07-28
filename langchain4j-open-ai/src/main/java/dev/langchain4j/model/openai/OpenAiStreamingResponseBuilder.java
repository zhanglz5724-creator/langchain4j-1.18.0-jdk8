/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 *  dev.langchain4j.http.client.sse.ServerSentEvent
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.openai;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.sse.ServerSentEvent;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.openai.OpenAiChatResponseMetadata;
import dev.langchain4j.model.openai.internal.OpenAiUtils;
import dev.langchain4j.model.openai.internal.ParsedAndRawResponse;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionChoice;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionResponse;
import dev.langchain4j.model.openai.internal.chat.Delta;
import dev.langchain4j.model.openai.internal.chat.FunctionCall;
import dev.langchain4j.model.openai.internal.chat.LogProb;
import dev.langchain4j.model.openai.internal.chat.LogProbs;
import dev.langchain4j.model.openai.internal.chat.ToolCall;
import dev.langchain4j.model.openai.internal.completion.CompletionChoice;
import dev.langchain4j.model.openai.internal.completion.CompletionResponse;
import dev.langchain4j.model.openai.internal.shared.Usage;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Internal
public class OpenAiStreamingResponseBuilder {
    private final StringBuffer contentBuilder = new StringBuffer();
    private final StringBuffer reasoningContentBuilder;
    private final StringBuffer toolNameBuilder = new StringBuffer();
    private final StringBuffer toolArgumentsBuilder = new StringBuffer();
    private final Map<Integer, ToolExecutionRequestBuilder> indexToToolExecutionRequestBuilder = new ConcurrentHashMap<Integer, ToolExecutionRequestBuilder>();
    private final AtomicInteger fallbackToolCallIndex = new AtomicInteger(0);
    private final AtomicReference<String> id = new AtomicReference();
    private final AtomicReference<Long> created = new AtomicReference();
    private final AtomicReference<String> model = new AtomicReference();
    private final AtomicReference<String> serviceTier = new AtomicReference();
    private final AtomicReference<String> systemFingerprint = new AtomicReference();
    private final AtomicReference<TokenUsage> tokenUsage = new AtomicReference();
    private final AtomicReference<FinishReason> finishReason = new AtomicReference();
    private final AtomicReference<SuccessfulHttpResponse> rawHttpResponse = new AtomicReference();
    private final Queue<ServerSentEvent> rawServerSentEvents = new ConcurrentLinkedQueue<ServerSentEvent>();
    private final List<LogProb> logProbs = new CopyOnWriteArrayList<LogProb>();
    private final boolean returnThinking;
    private final boolean accumulateToolCallId;

    public OpenAiStreamingResponseBuilder() {
        this(false, true);
    }

    public OpenAiStreamingResponseBuilder(boolean returnThinking) {
        this(returnThinking, true);
    }

    public OpenAiStreamingResponseBuilder(boolean returnThinking, boolean accumulateToolCallId) {
        this.returnThinking = returnThinking;
        this.accumulateToolCallId = accumulateToolCallId;
        this.reasoningContentBuilder = returnThinking ? new StringBuffer() : null;
    }

    public void append(ParsedAndRawResponse<ChatCompletionResponse> parsedAndRawResponse) {
        if (parsedAndRawResponse != null) {
            if (parsedAndRawResponse.rawHttpResponse() != null) {
                this.rawHttpResponse.set(parsedAndRawResponse.rawHttpResponse());
            }
            if (parsedAndRawResponse.rawServerSentEvent() != null) {
                this.rawServerSentEvents.add(parsedAndRawResponse.rawServerSentEvent());
            }
            this.append(parsedAndRawResponse.parsedResponse());
        }
    }

    public void append(ChatCompletionResponse partialResponse) {
        Delta delta;
        LogProbs logProbs;
        List<ChatCompletionChoice> choices;
        Usage usage;
        if (partialResponse == null) {
            return;
        }
        if (!Utils.isNullOrBlank((String)partialResponse.id())) {
            this.id.set(partialResponse.id());
        }
        if (partialResponse.created() != null) {
            this.created.set(partialResponse.created());
        }
        if (!Utils.isNullOrBlank((String)partialResponse.model())) {
            this.model.set(partialResponse.model());
        }
        if (!Utils.isNullOrBlank((String)partialResponse.serviceTier())) {
            this.serviceTier.set(partialResponse.serviceTier());
        }
        if (!Utils.isNullOrBlank((String)partialResponse.systemFingerprint())) {
            this.systemFingerprint.set(partialResponse.systemFingerprint());
        }
        if ((usage = partialResponse.usage()) != null) {
            this.tokenUsage.set(OpenAiUtils.tokenUsageFrom(usage));
        }
        if (Utils.isNullOrEmpty(choices = partialResponse.choices())) {
            return;
        }
        ChatCompletionChoice chatCompletionChoice = choices.get(0);
        if (chatCompletionChoice == null) {
            return;
        }
        String finishReason = chatCompletionChoice.finishReason();
        if (finishReason != null) {
            this.finishReason.set(OpenAiUtils.finishReasonFrom(finishReason));
        }
        if ((logProbs = chatCompletionChoice.logprobs()) != null && logProbs.content() != null) {
            this.logProbs.addAll(logProbs.content());
        }
        if ((delta = chatCompletionChoice.delta()) == null) {
            return;
        }
        String content = delta.content();
        if (!Utils.isNullOrEmpty((String)content)) {
            this.contentBuilder.append(content);
        }
        String reasoningContent = delta.reasoningContent();
        if (this.returnThinking && !Utils.isNullOrEmpty((String)reasoningContent)) {
            this.reasoningContentBuilder.append(reasoningContent);
        }
        if (delta.functionCall() != null) {
            FunctionCall functionCall = delta.functionCall();
            if (functionCall.name() != null) {
                this.toolNameBuilder.append(functionCall.name());
            }
            if (functionCall.arguments() != null) {
                this.toolArgumentsBuilder.append(functionCall.arguments());
            }
        }
        if (delta.toolCalls() != null) {
            for (ToolCall toolCall : delta.toolCalls()) {
                FunctionCall functionCall;
                if (OpenAiStreamingResponseBuilder.isSentinel(toolCall)) continue;
                int toolCallIndex = toolCall.index() != null ? toolCall.index().intValue() : this.fallbackToolCallIndex.get();
                ToolExecutionRequestBuilder builder = this.indexToToolExecutionRequestBuilder.computeIfAbsent(toolCallIndex, idx -> new ToolExecutionRequestBuilder());
                if (toolCall.index() == null && toolCall.id() != null && builder.idBuilder.length() > 0 && !builder.idBuilder.toString().equals(toolCall.id())) {
                    toolCallIndex = this.fallbackToolCallIndex.incrementAndGet();
                    builder = this.indexToToolExecutionRequestBuilder.computeIfAbsent(toolCallIndex, idx -> new ToolExecutionRequestBuilder());
                }
                if (toolCall.id() != null) {
                    if (this.accumulateToolCallId) {
                        builder.idBuilder.append(toolCall.id());
                    } else {
                        builder.idBuilder.setLength(0);
                        builder.idBuilder.append(toolCall.id());
                    }
                }
                if ((functionCall = toolCall.function()) == null) continue;
                if (functionCall.name() != null) {
                    builder.nameBuilder.append(functionCall.name());
                }
                if (functionCall.arguments() == null) continue;
                builder.argumentsBuilder.append(functionCall.arguments());
            }
        }
    }

    public void append(CompletionResponse partialResponse) {
        String token;
        List<CompletionChoice> choices;
        if (partialResponse == null) {
            return;
        }
        Usage usage = partialResponse.usage();
        if (usage != null) {
            this.tokenUsage.set(OpenAiUtils.tokenUsageFrom(usage));
        }
        if (Utils.isNullOrEmpty(choices = partialResponse.choices())) {
            return;
        }
        CompletionChoice completionChoice = choices.get(0);
        if (completionChoice == null) {
            return;
        }
        String finishReason = completionChoice.finishReason();
        if (finishReason != null) {
            this.finishReason.set(OpenAiUtils.finishReasonFrom(finishReason));
        }
        if ((token = completionChoice.text()) != null) {
            this.contentBuilder.append(token);
        }
    }

    public ChatResponse build() {
        return ChatResponse.builder().aiMessage(this.buildAiMessage()).metadata((ChatResponseMetadata)this.buildMetadata()).build();
    }

    private AiMessage buildAiMessage() {
        String text = this.contentBuilder.toString();
        String thinking = null;
        if (this.returnThinking) {
            thinking = this.reasoningContentBuilder.toString();
        }
        return AiMessage.builder().text(Utils.isNullOrEmpty((String)text) ? null : text).thinking(Utils.isNullOrEmpty((String)thinking) ? null : thinking).toolExecutionRequests(this.buildToolExecutionRequests()).build();
    }

    private List<ToolExecutionRequest> buildToolExecutionRequests() {
        ArrayList<ToolExecutionRequest> toolExecutionRequests = new ArrayList<ToolExecutionRequest>();
        String toolName = this.toolNameBuilder.toString();
        if (!toolName.isEmpty()) {
            ToolExecutionRequest toolExecutionRequest = ToolExecutionRequest.builder().name(toolName).arguments(this.toolArgumentsBuilder.toString()).build();
            toolExecutionRequests.add(toolExecutionRequest);
        }
        if (!this.indexToToolExecutionRequestBuilder.isEmpty()) {
            List toolRequests = this.indexToToolExecutionRequestBuilder.values().stream().map(it -> ToolExecutionRequest.builder().id(((ToolExecutionRequestBuilder)it).idBuilder.toString()).name(((ToolExecutionRequestBuilder)it).nameBuilder.toString()).arguments(((ToolExecutionRequestBuilder)it).argumentsBuilder.toString()).build()).collect(Collectors.toList());
            toolExecutionRequests.addAll(toolRequests);
        }
        return toolExecutionRequests;
    }

    private OpenAiChatResponseMetadata buildMetadata() {
        return ((OpenAiChatResponseMetadata.Builder)((OpenAiChatResponseMetadata.Builder)((OpenAiChatResponseMetadata.Builder)((OpenAiChatResponseMetadata.Builder)OpenAiChatResponseMetadata.builder().id(this.id.get())).modelName(this.model.get())).tokenUsage(this.tokenUsage.get())).finishReason(this.finishReason.get())).created(this.created.get()).serviceTier(this.serviceTier.get()).systemFingerprint(this.systemFingerprint.get()).rawHttpResponse(this.rawHttpResponse.get()).rawServerSentEvents(new ArrayList<ServerSentEvent>(this.rawServerSentEvents)).logProbs(this.logProbs.isEmpty() ? null : OpenAiUtils.logProbsFrom(LogProbs.builder().content(new ArrayList<LogProb>(this.logProbs)).build())).build();
    }

    private static boolean isSentinel(ToolCall toolCall) {
        boolean hasId = !Utils.isNullOrBlank((String)toolCall.id());
        FunctionCall functionCall = toolCall.function();
        if (functionCall == null) {
            return !hasId;
        }
        boolean hasName = !Utils.isNullOrBlank((String)functionCall.name());
        boolean hasArguments = functionCall.arguments() != null;
        return !hasId && !hasName && !hasArguments;
    }

    private static class ToolExecutionRequestBuilder {
        private final StringBuffer idBuilder = new StringBuffer();
        private final StringBuffer nameBuilder = new StringBuffer();
        private final StringBuffer argumentsBuilder = new StringBuffer();

        private ToolExecutionRequestBuilder() {
        }
    }
}

