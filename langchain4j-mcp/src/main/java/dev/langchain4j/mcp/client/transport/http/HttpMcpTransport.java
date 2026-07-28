/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  okhttp3.Call
 *  okhttp3.Callback
 *  okhttp3.Headers
 *  okhttp3.Headers$Builder
 *  okhttp3.Interceptor
 *  okhttp3.OkHttpClient
 *  okhttp3.OkHttpClient$Builder
 *  okhttp3.Request
 *  okhttp3.Request$Builder
 *  okhttp3.RequestBody
 *  okhttp3.Response
 *  okhttp3.sse.EventSource
 *  okhttp3.sse.EventSourceListener
 *  okhttp3.sse.EventSources
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.mcp.client.transport.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.mcp.client.McpCallContext;
import dev.langchain4j.mcp.client.McpHeadersSupplier;
import dev.langchain4j.mcp.client.transport.McpOperationHandler;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.McpRequestLoggingInterceptor;
import dev.langchain4j.mcp.client.transport.http.SseEventListener;
import dev.langchain4j.mcp.protocol.McpClientMessage;
import dev.langchain4j.mcp.protocol.McpInitializationNotification;
import dev.langchain4j.mcp.protocol.McpInitializeRequest;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class HttpMcpTransport
implements McpTransport {
    private static final Logger log = LoggerFactory.getLogger(HttpMcpTransport.class);
    private final String sseUrl;
    private final McpHeadersSupplier customHeadersSupplier;
    private final OkHttpClient client;
    private final boolean logResponses;
    private final boolean logRequests;
    private EventSource mcpSseEventListener;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private volatile Runnable onFailure;
    private volatile String postUrl;
    private volatile McpOperationHandler messageHandler;

    public HttpMcpTransport(Builder builder) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        Duration timeout = (Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L));
        httpClientBuilder.callTimeout(timeout);
        httpClientBuilder.connectTimeout(timeout);
        httpClientBuilder.readTimeout(timeout);
        httpClientBuilder.writeTimeout(timeout);
        this.logRequests = builder.logRequests;
        if (builder.logRequests) {
            httpClientBuilder.addInterceptor((Interceptor)new McpRequestLoggingInterceptor(builder.logger));
        }
        this.logResponses = builder.logResponses;
        this.sseUrl = (String)ValidationUtils.ensureNotNull((Object)builder.sseUrl, (String)"Missing SSE endpoint URL");
        this.customHeadersSupplier = (McpHeadersSupplier)Utils.getOrDefault((Object)builder.customHeadersSupplier, (McpHeadersSupplier)(i -> Collections.emptyMap()));
        this.client = httpClientBuilder.build();
    }

    @Override
    public void start(McpOperationHandler messageHandler) {
        this.messageHandler = messageHandler;
        this.mcpSseEventListener = this.startSseChannel(this.logResponses);
    }

    @Override
    public CompletableFuture<JsonNode> initialize(McpInitializeRequest operation) {
        Request httpRequest = null;
        Request initializationNotification = null;
        try {
            httpRequest = this.createRequest(new McpCallContext(null, operation));
            initializationNotification = this.createRequest(new McpCallContext(null, new McpInitializationNotification()));
        }
        catch (JsonProcessingException e) {
            CompletableFuture<JsonNode> future = new CompletableFuture<JsonNode>();
            future.completeExceptionally(e);
            return future;
        }
        Request finalInitializationNotification = initializationNotification;
        return this.execute(httpRequest, operation.getId()).thenCompose(originalResponse -> this.execute(finalInitializationNotification, null).thenCompose(nullNode -> CompletableFuture.completedFuture(originalResponse)));
    }

    @Override
    public CompletableFuture<JsonNode> executeOperationWithResponse(McpClientMessage operation) {
        return this.executeOperationWithResponse(new McpCallContext(null, operation));
    }

    @Override
    public CompletableFuture<JsonNode> executeOperationWithResponse(McpCallContext context) {
        try {
            Request httpRequest = this.createRequest(context);
            return this.execute(httpRequest, context.message().getId());
        }
        catch (JsonProcessingException e) {
            CompletableFuture<JsonNode> future = new CompletableFuture<JsonNode>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    public void executeOperationWithoutResponse(McpClientMessage operation) {
        this.executeOperationWithoutResponse(new McpCallContext(null, operation));
    }

    @Override
    public void executeOperationWithoutResponse(McpCallContext context) {
        try {
            Request httpRequest = this.createRequest(context);
            this.execute(httpRequest, null);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void checkHealth() {
    }

    @Override
    public void onFailure(Runnable actionOnFailure) {
        this.onFailure = actionOnFailure;
    }

    private CompletableFuture<JsonNode> execute(Request request, final Long id) {
        final CompletableFuture<JsonNode> future = new CompletableFuture<JsonNode>();
        if (id != null) {
            this.messageHandler.startOperation(id, future);
        }
        this.client.newCall(request).enqueue(new Callback(){

            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }

            public void onResponse(Call call, Response response) throws IOException {
                Response resp;
                try (Response ignored = resp = response;){
                    int statusCode = response.code();
                    if (!HttpMcpTransport.this.isExpectedStatusCode(statusCode)) {
                        future.completeExceptionally(new RuntimeException("Unexpected status code: " + statusCode));
                    }
                    if (id == null) {
                        future.complete(null);
                    }
                }
            }
        });
        return future;
    }

    private boolean isExpectedStatusCode(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    private EventSource startSseChannel(boolean logResponses) {
        Request request = new Request.Builder().url(this.sseUrl).headers(this.buildCommonHeaders(null)).build();
        CompletableFuture initializationFinished = new CompletableFuture();
        SseEventListener listener = new SseEventListener(this.messageHandler, logResponses, initializationFinished, this.onFailure);
        EventSource eventSource = EventSources.createFactory((OkHttpClient)this.client).newEventSource(request, (EventSourceListener)listener);
        try {
            int timeout = this.client.callTimeoutMillis() > 0 ? this.client.callTimeoutMillis() : Integer.MAX_VALUE;
            String relativePostUrl = (String)initializationFinished.get(timeout, TimeUnit.MILLISECONDS);
            this.postUrl = this.buildAbsolutePostUrl(relativePostUrl);
            log.debug("Received the server's POST URL: {}", (Object)this.postUrl);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return eventSource;
    }

    private String buildAbsolutePostUrl(String relativePostUrl) {
        try {
            return URI.create(this.sseUrl).resolve(relativePostUrl).toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Headers buildCommonHeaders(McpCallContext callContext) {
        Headers.Builder headerBuilder = new Headers.Builder();
        Map headers = (Map)this.customHeadersSupplier.apply(callContext);
        if (headers != null) {
            headers.forEach((arg_0, arg_1) -> headerBuilder.add((String)arg_0, (String)arg_1));
        }
        return headerBuilder.build();
    }

    private Request createRequest(McpCallContext callContext) throws JsonProcessingException {
        Headers.Builder headerBuilder = new Headers.Builder().add(CONTENT_TYPE, CONTENT_TYPE_JSON);
        Map headers = (Map)this.customHeadersSupplier.apply(callContext);
        if (headers != null) {
            headers.forEach((arg_0, arg_1) -> headerBuilder.add((String)arg_0, (String)arg_1));
        }
        return new Request.Builder().url(this.postUrl).headers(headerBuilder.build()).post(RequestBody.create((byte[])OBJECT_MAPPER.writeValueAsBytes((Object)callContext.message()))).build();
    }

    @Override
    public void close() throws IOException {
        if (this.mcpSseEventListener != null) {
            this.mcpSseEventListener.cancel();
        }
        if (this.client != null) {
            this.client.dispatcher().executorService().shutdown();
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String sseUrl;
        private McpHeadersSupplier customHeadersSupplier;
        private Duration timeout;
        private boolean logRequests = false;
        private boolean logResponses = false;
        private Logger logger;

        public Builder sseUrl(String sseUrl) {
            this.sseUrl = sseUrl;
            return this;
        }

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = i -> customHeaders;
            return this;
        }

        public Builder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = i -> (Map)customHeadersSupplier.get();
            return this;
        }

        public Builder customHeaders(McpHeadersSupplier customMcpHeadersSupplier) {
            this.customHeadersSupplier = customMcpHeadersSupplier;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder logRequests(boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public Builder logResponses(boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public Builder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public HttpMcpTransport build() {
            return new HttpMcpTransport(this);
        }
    }
}

