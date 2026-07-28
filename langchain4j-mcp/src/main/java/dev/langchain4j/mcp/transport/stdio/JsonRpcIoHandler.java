/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  dev.langchain4j.internal.Utils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.mcp.transport.stdio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.mcp.client.logging.McpLoggers;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonRpcIoHandler
implements Runnable,
Closeable {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(JsonRpcIoHandler.class);
    private final InputStream input;
    private final PrintStream out;
    private final boolean logEvents;
    private final Logger trafficLog;
    private final Consumer<JsonNode> messageHandler;
    private volatile boolean closed = false;

    public JsonRpcIoHandler(InputStream input, OutputStream output, Consumer<JsonNode> messageHandler, boolean logEvents) {
        this(input, output, messageHandler, logEvents, null);
    }

    public JsonRpcIoHandler(InputStream input, OutputStream output, Consumer<JsonNode> messageHandler, boolean logEvents, Logger logger) {
        this.input = input;
        this.logEvents = logEvents;
        this.messageHandler = messageHandler;
        this.out = new PrintStream(output, true);
        this.trafficLog = (Logger)Utils.getOrDefault((Object)logger, (Object)McpLoggers.traffic());
    }

    @Override
    public void run() {
        block18: {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.input));){
                String line;
                while ((line = reader.readLine()) != null) {
                    if (this.logEvents) {
                        this.trafficLog.debug("< {}", (Object)line);
                    }
                    try {
                        this.messageHandler.accept(OBJECT_MAPPER.readTree(line));
                    }
                    catch (JsonProcessingException e) {
                        log.warn("Ignoring message received because it is not valid JSON: {}", (Object)line);
                    }
                }
            }
            catch (IOException e) {
                if (this.closed) break block18;
                throw new RuntimeException(e);
            }
        }
        log.debug("JsonRpcIoHandler has finished reading input stream");
    }

    public void submit(String message) throws IOException {
        if (this.logEvents) {
            this.trafficLog.debug("> {}", (Object)message);
        }
        this.out.println(message);
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
        this.out.close();
        this.input.close();
    }
}

