/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.http.client.HttpClient
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.http.client.HttpClientBuilderLoader
 *  dev.langchain4j.http.client.HttpMethod
 *  dev.langchain4j.http.client.HttpRequest
 *  dev.langchain4j.http.client.HttpRequest$Builder
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 *  dev.langchain4j.http.client.log.LoggingHttpClient
 *  dev.langchain4j.http.client.sse.CancellationUnsupportedHandle
 *  dev.langchain4j.http.client.sse.ServerSentEvent
 *  dev.langchain4j.http.client.sse.ServerSentEventContext
 *  dev.langchain4j.http.client.sse.ServerSentEventListener
 *  dev.langchain4j.http.client.sse.ServerSentEventParsingHandle
 *  dev.langchain4j.http.client.sse.ServerSentEventParsingHandleUtils
 *  dev.langchain4j.internal.ExceptionMapper
 *  dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils
 *  dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler
 *  dev.langchain4j.internal.ToolCallBuilder
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.CompleteToolCall
 *  dev.langchain4j.model.chat.response.PartialToolCall
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.chat.response.StreamingHandle
 *  dev.langchain4j.model.output.FinishReason
 */
package dev.langchain4j.model.anthropic.internal.client;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.http.client.HttpClientBuilderLoader;
import dev.langchain4j.http.client.HttpMethod;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.log.LoggingHttpClient;
import dev.langchain4j.http.client.sse.CancellationUnsupportedHandle;
import dev.langchain4j.http.client.sse.ServerSentEvent;
import dev.langchain4j.http.client.sse.ServerSentEventContext;
import dev.langchain4j.http.client.sse.ServerSentEventListener;
import dev.langchain4j.http.client.sse.ServerSentEventParsingHandle;
import dev.langchain4j.http.client.sse.ServerSentEventParsingHandleUtils;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils;
import dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler;
import dev.langchain4j.internal.ToolCallBuilder;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.anthropic.AnthropicChatResponseMetadata;
import dev.langchain4j.model.anthropic.AnthropicServerToolResult;
import dev.langchain4j.model.anthropic.AnthropicTokenUsage;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCountTokensRequest;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCreateMessageRequest;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCreateMessageResponse;
import dev.langchain4j.model.anthropic.internal.api.AnthropicDelta;
import dev.langchain4j.model.anthropic.internal.api.AnthropicDiagnostics;
import dev.langchain4j.model.anthropic.internal.api.AnthropicModelsListResponse;
import dev.langchain4j.model.anthropic.internal.api.AnthropicResponseMessage;
import dev.langchain4j.model.anthropic.internal.api.AnthropicStreamingData;
import dev.langchain4j.model.anthropic.internal.api.AnthropicStreamingException;
import dev.langchain4j.model.anthropic.internal.api.AnthropicUsage;
import dev.langchain4j.model.anthropic.internal.api.MessageTokenCountResponse;
import dev.langchain4j.model.anthropic.internal.client.AnthropicClient;
import dev.langchain4j.model.anthropic.internal.client.AnthropicCreateMessageOptions;
import dev.langchain4j.model.anthropic.internal.client.Json;
import dev.langchain4j.model.anthropic.internal.client.ParsedAndRawResponse;
import dev.langchain4j.model.anthropic.internal.mapper.AnthropicMapper;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.PartialToolCall;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.chat.response.StreamingHandle;
import dev.langchain4j.model.output.FinishReason;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Internal
public class DefaultAnthropicClient
extends AnthropicClient {
    private static final String CONTENT_BLOCK_TEXT = "text";
    private static final String CONTENT_BLOCK_THINKING = "thinking";
    private static final String CONTENT_BLOCK_REDACTED_THINKING = "redacted_thinking";
    private static final String CONTENT_BLOCK_TOOL_USE = "tool_use";
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String apiKey;
    private final String version;
    private final String beta;
    private final Supplier<Map<String, String>> customHeadersSupplier;

    public static Builder builder() {
        return new Builder();
    }

    DefaultAnthropicClient(Builder builder) {
        HttpClientBuilder httpClientBuilder = (HttpClientBuilder)Utils.getOrDefault((Object)builder.httpClientBuilder, HttpClientBuilderLoader::loadHttpClientBuilder);
        HttpClient httpClient = httpClientBuilder.connectTimeout((Duration)Utils.getOrDefault((Object)Utils.getOrDefault((Object)builder.timeout, (Object)httpClientBuilder.connectTimeout()), (Object)Duration.ofSeconds(15L))).readTimeout((Duration)Utils.getOrDefault((Object)Utils.getOrDefault((Object)builder.timeout, (Object)httpClientBuilder.readTimeout()), (Object)Duration.ofSeconds(60L))).build();
        this.httpClient = builder.logRequests != null && builder.logRequests != false || builder.logResponses != null && builder.logResponses != false ? new LoggingHttpClient(httpClient, builder.logRequests, builder.logResponses, builder.logger) : httpClient;
        this.baseUrl = ValidationUtils.ensureNotBlank((String)builder.baseUrl, (String)"baseUrl");
        this.apiKey = ValidationUtils.ensureNotBlank((String)builder.apiKey, (String)"apiKey");
        this.version = ValidationUtils.ensureNotBlank((String)builder.version, (String)"version");
        this.beta = builder.beta;
        this.customHeadersSupplier = builder.customHeadersSupplier != null ? builder.customHeadersSupplier : Collections::emptyMap;
    }

    @Override
    public AnthropicCreateMessageResponse createMessage(AnthropicCreateMessageRequest request) {
        return this.createMessageWithRawResponse(request).parsedResponse();
    }

    @Override
    public ParsedAndRawResponse createMessageWithRawResponse(AnthropicCreateMessageRequest request) {
        HttpRequest httpRequest = this.toHttpRequest(Json.toJson(request), "messages");
        SuccessfulHttpResponse rawResponse = this.httpClient.execute(httpRequest);
        AnthropicCreateMessageResponse parsedResponse = Json.fromJson(rawResponse.body(), AnthropicCreateMessageResponse.class);
        return new ParsedAndRawResponse(parsedResponse, rawResponse);
    }

    @Override
    public void createMessage(AnthropicCreateMessageRequest request, final AnthropicCreateMessageOptions options, StreamingChatResponseHandler handler) {
        final StreamingChatResponseHandler targetHandler = handler;
        ServerSentEventListener eventListener = new ServerSentEventListener(){
            final MappingTrackingStreamingChatResponseHandler handler;
            final List<String> contents;
            final StringBuffer contentBuilder;
            final List<String> thinkings;
            final StringBuffer thinkingBuilder;
            final List<String> thinkingSignatures;
            final List<String> redactedThinkings;
            final ConcurrentHashMap<Integer, String> contentBlockTypes;
            final ConcurrentHashMap<Integer, ToolCallBuilder> toolCallBuilders;
            final AtomicInteger toolCallIndex;
            final Queue<ToolExecutionRequest> completedToolExecutionRequests;
            final List<AnthropicServerToolResult> serverToolResults;
            final AtomicInteger inputTokenCount;
            final AtomicInteger outputTokenCount;
            final AtomicInteger cacheCreationInputTokens;
            final AtomicInteger cacheReadInputTokens;
            final AtomicReference<String> responseId;
            final AtomicReference<String> responseModel;
            final AtomicReference<AnthropicDiagnostics> responseDiagnostics;
            volatile String stopReason;
            volatile StreamingHandle streamingHandle;
            final AtomicReference<SuccessfulHttpResponse> rawHttpResponse;
            final Queue<ServerSentEvent> rawServerSentEvents;
            {
                this.handler = new MappingTrackingStreamingChatResponseHandler(targetHandler);
                this.contents = Collections.synchronizedList(new ArrayList());
                this.contentBuilder = new StringBuffer();
                this.thinkings = Collections.synchronizedList(new ArrayList());
                this.thinkingBuilder = new StringBuffer();
                this.thinkingSignatures = Collections.synchronizedList(new ArrayList());
                this.redactedThinkings = Collections.synchronizedList(new ArrayList());
                this.contentBlockTypes = new ConcurrentHashMap();
                this.toolCallBuilders = new ConcurrentHashMap();
                this.toolCallIndex = new AtomicInteger(-1);
                this.completedToolExecutionRequests = new ConcurrentLinkedQueue<ToolExecutionRequest>();
                this.serverToolResults = Collections.synchronizedList(new ArrayList());
                this.inputTokenCount = new AtomicInteger();
                this.outputTokenCount = new AtomicInteger();
                this.cacheCreationInputTokens = new AtomicInteger();
                this.cacheReadInputTokens = new AtomicInteger();
                this.responseId = new AtomicReference();
                this.responseModel = new AtomicReference();
                this.responseDiagnostics = new AtomicReference();
                this.rawHttpResponse = new AtomicReference();
                this.rawServerSentEvents = new ConcurrentLinkedQueue<ServerSentEvent>();
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
                this.handler.resetMappingTracking();
                String eventName = event.event();
                String eventData = event.data();
                if (this.isSkippableSseFrame(eventName, eventData)) {
                    this.rawServerSentEvents.add(event);
                    return;
                }
                AnthropicStreamingData data = Json.fromJson(eventData, AnthropicStreamingData.class);
                if ("message_start".equals(eventName)) {
                    this.handleMessageStart(data);
                } else if ("content_block_start".equals(eventName)) {
                    this.handleContentBlockStart(data, this.streamingHandle);
                } else if ("content_block_delta".equals(eventName)) {
                    this.handleContentBlockDelta(data, this.streamingHandle);
                } else if ("content_block_stop".equals(eventName)) {
                    this.handleContentBlockStop(data, this.streamingHandle);
                } else if ("message_delta".equals(eventName)) {
                    this.handleMessageDelta(data);
                } else if ("message_stop".equals(eventName)) {
                    this.handleMessageStop();
                } else if ("error".equals(eventName)) {
                    this.handleError(data);
                }
                this.rawServerSentEvents.add(event);
                if (!this.handler.wasMapped()) {
                    InternalStreamingChatResponseHandlerUtils.onUnmappedRawEvent((StreamingChatResponseHandler)this.handler, (Object)event);
                }
            }

            private boolean isSkippableSseFrame(String eventName, String eventData) {
                if (eventName == null) {
                    return true;
                }
                if (eventData == null) {
                    return true;
                }
                String trimmed = eventData.trim();
                if (trimmed.isEmpty() || "[DONE]".equals(trimmed)) {
                    return true;
                }
                return !trimmed.startsWith("{");
            }

            private void handleMessageStart(AnthropicStreamingData data) {
                AnthropicResponseMessage message = data.message;
                if (message != null) {
                    if (message.usage != null) {
                        this.handleUsage(message.usage);
                    }
                    if (message.id != null) {
                        this.responseId.set(message.id);
                    }
                    if (message.model != null) {
                        this.responseModel.set(message.model);
                    }
                    if (message.diagnostics != null) {
                        this.responseDiagnostics.set(message.diagnostics);
                    }
                }
            }

            private void handleUsage(AnthropicUsage usage) {
                if (usage.inputTokens != null) {
                    this.inputTokenCount.set(usage.inputTokens);
                }
                if (usage.outputTokens != null) {
                    this.outputTokenCount.set(usage.outputTokens);
                }
                if (usage.cacheCreationInputTokens != null) {
                    this.cacheCreationInputTokens.set(usage.cacheCreationInputTokens);
                }
                if (usage.cacheReadInputTokens != null) {
                    this.cacheReadInputTokens.set(usage.cacheReadInputTokens);
                }
            }

            private void handleContentBlockStart(AnthropicStreamingData data, StreamingHandle streamingHandle) {
                if (data.contentBlock == null) {
                    return;
                }
                String blockType = data.contentBlock.type;
                this.contentBlockTypes.put(data.index, blockType);
                if (DefaultAnthropicClient.CONTENT_BLOCK_TEXT.equals(blockType)) {
                    String text = data.contentBlock.text;
                    if (Utils.isNotNullOrEmpty((String)text)) {
                        this.contentBuilder.append(text);
                        InternalStreamingChatResponseHandlerUtils.onPartialResponse((StreamingChatResponseHandler)this.handler, (String)text, (StreamingHandle)streamingHandle);
                    }
                } else if (DefaultAnthropicClient.CONTENT_BLOCK_THINKING.equals(blockType) && options.returnThinking()) {
                    String signature;
                    String thinking = data.contentBlock.thinking;
                    if (Utils.isNotNullOrEmpty((String)thinking)) {
                        this.thinkingBuilder.append(thinking);
                        InternalStreamingChatResponseHandlerUtils.onPartialThinking((StreamingChatResponseHandler)this.handler, (String)thinking, (StreamingHandle)streamingHandle);
                    }
                    if (Utils.isNotNullOrEmpty((String)(signature = data.contentBlock.signature))) {
                        this.thinkingSignatures.add(signature);
                    }
                } else if (DefaultAnthropicClient.CONTENT_BLOCK_REDACTED_THINKING.equals(blockType) && options.returnThinking()) {
                    String redactedThinking = data.contentBlock.data;
                    if (Utils.isNotNullOrEmpty((String)redactedThinking)) {
                        this.redactedThinkings.add(redactedThinking);
                    }
                } else if (DefaultAnthropicClient.CONTENT_BLOCK_TOOL_USE.equals(blockType)) {
                    ToolCallBuilder toolCallBuilder = new ToolCallBuilder(this.toolCallIndex.incrementAndGet());
                    toolCallBuilder.updateId(data.contentBlock.id);
                    toolCallBuilder.updateName(data.contentBlock.name);
                    this.toolCallBuilders.put(data.index, toolCallBuilder);
                } else if (this.isServerToolResultType(blockType) && options.returnServerToolResults()) {
                    AnthropicServerToolResult result = AnthropicServerToolResult.builder().type(data.contentBlock.type).toolUseId(data.contentBlock.toolUseId).content(data.contentBlock.content).build();
                    this.serverToolResults.add(result);
                }
            }

            private boolean isServerToolResultType(String type) {
                return type != null && type.endsWith("_tool_result");
            }

            private void handleContentBlockDelta(AnthropicStreamingData data, StreamingHandle streamingHandle) {
                String partialJson;
                if (data.delta == null) {
                    return;
                }
                String blockType = this.contentBlockTypes.get(data.index);
                if (DefaultAnthropicClient.CONTENT_BLOCK_TEXT.equals(blockType)) {
                    String text = data.delta.text;
                    if (Utils.isNotNullOrEmpty((String)text)) {
                        this.contentBuilder.append(text);
                        InternalStreamingChatResponseHandlerUtils.onPartialResponse((StreamingChatResponseHandler)this.handler, (String)text, (StreamingHandle)streamingHandle);
                    }
                } else if (DefaultAnthropicClient.CONTENT_BLOCK_THINKING.equals(blockType) && options.returnThinking()) {
                    String signature;
                    String thinking = data.delta.thinking;
                    if (Utils.isNotNullOrEmpty((String)thinking)) {
                        this.thinkingBuilder.append(thinking);
                        InternalStreamingChatResponseHandlerUtils.onPartialThinking((StreamingChatResponseHandler)this.handler, (String)thinking, (StreamingHandle)streamingHandle);
                    }
                    if (Utils.isNotNullOrEmpty((String)(signature = data.delta.signature))) {
                        this.thinkingSignatures.add(signature);
                    }
                } else if (DefaultAnthropicClient.CONTENT_BLOCK_REDACTED_THINKING.equals(blockType) && options.returnThinking()) {
                    String redactedThinking = data.delta.data;
                    if (Utils.isNotNullOrEmpty((String)redactedThinking)) {
                        this.redactedThinkings.add(redactedThinking);
                    }
                } else if (DefaultAnthropicClient.CONTENT_BLOCK_TOOL_USE.equals(blockType) && Utils.isNotNullOrEmpty((String)(partialJson = data.delta.partialJson))) {
                    ToolCallBuilder toolCallBuilder = this.toolCallBuilders.get(data.index);
                    toolCallBuilder.appendArguments(partialJson);
                    PartialToolCall partialToolRequest = PartialToolCall.builder().index(toolCallBuilder.index()).id(toolCallBuilder.id()).name(toolCallBuilder.name()).partialArguments(partialJson).build();
                    InternalStreamingChatResponseHandlerUtils.onPartialToolCall((StreamingChatResponseHandler)this.handler, (PartialToolCall)partialToolRequest, (StreamingHandle)streamingHandle);
                }
            }

            private void handleContentBlockStop(AnthropicStreamingData data, StreamingHandle streamingHandle) {
                String blockType = this.contentBlockTypes.remove(data.index);
                if (DefaultAnthropicClient.CONTENT_BLOCK_TEXT.equals(blockType)) {
                    this.contents.add(this.contentBuilder.toString());
                    this.contentBuilder.setLength(0);
                } else if (DefaultAnthropicClient.CONTENT_BLOCK_THINKING.equals(blockType) && options.returnThinking()) {
                    this.thinkings.add(this.thinkingBuilder.toString());
                    this.thinkingBuilder.setLength(0);
                } else if (DefaultAnthropicClient.CONTENT_BLOCK_TOOL_USE.equals(blockType)) {
                    ToolCallBuilder toolCallBuilder = this.toolCallBuilders.remove(data.index);
                    CompleteToolCall completeToolCall = toolCallBuilder.buildAndReset();
                    this.completedToolExecutionRequests.add(completeToolCall.toolExecutionRequest());
                    if (completeToolCall.toolExecutionRequest().arguments().equals("{}")) {
                        PartialToolCall partialToolRequest = PartialToolCall.builder().index(completeToolCall.index()).id(completeToolCall.toolExecutionRequest().id()).name(completeToolCall.toolExecutionRequest().name()).partialArguments(completeToolCall.toolExecutionRequest().arguments()).build();
                        InternalStreamingChatResponseHandlerUtils.onPartialToolCall((StreamingChatResponseHandler)this.handler, (PartialToolCall)partialToolRequest, (StreamingHandle)streamingHandle);
                    }
                    InternalStreamingChatResponseHandlerUtils.onCompleteToolCall((StreamingChatResponseHandler)this.handler, (CompleteToolCall)completeToolCall);
                }
            }

            private void handleMessageDelta(AnthropicStreamingData data) {
                if (data.delta != null) {
                    AnthropicDelta delta = data.delta;
                    if (delta.stopReason != null) {
                        this.stopReason = delta.stopReason;
                    }
                }
                if (data.usage != null) {
                    this.handleUsage(data.usage);
                }
            }

            private void handleMessageStop() {
                ChatResponse completeResponse = this.build();
                InternalStreamingChatResponseHandlerUtils.onCompleteResponse((StreamingChatResponseHandler)this.handler, (ChatResponse)completeResponse);
            }

            private ChatResponse build() {
                String text = this.contents.stream().filter(content -> !content.isEmpty()).collect(Collectors.joining("\n"));
                String thinking = this.thinkings.stream().filter(content -> !content.isEmpty()).collect(Collectors.joining("\n"));
                HashMap<String, Object> attributes = new HashMap<String, Object>();
                String thinkingSignature = this.thinkingSignatures.stream().filter(content -> !content.isEmpty()).collect(Collectors.joining("\n"));
                if (Utils.isNotNullOrBlank((String)thinkingSignature)) {
                    attributes.put("thinking_signature", thinkingSignature);
                }
                if (!this.redactedThinkings.isEmpty()) {
                    attributes.put(DefaultAnthropicClient.CONTENT_BLOCK_REDACTED_THINKING, this.redactedThinkings);
                }
                if (options.returnServerToolResults() && !this.serverToolResults.isEmpty()) {
                    attributes.put("server_tool_results", this.serverToolResults);
                }
                ArrayList<ToolExecutionRequest> toolExecutionRequests = new ArrayList<ToolExecutionRequest>(this.completedToolExecutionRequests);
                AnthropicTokenUsage tokenUsage = AnthropicTokenUsage.builder().inputTokenCount(this.inputTokenCount.get()).outputTokenCount(this.outputTokenCount.get()).cacheCreationInputTokens(this.cacheCreationInputTokens.get()).cacheReadInputTokens(this.cacheReadInputTokens.get()).build();
                FinishReason finishReason = AnthropicMapper.toFinishReason(this.stopReason);
                ChatResponseMetadata chatResponseMetadata = this.createMetadata(tokenUsage, finishReason);
                AiMessage aiMessage = AiMessage.builder().text(Utils.isNullOrEmpty((String)text) ? null : text).thinking(Utils.isNullOrEmpty((String)thinking) ? null : thinking).toolExecutionRequests(toolExecutionRequests).attributes(attributes).build();
                return ChatResponse.builder().aiMessage(aiMessage).metadata(chatResponseMetadata).build();
            }

            private ChatResponseMetadata createMetadata(AnthropicTokenUsage tokenUsage, FinishReason finishReason) {
                AnthropicChatResponseMetadata.Builder metadataBuilder = AnthropicChatResponseMetadata.builder();
                if (this.responseId.get() != null) {
                    metadataBuilder.id(this.responseId.get());
                }
                if (this.responseModel.get() != null) {
                    metadataBuilder.modelName(this.responseModel.get());
                }
                if (tokenUsage != null) {
                    metadataBuilder.tokenUsage(tokenUsage);
                }
                if (finishReason != null) {
                    metadataBuilder.finishReason(finishReason);
                }
                if (this.rawHttpResponse.get() != null) {
                    metadataBuilder.rawHttpResponse(this.rawHttpResponse.get());
                }
                if (!this.rawServerSentEvents.isEmpty()) {
                    metadataBuilder.rawServerSentEvents(new ArrayList<ServerSentEvent>(this.rawServerSentEvents));
                }
                if (this.responseDiagnostics.get() != null) {
                    metadataBuilder.cacheDiagnostics(AnthropicMapper.toCacheDiagnostics(this.responseDiagnostics.get()));
                }
                return metadataBuilder.build();
            }

            private void handleError(AnthropicStreamingData data) {
                InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> this.handler.onError((Throwable)((Object)new AnthropicStreamingException(data.error.message, data.error.type))));
            }

            public void onError(Throwable error) {
                RuntimeException mappedError = ExceptionMapper.DEFAULT.mapException(error);
                InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> this.handler.onError((Throwable)mappedError));
            }
        };
        HttpRequest httpRequest = this.toHttpRequest(Json.toJson(request), "messages");
        this.httpClient.execute(httpRequest, eventListener);
    }

    @Override
    public MessageTokenCountResponse countTokens(AnthropicCountTokensRequest request) {
        HttpRequest httpRequest = this.toHttpRequest(Json.toJson(request), "messages/count_tokens");
        SuccessfulHttpResponse successfulHttpResponse = this.httpClient.execute(httpRequest);
        return Json.fromJson(successfulHttpResponse.body(), MessageTokenCountResponse.class);
    }

    @Override
    public AnthropicModelsListResponse listModels() {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.GET).url(this.baseUrl, "models").addHeader("x-api-key", new String[]{this.apiKey}).addHeader("anthropic-version", new String[]{this.version}).addHeaders(this.customHeadersSupplier.get()).build();
        SuccessfulHttpResponse successfulHttpResponse = this.httpClient.execute(httpRequest);
        return Json.fromJson(successfulHttpResponse.body(), AnthropicModelsListResponse.class);
    }

    @Override
    public void createMessage(AnthropicCreateMessageRequest request, StreamingChatResponseHandler handler) {
        this.createMessage(request, new AnthropicCreateMessageOptions(false), handler);
    }

    private HttpRequest toHttpRequest(String jsonRequest, String path) {
        HttpRequest.Builder builder = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, path).addHeader("Content-Type", new String[]{"application/json"}).addHeader("x-api-key", new String[]{this.apiKey}).addHeader("anthropic-version", new String[]{this.version}).addHeaders(this.customHeadersSupplier.get()).body(jsonRequest);
        if (this.beta != null) {
            builder.addHeader("anthropic-beta", new String[]{this.beta});
        }
        return builder.build();
    }

    public static class Builder
    extends AnthropicClient.Builder<DefaultAnthropicClient, Builder> {
        @Override
        public DefaultAnthropicClient build() {
            return new DefaultAnthropicClient(this);
        }
    }
}

