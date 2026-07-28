/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.github.dockerjava.api.async.ResultCallback$Adapter
 *  com.github.dockerjava.api.model.Frame
 *  com.github.dockerjava.api.model.StreamType
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.mcp.client.transport.McpOperationHandler
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.mcp.client.transport.docker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.mcp.client.transport.McpOperationHandler;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DockerResultCallback
extends ResultCallback.Adapter<Frame> {
    private static final Logger LOG = LoggerFactory.getLogger(DockerResultCallback.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger DEFAULT_TRAFFIC_LOG = LoggerFactory.getLogger((String)"MCP");
    private static final Pattern NEWLINE_PATTERN = Pattern.compile("([^\\r\\n]+)[\\r\\n]+");
    private final boolean logEvents;
    private final Logger trafficLog;
    private final McpOperationHandler messageHandler;
    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private final StringBuilder logAggregator = new StringBuilder();

    public DockerResultCallback(boolean logEvents, McpOperationHandler messageHandler) {
        this(logEvents, null, messageHandler);
    }

    public DockerResultCallback(boolean logEvents, Logger logger, McpOperationHandler messageHandler) {
        this.logEvents = logEvents;
        this.messageHandler = messageHandler;
        this.trafficLog = (Logger)Utils.getOrDefault((Object)logger, (Object)DEFAULT_TRAFFIC_LOG);
    }

    public void onNext(Frame frame) {
        String frameStr = new String(frame.getPayload());
        if (frame.getStreamType() == StreamType.STDERR) {
            LOG.debug("[STDERR] {}", (Object)frameStr);
        } else if (frame.getStreamType() == StreamType.STDOUT) {
            this.send(frameStr);
        }
    }

    public ResultCallback.Adapter<Frame> awaitCompletion() throws InterruptedException {
        this.countDownLatch.await();
        return this;
    }

    public boolean awaitCompletion(long timeout, TimeUnit timeUnit) throws InterruptedException {
        return this.countDownLatch.await(timeout, timeUnit);
    }

    private void send(String line) {
        if (line != null && !line.trim().isEmpty()) {
            this.logAggregator.append(line);
            if (NEWLINE_PATTERN.matcher(line).matches()) {
                this.innerSend();
            }
        } else if (this.logAggregator.length() > 0) {
            this.innerSend();
        }
    }

    private void innerSend() {
        String message = this.logAggregator.toString();
        if (this.logEvents) {
            this.trafficLog.debug("< {}", (Object)message);
        }
        try {
            this.messageHandler.handle(OBJECT_MAPPER.readTree(message));
            this.logAggregator.setLength(0);
            this.countDownLatch.countDown();
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

