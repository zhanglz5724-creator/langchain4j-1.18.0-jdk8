/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.http.client.sse.DefaultServerSentEventParsingHandle
 *  dev.langchain4j.http.client.sse.ServerSentEvent
 *  dev.langchain4j.http.client.sse.ServerSentEventContext
 *  dev.langchain4j.http.client.sse.ServerSentEventListener
 *  dev.langchain4j.http.client.sse.ServerSentEventListenerUtils
 *  dev.langchain4j.http.client.sse.ServerSentEventParser
 *  dev.langchain4j.http.client.sse.ServerSentEventParsingHandle
 */
package dev.langchain4j.model.ollama;

import dev.langchain4j.Internal;
import dev.langchain4j.http.client.sse.DefaultServerSentEventParsingHandle;
import dev.langchain4j.http.client.sse.ServerSentEvent;
import dev.langchain4j.http.client.sse.ServerSentEventContext;
import dev.langchain4j.http.client.sse.ServerSentEventListener;
import dev.langchain4j.http.client.sse.ServerSentEventListenerUtils;
import dev.langchain4j.http.client.sse.ServerSentEventParser;
import dev.langchain4j.http.client.sse.ServerSentEventParsingHandle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Internal
class OllamaServerSentEventParser
implements ServerSentEventParser {
    OllamaServerSentEventParser() {
    }

    public void parse(InputStream httpResponseBody, ServerSentEventListener listener) {
        DefaultServerSentEventParsingHandle parsingHandle = new DefaultServerSentEventParsingHandle(httpResponseBody);
        ServerSentEventContext context = new ServerSentEventContext((ServerSentEventParsingHandle)parsingHandle);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponseBody, StandardCharsets.UTF_8));){
            String line;
            while (!parsingHandle.isCancelled() && (line = reader.readLine()) != null) {
                ServerSentEvent sse = new ServerSentEvent(null, line);
                ServerSentEventListenerUtils.ignoringExceptions(() -> listener.onEvent(sse, context));
            }
        }
        catch (IOException e) {
            ServerSentEventListenerUtils.ignoringExceptions(() -> listener.onError((Throwable)e));
        }
    }
}

