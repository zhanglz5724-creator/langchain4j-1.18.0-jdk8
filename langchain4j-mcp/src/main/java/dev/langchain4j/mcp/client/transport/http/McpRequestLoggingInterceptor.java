/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  okhttp3.Headers
 *  okhttp3.Interceptor
 *  okhttp3.Interceptor$Chain
 *  okhttp3.Request
 *  okhttp3.Response
 *  okio.Buffer
 *  okio.BufferedSink
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.mcp.client.transport.http;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.mcp.client.logging.McpLoggers;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class McpRequestLoggingInterceptor
implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(McpRequestLoggingInterceptor.class);
    private final Logger trafficLog;

    McpRequestLoggingInterceptor(Logger logger) {
        this.trafficLog = (Logger)Utils.getOrDefault((Object)logger, (Object)McpLoggers.traffic());
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        this.log(request);
        return chain.proceed(request);
    }

    private void log(Request request) {
        try {
            this.trafficLog.debug("Request:\n- method: {}\n- url: {}\n- headers: {}\n- body: {}", new Object[]{request.method(), request.url(), McpRequestLoggingInterceptor.getHeaders(request.headers()), McpRequestLoggingInterceptor.getBody(request)});
        }
        catch (Exception e) {
            log.warn("Error while logging request: {}", (Object)e.getMessage());
        }
    }

    private static String getBody(Request request) {
        try {
            Buffer buffer = new Buffer();
            if (request.body() == null) {
                return "";
            }
            request.body().writeTo((BufferedSink)buffer);
            return buffer.readUtf8();
        }
        catch (Exception e) {
            log.warn("Exception while getting body", (Throwable)e);
            return "Exception while getting body: " + e.getMessage();
        }
    }

    static String getHeaders(Headers headers) {
        return StreamSupport.stream(headers.spliterator(), false).map(header -> {
            String headerKey = (String)header.component1();
            String headerValue = (String)header.component2();
            return String.format("[%s: %s]", headerKey, headerValue);
        }).collect(Collectors.joining(", "));
    }
}

