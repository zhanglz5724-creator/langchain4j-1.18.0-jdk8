/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.http.client.HttpClient
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.http.client.HttpClientBuilderLoader
 *  dev.langchain4j.http.client.HttpMethod
 *  dev.langchain4j.http.client.HttpRequest
 *  dev.langchain4j.http.client.HttpRequest$Builder
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
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.CompleteToolCall
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.chat.response.StreamingHandle
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.googleai;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.http.client.HttpClientBuilderLoader;
import dev.langchain4j.http.client.HttpMethod;
import dev.langchain4j.http.client.HttpRequest;
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
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.chat.response.StreamingHandle;
import dev.langchain4j.model.googleai.BatchRequestResponse;
import dev.langchain4j.model.googleai.GeminiCountTokensRequest;
import dev.langchain4j.model.googleai.GeminiCountTokensResponse;
import dev.langchain4j.model.googleai.GeminiEmbeddingRequestResponse;
import dev.langchain4j.model.googleai.GeminiGenerateContentRequest;
import dev.langchain4j.model.googleai.GeminiGenerateContentResponse;
import dev.langchain4j.model.googleai.GeminiModelsListResponse;
import dev.langchain4j.model.googleai.GeminiStreamingResponseBuilder;
import dev.langchain4j.model.googleai.Json;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

class GeminiService {
    private static final String GEMINI_AI_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta";
    private static final String API_KEY_HEADER_NAME = "x-goog-api-key";
    private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(15L);
    private static final Duration DEFAULT_READ_TIMEOUT = Duration.ofSeconds(60L);
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String apiKey;
    private final Supplier<Map<String, String>> customHeadersSupplier;

    GeminiService(@Nullable HttpClientBuilder httpClientBuilder, String apiKey, String baseUrl, boolean logRequestsAndResponses, boolean logRequests, boolean logResponses, Logger logger, Duration timeout, @Nullable Supplier<Map<String, String>> customHeadersSupplier) {
        this.apiKey = apiKey;
        this.baseUrl = (String)Utils.getOrDefault((Object)baseUrl, (Object)GEMINI_AI_ENDPOINT);
        this.customHeadersSupplier = customHeadersSupplier;
        HttpClientBuilder builder = (HttpClientBuilder)Utils.getOrDefault((Object)httpClientBuilder, HttpClientBuilderLoader::loadHttpClientBuilder);
        HttpClient httpClient = builder.connectTimeout((Duration)Utils.firstNotNull((String)"connectTimeout", (Object[])new Duration[]{timeout, builder.connectTimeout(), DEFAULT_CONNECT_TIMEOUT})).readTimeout((Duration)Utils.firstNotNull((String)"readTimeout", (Object[])new Duration[]{timeout, builder.readTimeout(), DEFAULT_READ_TIMEOUT})).build();
        this.httpClient = logRequestsAndResponses || logResponses || logRequests ? new LoggingHttpClient(httpClient, Boolean.valueOf(logRequestsAndResponses || logRequests), Boolean.valueOf(logRequestsAndResponses || logResponses), logger) : httpClient;
    }

    GeminiGenerateContentResponse generateContent(String modelName, GeminiGenerateContentRequest request) {
        String url = String.format("%s/models/%s:generateContent", this.baseUrl, modelName);
        return this.sendRequest(url, this.apiKey, request, GeminiGenerateContentResponse.class);
    }

    <REQ, RESP> BatchRequestResponse.Operation<RESP> batchCreate(String modelName, BatchRequestResponse.BatchCreateRequest<REQ> request, BatchOperationType operationType) {
        return this.sendRequest(String.format("%s/models/%s:%s", this.baseUrl, modelName, operationType.value), this.apiKey, request, BatchRequestResponse.Operation.class);
    }

    <REQ, RESP> BatchRequestResponse.Operation<RESP> batchCreate(String modelName, BatchRequestResponse.BatchCreateFileRequest request, BatchOperationType operationType) {
        return this.sendRequest(String.format("%s/models/%s:%s", this.baseUrl, modelName, operationType.value), this.apiKey, request, BatchRequestResponse.Operation.class);
    }

    <RESP> BatchRequestResponse.Operation<RESP> batchRetrieveBatch(String operationName) {
        return this.sendRequest(String.format("%s/%s", this.baseUrl, operationName), this.apiKey, null, BatchRequestResponse.Operation.class, HttpMethod.GET);
    }

