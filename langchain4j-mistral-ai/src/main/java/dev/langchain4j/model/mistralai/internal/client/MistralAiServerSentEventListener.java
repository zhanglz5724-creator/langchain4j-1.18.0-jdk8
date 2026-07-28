/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.AiMessage$Builder
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 *  dev.langchain4j.http.client.sse.CancellationUnsupportedHandle
 *  dev.langchain4j.http.client.sse.ServerSentEvent
 *  dev.langchain4j.http.client.sse.ServerSentEventContext
 *  dev.langchain4j.http.client.sse.ServerSentEventListener
 *  dev.langchain4j.http.client.sse.ServerSentEventParsingHandle
 *  dev.langchain4j.http.client.sse.ServerSentEventParsingHandleUtils
 *  dev.langchain4j.internal.ExceptionMapper
 *  dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils
 *  dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.CompleteToolCall
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.chat.response.StreamingHandle
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.mistralai.internal.client;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.sse.CancellationUnsupportedHandle;
import dev.langchain4j.http.client.sse.ServerSentEvent;
import dev.langchain4j.http.client.sse.ServerSentEventContext;
import dev.langchain4j.http.client.sse.ServerSentEventListener;
import dev.langchain4j.http.client.sse.ServerSentEventParsingHandle;
import dev.langchain4j.http.client.sse.ServerSentEventParsingHandleUtils;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils;
import dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.chat.response.StreamingHandle;
import dev.langchain4j.model.mistralai.MistralAiChatResponseMetadata;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionChoice;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionResponse;
import dev.langchain4j.model.mistralai.internal.api.MistralAiMessageContent;
import dev.langchain4j.model.mistralai.internal.api.MistralAiTextContent;
import dev.langchain4j.model.mistralai.internal.api.MistralAiThinkingContent;
import dev.langchain4j.model.mistralai.internal.api.MistralAiToolCall;
import dev.langchain4j.model.mistralai.internal.api.MistralAiUsage;
import dev.langchain4j.model.mistralai.internal.client.MistralAiJsonUtils;
import dev.langchain4j.model.mistralai.internal.mapper.MistralAiMapper;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

