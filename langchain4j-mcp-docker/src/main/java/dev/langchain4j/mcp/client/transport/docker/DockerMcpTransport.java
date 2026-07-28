/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.github.dockerjava.api.DockerClient
 *  com.github.dockerjava.api.async.ResultCallback
 *  com.github.dockerjava.api.command.CreateContainerCmd
 *  com.github.dockerjava.api.command.CreateContainerResponse
 *  com.github.dockerjava.api.command.InspectContainerResponse
 *  com.github.dockerjava.api.command.PullImageCmd
 *  com.github.dockerjava.api.command.PullImageResultCallback
 *  com.github.dockerjava.api.model.Bind
 *  com.github.dockerjava.api.model.HostConfig
 *  com.github.dockerjava.core.DefaultDockerClientConfig
 *  com.github.dockerjava.core.DockerClientConfig
 *  com.github.dockerjava.core.DockerClientImpl
 *  com.github.dockerjava.core.NameParser
 *  com.github.dockerjava.core.NameParser$ReposTag
 *  com.github.dockerjava.httpclient5.ApacheDockerHttpClient$Builder
 *  com.github.dockerjava.transport.DockerHttpClient
 *  com.github.dockerjava.transport.SSLConfig
 *  dev.langchain4j.mcp.client.McpCallContext
 *  dev.langchain4j.mcp.client.transport.McpOperationHandler
 *  dev.langchain4j.mcp.client.transport.McpTransport
 *  dev.langchain4j.mcp.protocol.McpClientMessage
 *  dev.langchain4j.mcp.protocol.McpInitializationNotification
 *  dev.langchain4j.mcp.protocol.McpInitializeRequest
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.mcp.client.transport.docker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.NameParser;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.transport.SSLConfig;
import dev.langchain4j.mcp.client.McpCallContext;
import dev.langchain4j.mcp.client.transport.McpOperationHandler;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.docker.DockerResultCallback;
import dev.langchain4j.mcp.protocol.McpClientMessage;
import dev.langchain4j.mcp.protocol.McpInitializationNotification;
import dev.langchain4j.mcp.protocol.McpInitializeRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DockerMcpTransport
implements McpTransport {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(DockerMcpTransport.class);
    private final String dockerHost;
    private final String dockerConfig;
    private final String dockerContext;
    private final String dockerCertPath;
    private final Boolean dockerTlsVerify;
    private final String registryEmail;
    private final String registryUsername;
    private final String registryPassword;
    private final String registryUrl;
    private final String apiVersion;
    private final String image;
    private final List<String> command;
    private final Map<String, String> environment;
    private final boolean logEvents;
    private final Logger logger;
    private final List<String> binds;
    private final Duration attachTimeout;
    private volatile McpOperationHandler messageHandler;
    private volatile String containerId;
    private volatile DockerClient dockerClient;

    public DockerMcpTransport(Builder builder) {
        this.dockerHost = builder.dockerHost;
        this.dockerConfig = builder.dockerConfig;
        this.dockerContext = builder.dockerContext;
        this.dockerCertPath = builder.dockerCertPath;
        this.dockerTlsVerify = builder.dockerTlsVerify;
        this.registryEmail = builder.registryEmail;
        this.registryUsername = builder.registryUsername;
        this.registryPassword = builder.registryPassword;
        this.registryUrl = builder.registryUrl;
        this.apiVersion = builder.apiVersion;
        this.image = builder.image;
        this.command = builder.command;
        this.environment = builder.environment;
        this.logEvents = builder.logEvents;
        this.logger = builder.logger;
        this.binds = builder.binds;
        this.attachTimeout = builder.attachTimeout;
    }

    static DockerHttpClient buildHttpClient(DockerClientConfig config) {
        return new ApacheDockerHttpClient.Builder().dockerHost(config.getDockerHost()).sslConfig((SSLConfig)config.getSSLConfig()).build();
    }

    public void start(McpOperationHandler messageHandler) {
        this.messageHandler = messageHandler;
        log.debug("Starting docker container with host: {}, image: {}, command: {}", new Object[]{this.dockerHost, this.image, this.command});
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost(this.dockerHost).withDockerConfig(this.dockerConfig).withDockerContext(this.dockerContext).withDockerCertPath(this.dockerCertPath).withDockerTlsVerify(this.dockerTlsVerify).withRegistryEmail(this.registryEmail).withRegistryUsername(this.registryUsername).withRegistryPassword(this.registryPassword).withRegistryUrl(this.registryUrl).withApiVersion(this.apiVersion).build();
        DockerHttpClient httpClient = DockerMcpTransport.buildHttpClient((DockerClientConfig)config);
        this.dockerClient = DockerClientImpl.getInstance((DockerClientConfig)config, (DockerHttpClient)httpClient);
        String imageNameWithoutTag = this.getImageNameWithoutTag(this.image);
        NameParser.ReposTag parsedTagFromImage = NameParser.parseRepositoryTag((String)this.image);
        try (PullImageCmd pull = this.dockerClient.pullImageCmd(imageNameWithoutTag);){
            String tag = !parsedTagFromImage.tag.isEmpty() ? parsedTagFromImage.tag : "latest";
            String repository = pull.getRepository().contains(":") ? pull.getRepository().split(":")[0] : pull.getRepository();
            ((PullImageResultCallback)pull.withTag(tag).exec((ResultCallback)new PullImageResultCallback())).awaitCompletion();
            log.trace("Image pulled [{}:{}]", (Object)repository, (Object)tag);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        HostConfig hostConfig = new HostConfig().withBinds(this.binds.stream().map(bind -> Bind.parse((String)bind)).collect(Collectors.toList()));
        CreateContainerCmd container = this.dockerClient.createContainerCmd(this.image).withTty(Boolean.valueOf(false)).withAttachStdin(Boolean.valueOf(true)).withAttachStdout(Boolean.valueOf(true)).withAttachStderr(Boolean.valueOf(true)).withStdinOpen(Boolean.valueOf(true)).withCmd(this.command.toArray(new String[0])).withEnv(this.environment.entrySet().stream().map(r -> (String)r.getKey() + "=" + (String)r.getValue()).collect(Collectors.toList())).withHostConfig(hostConfig);
        try {
            CreateContainerResponse exec = container.exec();
            this.containerId = exec.getId();
            this.dockerClient.startContainerCmd(this.containerId).exec();
            this.dockerClient.waitContainerCmd(this.containerId).start().awaitStarted();
            log.debug("ID of the started container: {}", (Object)exec.getId());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getImageNameWithoutTag(String fullImageName) {
        if (fullImageName == null || fullImageName.isEmpty()) {
            return fullImageName;
        }
        int lastColonIndex = fullImageName.lastIndexOf(58);
        int firstSlashIndex = fullImageName.indexOf(47);
        if (lastColonIndex > -1 && (firstSlashIndex == -1 || lastColonIndex > firstSlashIndex)) {
            return fullImageName.substring(0, lastColonIndex);
        }
        return fullImageName;
    }

    public CompletableFuture<JsonNode> initialize(McpInitializeRequest operation) {
        try {
            String requestString = OBJECT_MAPPER.writeValueAsString((Object)operation);
            String initializationNotification = OBJECT_MAPPER.writeValueAsString((Object)new McpInitializationNotification());
            CompletableFuture<JsonNode> execute = this.execute(requestString, operation.getId());
            return execute.thenCompose(originalResponse -> {
                CompletableFuture<JsonNode> execute1 = this.execute(initializationNotification, null);
                return execute1.thenCompose(nullNode -> CompletableFuture.completedFuture(originalResponse));
            });
        }
        catch (JsonProcessingException e) {
            CompletableFuture<JsonNode> future = new CompletableFuture<JsonNode>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public CompletableFuture<JsonNode> executeOperationWithResponse(McpClientMessage operation) {
        return this.executeOperationWithResponse(new McpCallContext(null, operation));
    }

    public CompletableFuture<JsonNode> executeOperationWithResponse(McpCallContext context) {
        try {
            String requestString = OBJECT_MAPPER.writeValueAsString((Object)context.message());
            return this.execute(requestString, context.message().getId());
        }
        catch (JsonProcessingException e) {
            CompletableFuture<JsonNode> future = new CompletableFuture<JsonNode>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public void executeOperationWithoutResponse(McpClientMessage operation) {
        this.executeOperationWithoutResponse(new McpCallContext(null, operation));
    }

    public void executeOperationWithoutResponse(McpCallContext context) {
        try {
            String requestString = OBJECT_MAPPER.writeValueAsString((Object)context.message());
            this.execute(requestString, null);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkHealth() {
        InspectContainerResponse inspectContainer = this.dockerClient.inspectContainerCmd(this.containerId).exec();
        if (inspectContainer == null || Boolean.FALSE.equals(inspectContainer.getState().getRunning())) {
            throw new IllegalStateException("Container is not alive");
        }
    }

    public void onFailure(Runnable actionOnFailure) {
    }

    public void close() throws IOException {
        log.debug("Killing container {}", (Object)this.containerId);
        this.dockerClient.killContainerCmd(this.containerId).exec();
        log.debug("Deleting container {}", (Object)this.containerId);
        this.dockerClient.removeContainerCmd(this.containerId).exec();
    }

    private CompletableFuture<JsonNode> execute(String request, Long id) {
        CompletableFuture<JsonNode> future = new CompletableFuture<JsonNode>();
        if (id != null) {
            this.messageHandler.startOperation(id, future);
        }
        try (PipedOutputStream out = new PipedOutputStream();
             PipedInputStream in = new PipedInputStream(out);){
            DockerResultCallback callback = (DockerResultCallback)this.dockerClient.attachContainerCmd(this.containerId).withStdOut(Boolean.valueOf(true)).withStdErr(Boolean.valueOf(true)).withFollowStream(Boolean.valueOf(true)).withStdIn((InputStream)in).exec((ResultCallback)new DockerResultCallback(this.logEvents, this.logger, this.messageHandler));
            callback.awaitStarted(this.attachTimeout.toMillis(), TimeUnit.MILLISECONDS);
            out.write((request + "\n").getBytes());
            out.flush();
            if (id != null) {
                callback.awaitCompletion();
            } else {
                Thread.sleep(100L);
            }
            callback.close();
            if (id == null) {
                future.complete(null);
            }
        }
        catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String dockerHost;
        private String dockerConfig;
        private String dockerContext;
        private String dockerCertPath;
        private Boolean dockerTlsVerify;
        private String registryEmail;
        private String registryUsername;
        private String registryPassword;
        private String registryUrl;
        private String apiVersion;
        private String image;
        private List<String> command;
        private Map<String, String> environment;
        private boolean logEvents;
        private Logger logger;
        private List<String> binds;
        private Duration attachTimeout;

        public Builder dockerHost(String dockerHost) {
            this.dockerHost = dockerHost;
            return this;
        }

        public Builder dockerConfig(String dockerConfig) {
            this.dockerConfig = dockerConfig;
            return this;
        }

        public Builder dockerContext(String dockerContext) {
            this.dockerContext = dockerContext;
            return this;
        }

        public Builder dockerCertPath(String dockerCertPath) {
            this.dockerCertPath = dockerCertPath;
            return this;
        }

        public Builder dockerTlsVerify(Boolean dockerTlsVerify) {
            this.dockerTlsVerify = dockerTlsVerify;
            return this;
        }

        @Deprecated
        public Builder dockerTslVerify(Boolean dockerTlsVerify) {
            return this.dockerTlsVerify(dockerTlsVerify);
        }

        public Builder registryEmail(String registryEmail) {
            this.registryEmail = registryEmail;
            return this;
        }

        public Builder registryUsername(String registryUsername) {
            this.registryUsername = registryUsername;
            return this;
        }

        public Builder registryPassword(String registryPassword) {
            this.registryPassword = registryPassword;
            return this;
        }

        public Builder registryUrl(String registryUrl) {
            this.registryUrl = registryUrl;
            return this;
        }

        public Builder apiVersion(String apiVersion) {
            this.apiVersion = apiVersion;
            return this;
        }

        public Builder image(String image) {
            this.image = image;
            return this;
        }

        public Builder command(List<String> command) {
            this.command = command;
            return this;
        }

        public Builder environment(Map<String, String> environment) {
            this.environment = environment;
            return this;
        }

        public Builder logEvents(boolean logEvents) {
            this.logEvents = logEvents;
            return this;
        }

        public Builder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public Builder binds(List<String> binds) {
            this.binds = binds;
            return this;
        }

        public Builder attachTimeout(Duration attachTimeout) {
            this.attachTimeout = attachTimeout;
            return this;
        }

        public DockerMcpTransport build() {
            if (this.dockerHost == null || this.dockerHost.isEmpty()) {
                throw new IllegalArgumentException("Missing host");
            }
            if (this.image == null || this.image.isEmpty()) {
                throw new IllegalArgumentException("Missing image");
            }
            if (this.command == null) {
                this.command = Collections.emptyList();
            }
            if (this.environment == null) {
                this.environment = Collections.emptyMap();
            }
            if (this.binds == null) {
                this.binds = Collections.emptyList();
            }
            if (this.attachTimeout == null) {
                this.attachTimeout = Duration.ofSeconds(30L);
            }
            return new DockerMcpTransport(this);
        }
    }
}