    Void batchCancelBatch(String operationName) {
        String url = String.format("%s/%s:cancel", this.baseUrl, operationName);
        return this.sendRequest(url, this.apiKey, null, Void.class);
    }

    Void batchDeleteBatch(String batchName) {
        String url = String.format("%s/%s", this.baseUrl, batchName);
        return this.sendRequest(url, this.apiKey, null, Void.class, HttpMethod.DELETE);
    }

    <RESP> BatchRequestResponse.ListOperationsResponse<RESP> batchListBatches(@Nullable Integer pageSize, @Nullable String pageToken) {
        String url = GeminiService.buildUrl(this.baseUrl + "/batches", new StringPair("pageSize", pageSize != null ? String.valueOf(pageSize) : null), new StringPair("pageToken", pageToken));
        return this.sendRequest(url, this.apiKey, null, BatchRequestResponse.ListOperationsResponse.class, HttpMethod.GET);
    }

    GeminiCountTokensResponse countTokens(String modelName, GeminiCountTokensRequest request) {
        String url = String.format("%s/models/%s:countTokens", this.baseUrl, modelName);
        return this.sendRequest(url, this.apiKey, request, GeminiCountTokensResponse.class);
    }

    GeminiEmbeddingRequestResponse.GeminiEmbeddingResponse embed(String modelName, GeminiEmbeddingRequestResponse.GeminiEmbeddingRequest request) {
        String url = String.format("%s/models/%s:embedContent", this.baseUrl, modelName);
        return this.sendRequest(url, this.apiKey, request, GeminiEmbeddingRequestResponse.GeminiEmbeddingResponse.class);
    }

    GeminiEmbeddingRequestResponse.GeminiBatchEmbeddingResponse batchEmbed(String modelName, GeminiEmbeddingRequestResponse.GeminiBatchEmbeddingRequest request) {
        String url = String.format("%s/models/%s:batchEmbedContents", this.baseUrl, modelName);
        return this.sendRequest(url, this.apiKey, request, GeminiEmbeddingRequestResponse.GeminiBatchEmbeddingResponse.class);
    }

    GeminiModelsListResponse listModels(@Nullable Integer pageSize, @Nullable String pageToken) {
        String url = GeminiService.buildUrl(this.baseUrl + "/models", new StringPair("pageSize", pageSize != null ? String.valueOf(pageSize) : null), new StringPair("pageToken", pageToken));
        return this.sendRequest(url, this.apiKey, null, GeminiModelsListResponse.class, HttpMethod.GET);
    }

    void generateContentStream(String modelName, GeminiGenerateContentRequest request, boolean includeCodeExecutionOutput, Boolean returnThinking, StreamingChatResponseHandler handler) {
        String url = String.format("%s/models/%s:streamGenerateContent?alt=sse", this.baseUrl, modelName);
        this.streamRequest(url, this.apiKey, request, includeCodeExecutionOutput, returnThinking, handler);
    }

    private <T> T sendRequest(String url, String apiKey, @Nullable Object requestBody, Class<T> responseType) {
        return this.sendRequest(url, apiKey, requestBody, responseType, HttpMethod.POST);
    }

    private <T> T sendRequest(String url, String apiKey, @Nullable Object requestBody, Class<T> responseType, HttpMethod httpMethod) {
        HttpRequest request = this.buildHttpRequest(url, apiKey, requestBody, httpMethod);
        return Json.fromJson(this.httpClient.execute(request).body(), responseType);
    }

