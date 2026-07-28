package dev.langchain4j.mcp.client.transport.http;

import static dev.langchain4j.internal.Utils.getOrDefault;
import static dev.langchain4j.internal.ValidationUtils.ensureNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.internal.DefaultExecutorProvider;
import dev.langchain4j.mcp.client.McpCallContext;
import dev.langchain4j.mcp.client.McpHeadersSupplier;
import dev.langchain4j.mcp.client.logging.McpLoggers;
import dev.langchain4j.mcp.client.transport.McpOperationHandler;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.protocol.McpClientMessage;
import dev.langchain4j.mcp.protocol.McpInitializationNotification;
import dev.langchain4j.mcp.protocol.McpInitializeRequest;
import dev.langchain4j.mcp.protocol.McpJsonRpcMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamableHttpMcpTransport implements McpTransport {
    private static final Logger LOG = LoggerFactory.getLogger(StreamableHttpMcpTransport.class);
    private static final long DEFAULT_SUBSIDIARY_RETRY_MS = 5000;
    private static final X509TrustManager DEFAULT_TRUST_MANAGER = getDefaultTrustManager();
    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final String url;
    private final McpHeadersSupplier customHeadersSupplier;
    private final boolean logResponses;
    private final boolean logRequests;
    private final Logger trafficLog;
    private final AtomicReference<CompletableFuture<JsonNode>> initializeInProgress = new AtomicReference<>(null);
    private volatile McpOperationHandler operationHandler;
    private final OkHttpClient httpClient;
    private final SSLContext sslContext;
    private McpInitializeRequest initializeRequest;
    private final AtomicReference<String> mcpSessionId = new AtomicReference<>();

    // Subsidiary SSE channel fields
    private final boolean subsidiaryChannelEnabled;
    private volatile Runnable onFailureCallback;
    private volatile boolean subsidiaryChannelEstablished;
    private final AtomicReference<String> subsidiaryLastEventId = new AtomicReference<>();
    private final AtomicLong subsidiaryRetryMs = new AtomicLong(DEFAULT_SUBSIDIARY_RETRY_MS);
    private final Executor executor;
    private AtomicBoolean closed = new AtomicBoolean(false);
    private volatile EventSource subsidiaryEventSource;

    public StreamableHttpMcpTransport(StreamableHttpMcpTransport.Builder builder) {
        url = ensureNotNull(builder.url, "Missing server endpoint URL");
        logRequests = builder.logRequests;
        logResponses = builder.logResponses;
        trafficLog = getOrDefault(builder.logger, McpLoggers.traffic());
        Duration timeout = getOrDefault(builder.timeout, Duration.ofSeconds(60));
        customHeadersSupplier = getOrDefault(builder.customHeadersSupplier, (i) -> Collections.emptyMap());
        sslContext = builder.sslContext;
        subsidiaryChannelEnabled = builder.subsidiaryChannelEnabled;
        executor = getOrDefault(builder.executor, DefaultExecutorProvider.getDefaultExecutorService());
        OkHttpClient.Builder clientBuilder =
                new OkHttpClient.Builder().connectTimeout(timeout).readTimeout(Duration.ZERO);
        if (builder.forceHttpVersion1_1) {
            clientBuilder.protocols(Collections.singletonList(Protocol.HTTP_1_1));
        }
        if (builder.followRedirects) {
            clientBuilder.followRedirects(true);
        }
        if (sslContext != null) {
            clientBuilder.sslSocketFactory(sslContext.getSocketFactory(), DEFAULT_TRUST_MANAGER);
        }
        if (executor instanceof ExecutorService) {
            clientBuilder.dispatcher(new Dispatcher((ExecutorService) executor));
        }
        httpClient = clientBuilder.build();
    }

    @Override
    public void start(McpOperationHandler operationHandler) {
        this.operationHandler = operationHandler;
    }

    @Override
    public CompletableFuture<JsonNode> initialize(McpInitializeRequest operation) {
        this.initializeRequest = operation;
        CompletableFuture<JsonNode> completableFuture = execute(new McpCallContext(null, operation), false);
        initializeInProgress.set(completableFuture);
        return completableFuture
                .thenCompose(originalResponse -> {
                    initializeInProgress.set(null);
                    return CompletableFuture.completedFuture(originalResponse);
                })
                .thenCompose(originalResponse -> execute(
                                new McpCallContext(null, new McpInitializationNotification()), false)
                        .thenCompose(nullNode -> CompletableFuture.completedFuture(originalResponse)))
                .thenCompose(originalResponse -> {
                    if (subsidiaryChannelEnabled) {
                        return startSubsidiaryChannel(true)
                                .thenCompose(v -> CompletableFuture.completedFuture(originalResponse));
                    }
                    return CompletableFuture.completedFuture(originalResponse);
                });
    }

    private Request createRequest(McpJsonRpcMessage message, McpCallContext callContext)
            throws JsonProcessingException {
        String body = OBJECT_MAPPER.writeValueAsString(message);
        if (logRequests) {
            trafficLog.info("Request: {}", body);
        }
        final Request.Builder builder = new Request.Builder();
        String sessionId = mcpSessionId.get();
        if (sessionId != null && !(message instanceof McpInitializeRequest)) {
            builder.addHeader("Mcp-Session-Id", sessionId);
        }
        Map<String, String> headers = customHeadersSupplier.apply(callContext);
        if (headers != null) {
            headers.forEach(builder::addHeader);
        }
        return builder.url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json,text/event-stream")
                .post(RequestBody.create(MediaType.parse("application/json"), body))
                .build();
    }

    @Override
    public CompletableFuture<JsonNode> executeOperationWithResponse(McpClientMessage operation) {
        return executeOperationWithResponse(new McpCallContext(null, operation));
    }

    @Override
    public CompletableFuture<JsonNode> executeOperationWithResponse(McpCallContext context) {
        return execute(context, false);
    }

    @Override
    public void executeOperationWithoutResponse(McpClientMessage operation) {
        executeOperationWithoutResponse(new McpCallContext(null, operation));
    }

    @Override
    public void executeOperationWithoutResponse(McpCallContext context) {
        execute(context, false);
    }

    @Override
    public void checkHealth() {
        // no transport-specific checks right now
    }

    @Override
    public void onFailure(Runnable actionOnFailure) {
        this.onFailureCallback = actionOnFailure;
    }

    /**
     * Returns the MCP session ID assigned by the server, or {@code null} if no session
     * has been established yet (or the server does not use sessions). The session ID is
     * captured from the {@code Mcp-Session-Id} response header during initialization
     * and reused on subsequent requests via the same header.
     */
    public String getMcpSessionId() {
        return mcpSessionId.get();
    }

    /**
     * Sets the MCP session ID to be sent on subsequent requests via the
     * {@code Mcp-Session-Id} header. This is intended for scenarios where a session
     * obtained elsewhere (for example, in another process or pod) needs to be resumed
     * by this transport, allowing stateless deployments without sticky sessions.
     */
    public void setMcpSessionId(String mcpSessionId) {
        this.mcpSessionId.set(mcpSessionId);
    }

    private CompletableFuture<JsonNode> execute(McpCallContext context, boolean isRetry) {
        Long id = context.message().getId();
        if (!(context.message() instanceof McpInitializeRequest)) {
            CompletableFuture<JsonNode> reinitializeInProgress = this.initializeInProgress.get();
            if (reinitializeInProgress != null) {
                reinitializeInProgress.join();
            }
        }
        Request request = null;
        try {
            request = createRequest(
                    context.message(), new McpCallContext(context.invocationContext(), context.message()));
        } catch (JsonProcessingException e) {
            CompletableFuture<JsonNode> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
        CompletableFuture<JsonNode> future = new CompletableFuture<>();
        if (id != null) {
            operationHandler.startOperation(id, future);
        }

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int statusCode = response.code();
                if (!isExpectedStatusCode(statusCode)) {
                    if (!(context.message() instanceof McpInitializeRequest) && statusCode == 404) {
                        if (!isRetry) {
                            response.close();
                            initialize(StreamableHttpMcpTransport.this.initializeRequest)
                                    .thenAccept(node -> {
                                        execute(context, true)
                                                .thenAccept(future::complete)
                                                .exceptionally(t -> {
                                                    future.completeExceptionally(t);
                                                    return null;
                                                });
                                    })
                                    .exceptionally(t -> {
                                        future.completeExceptionally(t);
                                        return null;
                                    });
                            return;
                        }
                    }
                    future.completeExceptionally(
                            new RuntimeException("Unexpected status code: " + statusCode));
                    response.close();
                    return;
                }

                String contentType = response.header("Content-Type");
                String sessionId = response.header("Mcp-Session-Id");
                if (sessionId != null) {
                    LOG.debug("Assigned MCP session ID: {}", sessionId);
                    StreamableHttpMcpTransport.this.mcpSessionId.set(sessionId);
                }
                if (id != null
                        && contentType != null
                        && contentType.contains("text/event-stream")) {
                    // the server has started an SSE stream
                    executor.execute(() -> {
                        try {
                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                if (logResponses && !line.trim().isEmpty()) {
                                    trafficLog.info("SSE event received: {}", line);
                                }
                                if (line.startsWith("data:")) {
                                    try {
                                        operationHandler.handle(OBJECT_MAPPER.readTree(line.substring(5)));
                                    } catch (JsonProcessingException e) {
                                        LOG.warn("Failed to parse SSE event: {}", line, e);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            future.completeExceptionally(e);
                        } finally {
                            response.close();
                        }
                    });
                } else {
                    // the server has returned a regular HTTP response
                    try {
                        String responseBody = response.body().string();
                        if (logResponses) {
                            trafficLog.info("Response: {}", responseBody);
                        }
                        if (id == null) {
                            future.complete(null);
                        }
                        JsonNode node = OBJECT_MAPPER.readTree(responseBody);
                        operationHandler.handle(node);
                    } catch (IOException e) {
                        future.completeExceptionally(e);
                    } finally {
                        response.close();
                    }
                }
            }
        });
        return future;
    }

    /**
     * Opens the subsidiary SSE channel by issuing an HTTP GET to the MCP endpoint.
     * This allows the server to send notifications and requests to the client
     * without the client first sending data via HTTP POST.
     *
     * @param firstAttempt if true, failures will not trigger reconnection
     * @return a future that completes when the channel setup attempt finishes
     */
    private CompletableFuture<Void> startSubsidiaryChannel(boolean firstAttempt) {
        if (closed.get()) {
            return CompletableFuture.completedFuture(null);
        }
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .addHeader("Accept", "text/event-stream")
                .get();
        String sessionId = mcpSessionId.get();
        if (sessionId != null) {
            requestBuilder.addHeader("Mcp-Session-Id", sessionId);
        }
        String lastId = subsidiaryLastEventId.get();
        if (lastId != null) {
            requestBuilder.addHeader("Last-Event-ID", lastId);
        }
        Map<String, String> headers = customHeadersSupplier.apply(null);
        if (headers != null) {
            headers.forEach(requestBuilder::addHeader);
        }
        Request request = requestBuilder.build();

        CompletableFuture<Void> result = new CompletableFuture<>();

        EventSourceListener listener = new EventSourceListener() {
            @Override
            public void onOpen(EventSource eventSource, Response response) {
                subsidiaryChannelEstablished = true;
                LOG.debug("Subsidiary SSE channel established");
                result.complete(null);
            }

            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                if (id != null) {
                    subsidiaryLastEventId.set(id);
                }
                if (logResponses && data != null && !data.trim().isEmpty()) {
                    trafficLog.info("SSE event received: data:{}", data);
                }
                if (data != null) {
                    try {
                        operationHandler.handle(OBJECT_MAPPER.readTree(data));
                    } catch (JsonProcessingException e) {
                        LOG.warn("Failed to parse SSE event: {}", data, e);
                    }
                }
            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response) {
                if (!subsidiaryChannelEstablished) {
                    int statusCode = response != null ? response.code() : 0;
                    String contentType = response != null ? response.header("Content-Type") : null;
                    if (firstAttempt) {
                        LOG.warn(
                                "Failed to open subsidiary SSE channel (status={}, contentType={}), will not re-attempt",
                                statusCode,
                                contentType != null ? contentType : "absent");
                    } else {
                        LOG.debug(
                                "Failed to reconnect subsidiary SSE channel (status={}, contentType={}), scheduling retry",
                                statusCode,
                                contentType != null ? contentType : "absent");
                        if (!closed.get()) {
                            scheduleSubsidiaryReconnect();
                        }
                    }
                    result.complete(null);
                } else {
                    // Established stream failure
                    if (response != null && t == null) {
                        // Non-2xx on reconnect, EventSource won't auto-retry
                        if (!closed.get()) {
                            scheduleSubsidiaryReconnect();
                        }
                    }
                    // I/O errors are handled by EventSource's auto-retry
                }
            }

            @Override
            public void onClosed(EventSource eventSource) {
                LOG.debug("Subsidiary SSE channel closed");
                if (!closed.get() && subsidiaryChannelEstablished) {
                    scheduleSubsidiaryReconnect();
                }
            }
        };

        subsidiaryEventSource =
                EventSources.createFactory(httpClient).newEventSource(request, listener);
        return result;
    }

    private void scheduleSubsidiaryReconnect() {
        if (closed.get() || !subsidiaryChannelEstablished) {
            return;
        }
        long delayMs = subsidiaryRetryMs.get();
        LOG.debug("Scheduling subsidiary SSE channel reconnect in {} ms", delayMs);
        executor.execute(() -> {
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            if (!closed.get()) {
                startSubsidiaryChannel(false);
            }
        });
    }

    private boolean isExpectedStatusCode(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    private static X509TrustManager getDefaultTrustManager() {
        try {
            TrustManagerFactory tmf =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);
            for (TrustManager tm : tmf.getTrustManagers()) {
                if (tm instanceof X509TrustManager) {
                    return (X509TrustManager) tm;
                }
            }
            throw new IllegalStateException("No X509TrustManager found");
        } catch (Exception e) {
            throw new RuntimeException("Failed to get default X509TrustManager", e);
        }
    }

    @Override
    public void close() throws IOException {
        closed.set(true);
        if (subsidiaryEventSource != null) {
            subsidiaryEventSource.cancel();
        }
        httpClient.dispatcher().executorService().shutdown();
        httpClient.connectionPool().evictAll();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Executor executor;
        private String url;
        private McpHeadersSupplier customHeadersSupplier;
        private Duration timeout;
        private boolean logRequests = false;
        private boolean logResponses = false;
        private Logger logger;
        private SSLContext sslContext;
        private boolean forceHttpVersion1_1;
        private boolean subsidiaryChannelEnabled = false;
        private boolean followRedirects = false;

        /**
         * The URL of the MCP server.
         */
        public StreamableHttpMcpTransport.Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * The request headers of the MCP server.
         */
        public StreamableHttpMcpTransport.Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = (i) -> customHeaders;
            return this;
        }

        /**
         * A supplier for dynamic request headers of the MCP server.
         * The supplier is called for each request, allowing headers to be updated dynamically.
         */
        public StreamableHttpMcpTransport.Builder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = i -> customHeadersSupplier.get();
            return this;
        }

        /**
         * A supplier for dynamic request headers of the MCP server.
         * The supplier is called for each request, allowing headers to be updated dynamically.
         */
        public StreamableHttpMcpTransport.Builder customHeaders(McpHeadersSupplier customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        /**
         * The connection timeout (applied on the http client level). Application-level
         * timeouts are handled by the MCP client itself.
         */
        public StreamableHttpMcpTransport.Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * Whether to log all requests that are sent over this transport.
         */
        public StreamableHttpMcpTransport.Builder logRequests(boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        /**
         * Whether to log all responses received over this transport.
         */
        public StreamableHttpMcpTransport.Builder logResponses(boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        /**
         * Sets a custom {@link Logger} to be used for traffic logging (both requests and responses).
         * This logger will be used for both regular HTTP responses and Server-Sent Events (SSE) traffic.
         * If not specified, a default logger will be used.
         *
         * @param logger an alternate {@link Logger} to be used instead of the default one provided by Langchain4J for traffic logging.
         * @return {@code this}.
         */
        public StreamableHttpMcpTransport.Builder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        /**
         * An optional {@link Executor} that will be used for executing requests and handling responses.
         * It will also be used for scheduling auto-reconnect attempts of the subsidiary SSE channel if that is enabled.
         * If not provided, a default shared executor will be used.
         */
        public StreamableHttpMcpTransport.Builder executor(Executor executor) {
            this.executor = executor;
            return this;
        }

        /**
         * Supplies a custom {@link SSLContext} used when establishing HTTPS connections to the MCP server,
         * allowing private CAs or certificates.
         */
        public StreamableHttpMcpTransport.Builder sslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        /**
         * Forces the transport to use HTTP/1.1 instead of the default HTTP/2.
         */
        public StreamableHttpMcpTransport.Builder setHttpVersion1_1() {
            this.forceHttpVersion1_1 = true;
            return this;
        }

        /**
         * Enables or disables following HTTP redirects (3xx status codes).
         * When enabled, the transport will automatically follow redirects
         * using the default HTTP redirect policy (always redirect,
         * except from HTTPS to HTTP).
         * Defaults to {@code false}.
         */
        public StreamableHttpMcpTransport.Builder followRedirects(boolean followRedirects) {
            this.followRedirects = followRedirects;
            return this;
        }

        /**
         * Enables or disables the subsidiary SSE channel. When enabled, the transport
         * will open an HTTP GET-based SSE stream after initialization, allowing the
         * server to send notifications and requests to the client without the client
         * first sending data via HTTP POST. If the server does not support the
         * subsidiary channel (returns 405), the transport will log a warning and
         * continue without it. If the stream breaks after being successfully
         * established, the transport will automatically attempt to reconnect.
         * Defaults to {@code false}.
         */
        public StreamableHttpMcpTransport.Builder subsidiaryChannel(boolean subsidiaryChannelEnabled) {
            this.subsidiaryChannelEnabled = subsidiaryChannelEnabled;
            return this;
        }

        public StreamableHttpMcpTransport build() {
            return new StreamableHttpMcpTransport(this);
        }
    }
}
