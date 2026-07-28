/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.http.client.sse;

import dev.langchain4j.http.client.sse.DefaultServerSentEventParsingHandle;
import dev.langchain4j.http.client.sse.ServerSentEvent;
import dev.langchain4j.http.client.sse.ServerSentEventContext;
import dev.langchain4j.http.client.sse.ServerSentEventListener;
import dev.langchain4j.http.client.sse.ServerSentEventListenerUtils;
import dev.langchain4j.http.client.sse.ServerSentEventParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class DefaultServerSentEventParser
implements ServerSentEventParser {
    @Override
    public void parse(InputStream httpResponseBody, ServerSentEventListener listener) {
        DefaultServerSentEventParsingHandle parsingHandle = new DefaultServerSentEventParsingHandle(httpResponseBody);
        ServerSentEventContext context = new ServerSentEventContext(parsingHandle);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponseBody, StandardCharsets.UTF_8));){
            ServerSentEvent sse;
            String line;
            String event = null;
            StringBuilder data = new StringBuilder();
            while (!parsingHandle.isCancelled() && (line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    if (data.length() == 0) continue;
                    sse = new ServerSentEvent(event, data.toString());
                    ServerSentEventListenerUtils.ignoringExceptions(() -> listener.onEvent(sse, context));
                    event = null;
                    data.setLength(0);
                    continue;
                }
                if (line.startsWith("event:")) {
                    event = line.substring("event:".length()).trim();
                    continue;
                }
                if (!line.startsWith("data:")) continue;
                String content = line.substring("data:".length());
                if (data.length() != 0) {
                    data.append("\n");
                }
                data.append(content.trim());
            }
            if (!parsingHandle.isCancelled() && data.length() != 0) {
                sse = new ServerSentEvent(event, data.toString());
                ServerSentEventListenerUtils.ignoringExceptions(() -> listener.onEvent(sse, context));
            }
        }
        catch (IOException e) {
            ServerSentEventListenerUtils.ignoringExceptions(() -> listener.onError(e));
        }
    }
}