@Internal
class MistralAiServerSentEventListener
implements ServerSentEventListener {
    private final StringBuffer textBuilder;
    private final StringBuffer thinkingBuilder;
    private final boolean returnThinking;
    private final MappingTrackingStreamingChatResponseHandler handler;
    private List<ToolExecutionRequest> toolExecutionRequests;
    private TokenUsage tokenUsage;
    private FinishReason finishReason;
    private String modelName;
    private String id;
    private volatile StreamingHandle streamingHandle;
    final AtomicReference<SuccessfulHttpResponse> rawHttpResponse = new AtomicReference();
    final Queue<ServerSentEvent> rawServerSentEvents = new ConcurrentLinkedQueue<ServerSentEvent>();

    public MistralAiServerSentEventListener(StreamingChatResponseHandler handler, boolean returnThinking) {
        this.textBuilder = new StringBuffer();
        this.thinkingBuilder = returnThinking ? new StringBuffer() : null;
        this.returnThinking = returnThinking;
        this.handler = new MappingTrackingStreamingChatResponseHandler(handler);
    }

    public void onOpen(SuccessfulHttpResponse response) {
        this.rawHttpResponse.set(response);
    }

    public void onEvent(ServerSentEvent event) {
        this.onEvent(event, new ServerSentEventContext((ServerSentEventParsingHandle)new CancellationUnsupportedHandle()));
    }

    public void onEvent(ServerSentEvent event, ServerSentEventContext context) {
        if (this.streamingHandle == null) {
            this.streamingHandle = ServerSentEventParsingHandleUtils.toStreamingHandle((ServerSentEventParsingHandle)context.parsingHandle());
        }
        this.rawServerSentEvents.add(event);
        this.handler.resetMappingTracking();
        String data = event.data();
        if ("[DONE]".equals(data)) {
            ChatResponse response = ChatResponse.builder().aiMessage(this.createAiMessage()).metadata((ChatResponseMetadata)this.createMetadata()).build();
            InternalStreamingChatResponseHandlerUtils.onCompleteResponse((StreamingChatResponseHandler)this.handler, (ChatResponse)response);
        } else {
            String finishReasonString;
            MistralAiUsage usageInfo;
            List<MistralAiToolCall> toolCalls;
            MistralAiChatCompletionResponse chatCompletionResponse = MistralAiJsonUtils.fromJson(data, MistralAiChatCompletionResponse.class);
            MistralAiChatCompletionChoice choice = chatCompletionResponse.getChoices().get(0);
            this.modelName = chatCompletionResponse.getModel();
            this.id = chatCompletionResponse.getId();
            List<MistralAiMessageContent> chunks = choice.getDelta().getContent();
            if (Utils.isNotNullOrEmpty(chunks)) {
                for (MistralAiMessageContent chunk : chunks) {
                    MistralAiTextContent textContent;
                    String text;
                    if (this.returnThinking && chunk instanceof MistralAiThinkingContent) {
                        MistralAiThinkingContent thinkingContent = (MistralAiThinkingContent)chunk;
                        List<String> thinkingChunks = MistralAiServerSentEventListener.getThinkingChunks(thinkingContent);
                        for (String thinkingChunk : thinkingChunks) {
                            this.thinkingBuilder.append(thinkingChunk);
                            InternalStreamingChatResponseHandlerUtils.onPartialThinking((StreamingChatResponseHandler)this.handler, (String)thinkingChunk, (StreamingHandle)this.streamingHandle);
                        }
                    }
                    if (!(chunk instanceof MistralAiTextContent) || !Utils.isNotNullOrEmpty((String)(text = (textContent = (MistralAiTextContent)chunk).getText()))) continue;
                    this.textBuilder.append(text);
                    InternalStreamingChatResponseHandlerUtils.onPartialResponse((StreamingChatResponseHandler)this.handler, (String)text, (StreamingHandle)this.streamingHandle);
                }
            }
            if (Utils.isNotNullOrEmpty(toolCalls = choice.getDelta().getToolCalls())) {
                this.toolExecutionRequests = MistralAiMapper.toToolExecutionRequests(toolCalls);
                for (int i = 0; i < this.toolExecutionRequests.size(); ++i) {
                    CompleteToolCall completeToolCall = new CompleteToolCall(i, this.toolExecutionRequests.get(i));
                    InternalStreamingChatResponseHandlerUtils.onCompleteToolCall((StreamingChatResponseHandler)this.handler, (CompleteToolCall)completeToolCall);
                }
            }
            if ((usageInfo = chatCompletionResponse.getUsage()) != null) {
                this.tokenUsage = MistralAiMapper.tokenUsageFrom(usageInfo);
            }
            if ((finishReasonString = choice.getFinishReason()) != null) {
                this.finishReason = MistralAiMapper.finishReasonFrom(finishReasonString);
            }
        }
        if (!this.handler.wasMapped()) {
            InternalStreamingChatResponseHandlerUtils.onUnmappedRawEvent((StreamingChatResponseHandler)this.handler, (Object)event);
        }
    }

    private static List<String> getThinkingChunks(MistralAiThinkingContent thinkingContent) {
        if (thinkingContent == null) {
            return Collections.emptyList();
        }
        if (Utils.isNullOrEmpty(thinkingContent.getThinking())) {
            return Collections.emptyList();
        }
        ArrayList<String> thinkingChunks = new ArrayList<String>(1);
        for (MistralAiTextContent thinkingTextContent : thinkingContent.getThinking()) {
            String thinkingText = thinkingTextContent.getText();
            if (!Utils.isNotNullOrEmpty((String)thinkingText)) continue;
            thinkingChunks.add(thinkingText);
        }
        return thinkingChunks;
    }

    private AiMessage createAiMessage() {
        AiMessage.Builder aiMessageBuilder = AiMessage.builder();
        if (!this.textBuilder.toString().isEmpty()) {
            aiMessageBuilder.text(this.textBuilder.toString());
        }
        if (this.returnThinking && this.thinkingBuilder != null && this.thinkingBuilder.length() > 0) {
            aiMessageBuilder.thinking(this.thinkingBuilder.toString());
        }
        if (Utils.isNotNullOrEmpty(this.toolExecutionRequests)) {
            aiMessageBuilder.toolExecutionRequests(this.toolExecutionRequests);
        }
        return aiMessageBuilder.build();
    }

    private MistralAiChatResponseMetadata createMetadata() {
        MistralAiChatResponseMetadata.Builder metadataBuilder = MistralAiChatResponseMetadata.builder();
        ((MistralAiChatResponseMetadata.Builder)((MistralAiChatResponseMetadata.Builder)((MistralAiChatResponseMetadata.Builder)metadataBuilder.tokenUsage(this.tokenUsage)).finishReason(this.finishReason)).modelName(this.modelName)).id(this.id);
        if (this.rawHttpResponse.get() != null) {
            metadataBuilder.rawHttpResponse(this.rawHttpResponse.get());
        }
        if (!this.rawServerSentEvents.isEmpty()) {
            metadataBuilder.rawServerSentEvents(new ArrayList<ServerSentEvent>(this.rawServerSentEvents));
        }
        return metadataBuilder.build();
    }

    public void onError(Throwable error) {
        RuntimeException mappedError = ExceptionMapper.DEFAULT.mapException(error);
        InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> this.handler.onError((Throwable)mappedError));
    }
}

