/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.http.client.sse.ServerSentEvent
 *  dev.langchain4j.http.client.sse.ServerSentEventListener
 *  dev.langchain4j.internal.ExceptionMapper
 *  dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.StreamingResponseHandler
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.mistralai.internal.client;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.http.client.sse.ServerSentEvent;
import dev.langchain4j.http.client.sse.ServerSentEventListener;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionChoice;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionResponse;
import dev.langchain4j.model.mistralai.internal.api.MistralAiMessageContent;
import dev.langchain4j.model.mistralai.internal.api.MistralAiTextContent;
import dev.langchain4j.model.mistralai.internal.api.MistralAiToolCall;
import dev.langchain4j.model.mistralai.internal.api.MistralAiUsage;
import dev.langchain4j.model.mistralai.internal.client.MistralAiJsonUtils;
import dev.langchain4j.model.mistralai.internal.mapper.MistralAiMapper;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import java.util.List;
import java.util.function.BiFunction;

@Internal
class MistralAiFimServerSentEventListener
implements ServerSentEventListener {
    private final StringBuffer contentBuilder = new StringBuffer();
    private final StreamingResponseHandler<String> handler;
    private final BiFunction<String, List<ToolExecutionRequest>, String> toResponse;
    private List<ToolExecutionRequest> toolExecutionRequests;
    private TokenUsage tokenUsage;
    private FinishReason finishReason;

    public MistralAiFimServerSentEventListener(StreamingResponseHandler<String> handler, BiFunction<String, List<ToolExecutionRequest>, String> toResponse) {
        this.handler = handler;
        this.toResponse = toResponse;
    }

    public void onEvent(ServerSentEvent event) {
        String data = event.data();
        if ("[DONE]".equals(data)) {
            String responseContent = this.toResponse.apply(this.contentBuilder.toString(), this.toolExecutionRequests);
            Response response = Response.from((Object)responseContent, (TokenUsage)this.tokenUsage, (FinishReason)this.finishReason);
            try {
                this.handler.onComplete(response);
            }
            catch (Exception e) {
                InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> this.handler.onError((Throwable)e));
            }
        } else {
            String finishReasonString;
            MistralAiUsage usageInfo;
            List<MistralAiToolCall> toolCalls;
            MistralAiChatCompletionResponse chatCompletionResponse = MistralAiJsonUtils.fromJson(data, MistralAiChatCompletionResponse.class);
            MistralAiChatCompletionChoice choice = chatCompletionResponse.getChoices().get(0);
            List<MistralAiMessageContent> chunks = choice.getDelta().getContent();
            if (Utils.isNotNullOrEmpty(chunks)) {
                for (MistralAiMessageContent chunk : chunks) {
                    MistralAiTextContent textContent;
                    String text;
                    if (!(chunk instanceof MistralAiTextContent) || !Utils.isNotNullOrEmpty((String)(text = (textContent = (MistralAiTextContent)chunk).getText()))) continue;
                    this.contentBuilder.append(text);
                    try {
                        this.handler.onNext(text);
                    }
                    catch (Exception e) {
                        InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> this.handler.onError((Throwable)e));
                    }
                }
            }
            if (!Utils.isNullOrEmpty(toolCalls = choice.getDelta().getToolCalls())) {
                this.toolExecutionRequests = MistralAiMapper.toToolExecutionRequests(toolCalls);
            }
            if ((usageInfo = chatCompletionResponse.getUsage()) != null) {
                this.tokenUsage = MistralAiMapper.tokenUsageFrom(usageInfo);
            }
            if ((finishReasonString = choice.getFinishReason()) != null) {
                this.finishReason = MistralAiMapper.finishReasonFrom(finishReasonString);
            }
        }
    }

    public void onError(Throwable error) {
        RuntimeException mappedError = ExceptionMapper.DEFAULT.mapException(error);
        InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> this.handler.onError((Throwable)mappedError));
    }
}

