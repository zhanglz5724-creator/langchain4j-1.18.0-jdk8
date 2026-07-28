package dev.langchain4j.mcp.client.transport.websocket;

import static dev.langchain4j.internal.Utils.getOrDefault;
import static dev.langchain4j.internal.ValidationUtils.ensureNotNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.mcp.client.McpCallContext;
import dev.langchain4j.mcp.client.McpHeadersSupplier;
import dev.langchain4j.mcp.client.logging.McpLoggers;
import dev.langchain4j.mcp.client.transport.McpOperationHandler;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.protocol.McpClientMessage;
import dev.langchain4j.mcp.protocol.McpInitializationNotification;
import dev.langchain4j.mcp.protocol.McpInitializeRequest;
import java.io.IOException;
import java.security.KeyStore;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketMcpTransport implements McpTransport {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketMcpTransport.class);
    private final String url;
    private final McpHeadersSupplier headersSupplier;
    private final boolean logResponses;
    private final boolean logRequests;
    private final Logger trafficLog;
    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private volatile McpOperationHandler operationHandler;
    private volatile McpInitializeRequest initializeRequest;
    private final Duration connectTimeout;
    private volatile SSLContext sslContext;
    private volatile OkHttpClient httpClient;
    private final Executor executor;
    private final AtomicReference<CompletableFuture<WebSocket>> webSocketRef = new AtomicReference<>();
    private volatile boolean closed = false;
    private volatile Runnable actionOnFailure;

    public WebSocketMcpTransport(Builder builder) {
        this.url = ensureNotNull(builder.url, "Missing server endpoint URL");
        this.logResponses = builder.logResponses;
        this.logRequests = builder.logRequests;
        this.trafficLog = getOrDefault(builder.logger, McpLoggers.traffic());
        this.connectTimeout = getOrDefault(builder.timeout, Duration.ofSeconds(60));
        this.headersSupplier = getOrDefault(builder.headersSupplier, (i) -> Collections.emptyMap());
        this.executor = builder.executor;
        this.sslContext = builder.sslContext;
        this.httpClient = createHttpClient();
    }

    private OkHttpClient createHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout);
        if (sslContext != null) {
            try {
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init((KeyStore) null);
                X509TrustManager trustManager = (X509TrustManager) tmf.getTrustManagers()[0];
                clientBuilder.sslSocketFactory(sslContext.getSocketFactory(), trustManager);
            } catch (Exception e) {
                throw new RuntimeException("Failed to configure SSL", e);
            }
        }
        if (executor instanceof ExecutorService) {
            clientBuilder.dispatcher(new Dispatcher((ExecutorService) executor));
        }
        return clientBuilder.build();
    }

    private synchronized WebSocket getWebSocket() {
        try {
            CompletableFuture<WebSocket> future = this.webSocketRef.get();
            if (future == null) {
                return startWebSocket().get();
            }
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            try {
                return startWebSocket().get();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ex);
            } catch (ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void start(McpOperationHandler operationHandler) {
        this.operationHandler = operationHandler;
        startWebSocket();
    }

    private synchronized CompletableFuture<WebSocket> startWebSocket() {
        CompletableFuture<WebSocket> current = this.webSocketRef.get();
        if (current != null && !current.isDone()) {
            return current;
        }

        CompletableFuture<WebSocket> newWebSocketFuture = new CompletableFuture<>();

        Request.Builder requestBuilder = new Request.Builder().url(url);
        headersSupplier.apply(null).forEach(requestBuilder::header);
        Request request = requestBuilder.build();

        try {
            WebSocket ws = httpClient.newWebSocket(
                    request,
                    new WebSocketMcpListener(
                            operationHandler,
                            trafficLog,
                            logResponses,
                            () -> {
                                webSocketRef.set(null);
                            },
                            actionOnFailure,
                            newWebSocketFuture));
        } catch (Exception e) {
            newWebSocketFuture.completeExceptionally(e);
            return newWebSocketFuture;
        }

        if (this.initializeRequest != null) {
            newWebSocketFuture = newWebSocketFuture.thenCompose(webSocket -> execute(
                            new McpCallContext(null, this.initializeRequest),
                            Optional.of(webSocket),
                            this.initializeRequest.getId())
                    .thenCompose(originalResponse -> execute(
                                    new McpCallContext(null, new McpInitializationNotification()),
                                    Optional.of(webSocket),
                                    null)
                            .thenCompose(nullNode -> CompletableFuture.completedFuture(webSocket))));
        }
        this.webSocketRef.set(newWebSocketFuture);
        return newWebSocketFuture;
    }

    @Override
    public CompletableFuture<JsonNode> initialize(McpInitializeRequest operation) {
        this.initializeRequest = operation;
        CompletableFuture<JsonNode> completableFuture =
                execute(new McpCallContext(null, operation), Optional.empty(), operation.getId());
        return completableFuture
                .thenCompose(originalResponse -> {
                    return CompletableFuture.completedFuture(originalResponse);
                })
                .thenCompose(originalResponse -> execute(
                                new McpCallContext(null, new McpInitializationNotification()), Optional.empty(), null)
                        .thenCompose(nullNode -> CompletableFuture.completedFuture(originalResponse)));
    }

    @Override
    public CompletableFuture<JsonNode> executeOperationWithResponse(McpClientMessage request) {
        return executeOperationWithResponse(new McpCallContext(null, request));
    }

    @Override
    public CompletableFuture<JsonNode> executeOperationWithResponse(McpCallContext context) {
        return execute(context, Optional.empty(), context.message().getId());
    }

    @Override
    public void executeOperationWithoutResponse(McpClientMessage request) {
        executeOperationWithoutResponse(new McpCallContext(null, request));
    }

    @Override
    public void executeOperationWithoutResponse(McpCallContext context) {
        execute(context, Optional.empty(), null);
    }

    @Override
    public void checkHealth() {
    }

    @Override
    public void onFailure(Runnable actionOnFailure) {
        this.actionOnFailure = actionOnFailure;
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
        CompletableFuture<WebSocket> future = webSocketRef.get();
        if (future != null) {
            if (future.isDone()) {
                try {
                    WebSocket webSocket = future.get();
                    webSocket.close(1000, "Client closing");
                    LOG.info("WebSocket connection closed");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    LOG.warn("Failed to close WebSocket connection", e);
                }
            }
        }
        if (httpClient != null) {
            httpClient.dispatcher().executorService().shutdown();
            httpClient.connectionPool().evictAll();
        }
    }

    private CompletableFuture<JsonNode> execute(McpCallContext context, Optional<WebSocket> webSocket, Long id) {
        CompletableFuture<JsonNode> future = new CompletableFuture<>();
        if (closed) {
            future.completeExceptionally(new IllegalStateException("Transport is closed"));
            return future;
        }
        if (id != null) {
            operationHandler.startOperation(id, future);
        }
        try {
            String messageJson = OBJECT_MAPPER.writeValueAsString(context.message());
            WebSocket wsToUse = webSocket.orElseGet(() -> getWebSocket());
            if (logRequests) {
                trafficLog.info("> " + messageJson);
            }
            synchronized (wsToUse) {
                boolean sent = wsToUse.send(messageJson);
                if (sent && id == null) {
                    future.complete(null);
                } else if (!sent) {
                    future.completeExceptionally(new IOException("Failed to send WebSocket message"));
                }
            }
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    public void reloadSslContext(SSLContext sslContext) {
        ensureNotNull(sslContext, "sslContext");

        this.sslContext = sslContext;
        this.httpClient = createHttpClient();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private boolean logResponses;
        private boolean logRequests;
        private String url;
        private Logger logger;
        private Executor executor;
        private Duration timeout;
        private SSLContext sslContext;
        private McpHeadersSupplier headersSupplier;

        public Builder logResponses(boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public Builder logRequests(boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * Sets a custom {@link Logger} to be used for websocket traffic logging.
         * If not specified, a default logger will be used.
         *
         * @param logger an alternate {@link Logger} to be used instead of the default one provided by Langchain4J for traffic logging.
         * @return {@code this}.
         */
        public Builder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        /**
         * An optional {@link Executor} that will be used for executing requests and handling responses.
         */
        public Builder executor(Executor executor) {
            this.executor = executor;
            return this;
        }

        /**
         * The connection timeout (applied on the websocket client level). Application-level
         * timeouts are handled by the MCP client itself.
         */
        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder sslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        public Builder headersSupplier(Supplier<Map<String, String>> headersSupplier) {
            this.headersSupplier = (i) -> headersSupplier.get();
            return this;
        }

        public Builder headersSupplier(McpHeadersSupplier headersSupplier) {
            this.headersSupplier = headersSupplier;
            return this;
        }

        public WebSocketMcpTransport build() {
            return new WebSocketMcpTransport(this);
        }
    }
}
