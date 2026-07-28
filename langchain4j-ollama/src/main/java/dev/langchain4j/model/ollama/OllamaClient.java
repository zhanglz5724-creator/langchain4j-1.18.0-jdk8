/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClient
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.http.client.HttpClientBuilderLoader
 *  dev.langchain4j.http.client.HttpMethod
 *  dev.langchain4j.http.client.HttpRequest
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 *  dev.langchain4j.http.client.log.LoggingHttpClient
 *  dev.langchain4j.http.client.sse.CancellationUnsupportedHandle
 *  dev.langchain4j.http.client.sse.ServerSentEvent
 *  dev.langchain4j.http.client.sse.ServerSentEventContext
 *  dev.langchain4j.http.client.sse.ServerSentEventListener
 *  dev.langchain4j.http.client.sse.ServerSentEventParser
 *  dev.langchain4j.http.client.sse.ServerSentEventParsingHandle
 *  dev.langchain4j.http.client.sse.ServerSentEventParsingHandleUtils
 *  dev.langchain4j.internal.ExceptionMapper
 *  dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils
 *  dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler
 *  dev.langchain4j.internal.ToolCallBuilder
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.StreamingResponseHandler
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.CompleteToolCall
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.chat.response.StreamingHandle
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.ollama;

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
import dev.langchain4j.http.client.sse.ServerSentEventParser;
import dev.langchain4j.http.client.sse.ServerSentEventParsingHandle;
import dev.langchain4j.http.client.sse.ServerSentEventParsingHandleUtils;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.InternalStreamingChatResponseHandlerUtils;
import dev.langchain4j.internal.MappingTrackingStreamingChatResponseHandler;
import dev.langchain4j.internal.ToolCallBuilder;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.chat.response.StreamingHandle;
import dev.langchain4j.model.ollama.CompletionRequest;
import dev.langchain4j.model.ollama.CompletionResponse;
import dev.langchain4j.model.ollama.DeleteModelRequest;
import dev.langchain4j.model.ollama.EmbeddingRequest;
import dev.langchain4j.model.ollama.EmbeddingResponse;
import dev.langchain4j.model.ollama.InternalOllamaHelper;
import dev.langchain4j.model.ollama.Message;
import dev.langchain4j.model.ollama.ModelsListResponse;
import dev.langchain4j.model.ollama.OllamaChatRequest;
import dev.langchain4j.model.ollama.OllamaChatResponse;
import dev.langchain4j.model.ollama.OllamaJsonUtils;
import dev.langchain4j.model.ollama.OllamaModelCard;
import dev.langchain4j.model.ollama.OllamaServerSentEventParser;
import dev.langchain4j.model.ollama.OllamaStreamingException;
import dev.langchain4j.model.ollama.OllamaStreamingResponseBuilder;
import dev.langchain4j.model.ollama.RunningModelsListResponse;
import dev.langchain4j.model.ollama.ShowModelInformationRequest;
import dev.langchain4j.model.ollama.ToolCall;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.slf4j.Logger;

class OllamaClient {
    private final HttpClient httpClient;
    private final String baseUrl;
    private final Supplier<Map<String, String>> customHeadersSupplier;

    OllamaClient(Builder builder) {
        HttpClientBuilder httpClientBuilder = (HttpClientBuilder)Utils.getOrDefault((Object)builder.httpClientBuilder, HttpClientBuilderLoader::loadHttpClientBuilder);
        HttpClient httpClient = httpClientBuilder.connectTimeout((Duration)Utils.getOrDefault((Object)Utils.getOrDefault((Object)builder.timeout, (Object)httpClientBuilder.connectTimeout()), (Object)Duration.ofSeconds(15L))).readTimeout((Duration)Utils.getOrDefault((Object)Utils.getOrDefault((Object)builder.timeout, (Object)httpClientBuilder.readTimeout()), (Object)Duration.ofSeconds(60L))).build();
        this.httpClient = builder.logRequests || builder.logResponses ? new LoggingHttpClient(httpClient, Boolean.valueOf(builder.logRequests), Boolean.valueOf(builder.logResponses), builder.logger) : httpClient;
        this.baseUrl = ValidationUtils.ensureNotBlank((String)builder.baseUrl, (String)"baseUrl");
        this.customHeadersSupplier = (Supplier)Utils.getOrDefault((Object)builder.customHeadersSupplier, (Object)new Supplier<Map<String, String>>(){

            @Override
            public Map<String, String> get() {
                return Collections.emptyMap();
            }
        });
    }

    private Map<String, String> buildRequestHeaders() {
        Map<String, String> dynamicHeaders = this.customHeadersSupplier.get();
        if (Utils.isNullOrEmpty(dynamicHeaders)) {
            return Collections.emptyMap();
        }
        return dynamicHeaders;
    }

