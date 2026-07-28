/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.exception.HttpException
 */
package dev.langchain4j.http.client;

import dev.langchain4j.exception.HttpException;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.sse.DefaultServerSentEventParser;
import dev.langchain4j.http.client.sse.ServerSentEventListener;
import dev.langchain4j.http.client.sse.ServerSentEventParser;

public interface HttpClient {
    public SuccessfulHttpResponse execute(HttpRequest var1) throws HttpException, RuntimeException;

    default public void execute(HttpRequest request, ServerSentEventListener listener) {
        this.execute(request, new DefaultServerSentEventParser(), listener);
    }

    public void execute(HttpRequest var1, ServerSentEventParser var2, ServerSentEventListener var3);
}