    private void streamRequest(String url, String apiKey, Object requestBody, boolean includeCodeExecutionOutput, final Boolean returnThinking, StreamingChatResponseHandler handler) {
        HttpRequest httpRequest = this.buildHttpRequest(url, apiKey, requestBody, HttpMethod.POST);
        final GeminiStreamingResponseBuilder responseBuilder = new GeminiStreamingResponseBuilder(includeCodeExecutionOutput, returnThinking);
        final StreamingChatResponseHandler targetHandler = handler;
        this.httpClient.execute(httpRequest, new ServerSentEventListener(){
            final MappingTrackingStreamingChatResponseHandler handler;
            AtomicInteger toolIndex;
            volatile StreamingHandle streamingHandle;
            {
                this.handler = new MappingTrackingStreamingChatResponseHandler(targetHandler);
                this.toolIndex = new AtomicInteger(0);
            }

            public void onEvent(ServerSentEvent event) {
                this.onEvent(event, new ServerSentEventContext((ServerSentEventParsingHandle)new CancellationUnsupportedHandle()));
            }

            public void onEvent(ServerSentEvent event, ServerSentEventContext context) {
                if (this.streamingHandle == null) {
                    this.streamingHandle = ServerSentEventParsingHandleUtils.toStreamingHandle((ServerSentEventParsingHandle)context.parsingHandle());
                }
                this.handler.resetMappingTracking();
                GeminiGenerateContentResponse response = Json.fromJson(event.data(), GeminiGenerateContentResponse.class);
                GeminiStreamingResponseBuilder.TextAndTools textAndTools = responseBuilder.append(response);
                textAndTools.maybeText().ifPresent(text -> InternalStreamingChatResponseHandlerUtils.onPartialResponse((StreamingChatResponseHandler)this.handler, (String)text, (StreamingHandle)this.streamingHandle));
                textAndTools.maybeThought().ifPresent(thought -> {
                    if (Boolean.TRUE.equals(returnThinking)) {
                        InternalStreamingChatResponseHandlerUtils.onPartialThinking((StreamingChatResponseHandler)this.handler, (String)thought, (StreamingHandle)this.streamingHandle);
                    } else if (returnThinking == null) {
                        InternalStreamingChatResponseHandlerUtils.onPartialResponse((StreamingChatResponseHandler)this.handler, (String)thought, (StreamingHandle)this.streamingHandle);
                    }
                });
                for (ToolExecutionRequest tool : textAndTools.tools()) {
                    CompleteToolCall completeToolCall = new CompleteToolCall(this.toolIndex.get(), tool);
                    InternalStreamingChatResponseHandlerUtils.onCompleteToolCall((StreamingChatResponseHandler)this.handler, (CompleteToolCall)completeToolCall);
                    this.toolIndex.incrementAndGet();
                }
                if (!this.handler.wasMapped()) {
                    InternalStreamingChatResponseHandlerUtils.onUnmappedRawEvent((StreamingChatResponseHandler)this.handler, (Object)event);
                }
            }

            public void onClose() {
                if (this.streamingHandle == null || !this.streamingHandle.isCancelled()) {
                    ChatResponse completeResponse = responseBuilder.build();
                    InternalStreamingChatResponseHandlerUtils.onCompleteResponse((StreamingChatResponseHandler)this.handler, (ChatResponse)completeResponse);
                }
            }

            public void onError(Throwable error) {
                RuntimeException mappedError = ExceptionMapper.DEFAULT.mapException(error);
                InternalStreamingChatResponseHandlerUtils.withLoggingExceptions(() -> this.handler.onError((Throwable)mappedError));
            }
        });
    }

    private HttpRequest buildHttpRequest(String url, String apiKey, @Nullable Object body, HttpMethod method) {
        Map<String, String> customHeaders;
        HttpRequest.Builder builder = HttpRequest.builder().method(method).url(url).addHeader("Content-Type", new String[]{"application/json"}).addHeader("User-Agent", new String[]{"LangChain4j"});
        if (apiKey != null) {
            builder.addHeader(API_KEY_HEADER_NAME, new String[]{apiKey});
        }
        if (this.customHeadersSupplier != null && (customHeaders = this.customHeadersSupplier.get()) != null) {
            customHeaders.forEach((x$0, xva$1) -> builder.addHeader(x$0, new String[]{xva$1}));
        }
        if (body != null) {
            builder.body(Json.toJson(body));
        }
        return builder.build();
    }

    private static String buildUrl(String baseUrl, StringPair ... pairs) {
        StringBuilder sb = new StringBuilder();
        for (StringPair pair : pairs) {
            if (pair.value() == null) continue;
            if (sb.length() > 0) {
                sb.append("&");
            }
            try {
                sb.append(pair.key()).append("=").append(URLEncoder.encode(pair.value(), "UTF-8"));
            }
            catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        String queryParams = sb.toString();
        return queryParams.isEmpty() ? baseUrl : baseUrl + "?" + queryParams;
    }

    private static final class StringPair {
        private final String key;
        private final @Nullable String value;

        StringPair(String key, @Nullable String value) {
            this.key = key;
            this.value = value;
        }

        String key() {
            return this.key;
        }

        @Nullable String value() {
            return this.value;
        }
    }

    static enum BatchOperationType {
        BATCH_GENERATE_CONTENT("batchGenerateContent"),
        ASYNC_BATCH_EMBED_CONTENT("asyncBatchEmbedContent");

        private final String value;

        private BatchOperationType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}