    static Builder builder() {
        return new Builder();
    }

    CompletionResponse completion(CompletionRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "api/generate").addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).body(OllamaJsonUtils.toJson(request)).build();
        SuccessfulHttpResponse successfulHttpResponse = this.httpClient.execute(httpRequest);
        return OllamaJsonUtils.fromJson(successfulHttpResponse.body(), CompletionResponse.class);
    }

    OllamaChatResponse chat(OllamaChatRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "api/chat").addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).body(OllamaJsonUtils.toJson(request)).build();
        SuccessfulHttpResponse successfulHttpResponse = this.httpClient.execute(httpRequest);
        return OllamaJsonUtils.fromJson(successfulHttpResponse.body(), OllamaChatResponse.class);
    }

    void streamingCompletion(CompletionRequest request, final StreamingResponseHandler<String> handler) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "api/generate").addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).body(OllamaJsonUtils.toJson(request)).build();
        this.httpClient.execute(httpRequest, (ServerSentEventParser)new OllamaServerSentEventParser(), new ServerSentEventListener(){
            final StringBuilder contentBuilder = new StringBuilder();

            public void onEvent(ServerSentEvent event) {
                CompletionResponse completionResponse = OllamaJsonUtils.fromJson(event.data(), CompletionResponse.class);
                String error = completionResponse.getError();
                if (!Utils.isNullOrBlank((String)error)) {
                    this.onError((Throwable)((Object)new OllamaStreamingException(error)));
                    return;
                }
                this.contentBuilder.append(completionResponse.getResponse());
                handler.onNext(completionResponse.getResponse());
                if (Boolean.TRUE.equals(completionResponse.getDone())) {
                    Response response = Response.from((Object)this.contentBuilder.toString(), (TokenUsage)new TokenUsage(completionResponse.getPromptEvalCount(), completionResponse.getEvalCount()));
                    handler.onComplete(response);
                }
            }

            public void onError(Throwable throwable) {
                handler.onError((Throwable)ExceptionMapper.DEFAULT.mapException(throwable));
            }
        });
    }

    void streamingChat(ChatRequest request, final boolean returnThinking, StreamingChatResponseHandler handler) {
        ValidationUtils.ensureNotEmpty((Collection)request.messages(), (String)"messages");
        OllamaChatRequest ollamaChatRequest = InternalOllamaHelper.toOllamaChatRequest(request, true);
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "api/chat").addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).body(OllamaJsonUtils.toJson(ollamaChatRequest)).build();
        final StreamingChatResponseHandler targetHandler = handler;
        this.httpClient.execute(httpRequest, (ServerSentEventParser)new OllamaServerSentEventParser(), new ServerSentEventListener(){
            final MappingTrackingStreamingChatResponseHandler handler;
            final ToolCallBuilder toolCallBuilder;
            final OllamaStreamingResponseBuilder responseBuilder;
            volatile StreamingHandle streamingHandle;
            {
                this.handler = new MappingTrackingStreamingChatResponseHandler(targetHandler);
                this.toolCallBuilder = new ToolCallBuilder();
                this.responseBuilder = new OllamaStreamingResponseBuilder(this.toolCallBuilder, returnThinking);
            }

            public void onEvent(ServerSentEvent event) {
                this.onEvent(event, new ServerSentEventContext((ServerSentEventParsingHandle)new CancellationUnsupportedHandle()));
            }

            public void onEvent(ServerSentEvent event, ServerSentEventContext context) {
                List<ToolCall> toolCalls;
                if (this.streamingHandle == null) {
                    this.streamingHandle = ServerSentEventParsingHandleUtils.toStreamingHandle((ServerSentEventParsingHandle)context.parsingHandle());
                }
                this.handler.resetMappingTracking();
                OllamaChatResponse ollamaChatResponse = OllamaJsonUtils.fromJson(event.data(), OllamaChatResponse.class);
                String error = ollamaChatResponse.getError();
                if (!Utils.isNullOrBlank((String)error)) {
                    this.onError((Throwable)((Object)new OllamaStreamingException(error)));
                    return;
                }
                this.responseBuilder.append(ollamaChatResponse);
                Message message = ollamaChatResponse.getMessage();
                if (message == null) {
                    InternalStreamingChatResponseHandlerUtils.onUnmappedRawEvent((StreamingChatResponseHandler)this.handler, (Object)event);
                    return;
                }
                String content = message.getContent();
                if (!Utils.isNullOrEmpty((String)content)) {
                    InternalStreamingChatResponseHandlerUtils.onPartialResponse((StreamingChatResponseHandler)this.handler, (String)content, (StreamingHandle)this.streamingHandle);
                }
                String thinking = message.getThinking();
                if (returnThinking && !Utils.isNullOrEmpty((String)thinking)) {
                    InternalStreamingChatResponseHandlerUtils.onPartialThinking((StreamingChatResponseHandler)this.handler, (String)thinking, (StreamingHandle)this.streamingHandle);
                }
                if ((toolCalls = message.getToolCalls()) != null) {
                    for (ToolCall toolCall : toolCalls) {
                        int index = (Integer)Utils.getOrDefault((Object)toolCall.getFunction().getIndex(), (Object)0);
                        if (this.toolCallBuilder.index() != index) {
                            InternalStreamingChatResponseHandlerUtils.onCompleteToolCall((StreamingChatResponseHandler)this.handler, (CompleteToolCall)this.toolCallBuilder.buildAndReset());
                            this.toolCallBuilder.updateIndex(Integer.valueOf(index));
                        }
                        this.toolCallBuilder.updateName(toolCall.getFunction().getName());
                        this.toolCallBuilder.updateId(toolCall.getId());
                        String partialArguments = OllamaJsonUtils.toJsonWithoutIdent(toolCall.getFunction().getArguments());
                        if (!Utils.isNotNullOrEmpty((String)partialArguments)) continue;
                        this.toolCallBuilder.appendArguments(partialArguments);
                    }
                }
                if (Boolean.TRUE.equals(ollamaChatResponse.getDone())) {
                    if (this.toolCallBuilder.hasRequests()) {
                        InternalStreamingChatResponseHandlerUtils.onCompleteToolCall((StreamingChatResponseHandler)this.handler, (CompleteToolCall)this.toolCallBuilder.buildAndReset());
                    }
                    ChatResponse completeResponse = this.responseBuilder.build(ollamaChatResponse);
                    InternalStreamingChatResponseHandlerUtils.onCompleteResponse((StreamingChatResponseHandler)this.handler, (ChatResponse)completeResponse);
                }
                if (!this.handler.wasMapped()) {
                    InternalStreamingChatResponseHandlerUtils.onUnmappedRawEvent((StreamingChatResponseHandler)this.handler, (Object)event);
                }
            }

            public void onError(Throwable throwable) {
                RuntimeException mappedException = ExceptionMapper.DEFAULT.mapException(throwable);
                InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> this.handler.onError((Throwable)mappedException));
            }
        });
    }

    EmbeddingResponse embed(EmbeddingRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "api/embed").addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).body(OllamaJsonUtils.toJson(request)).build();
        SuccessfulHttpResponse successfulHttpResponse = this.httpClient.execute(httpRequest);
        return OllamaJsonUtils.fromJson(successfulHttpResponse.body(), EmbeddingResponse.class);
    }

    ModelsListResponse listModels() {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.GET).url(this.baseUrl, "api/tags").addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).build();
        SuccessfulHttpResponse successfulHttpResponse = this.httpClient.execute(httpRequest);
        return OllamaJsonUtils.fromJson(successfulHttpResponse.body(), ModelsListResponse.class);
    }

    OllamaModelCard showInformation(ShowModelInformationRequest showInformationRequest) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl, "api/show").addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).body(OllamaJsonUtils.toJson(showInformationRequest)).build();
        SuccessfulHttpResponse successfulHttpResponse = this.httpClient.execute(httpRequest);
        return OllamaJsonUtils.fromJson(successfulHttpResponse.body(), OllamaModelCard.class);
    }

    RunningModelsListResponse listRunningModels() {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.GET).url(this.baseUrl, "api/ps").addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).build();
        SuccessfulHttpResponse successfulHttpResponse = this.httpClient.execute(httpRequest);
        return OllamaJsonUtils.fromJson(successfulHttpResponse.body(), RunningModelsListResponse.class);
    }

    Void deleteModel(DeleteModelRequest deleteModelRequest) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.DELETE).url(this.baseUrl, "api/delete").addHeader("Content-Type", new String[]{"application/json"}).addHeaders(this.buildRequestHeaders()).body(OllamaJsonUtils.toJson(deleteModelRequest)).build();
        this.httpClient.execute(httpRequest);
        return null;
    }

    static class Builder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private Duration timeout;
        private boolean logRequests;
        private boolean logResponses;
        private Logger logger;
        private Supplier<Map<String, String>> customHeadersSupplier;

        Builder() {
        }

        Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        Builder logRequests(Boolean logRequests) {
            if (logRequests == null) {
                logRequests = false;
            }
            this.logRequests = logRequests;
            return this;
        }

        Builder logResponses(Boolean logResponses) {
            if (logResponses == null) {
                logResponses = false;
            }
            this.logResponses = logResponses;
            return this;
        }

        Builder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this;
        }

        Builder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        OllamaClient build() {
            return new OllamaClient(this);
        }
    }
}

