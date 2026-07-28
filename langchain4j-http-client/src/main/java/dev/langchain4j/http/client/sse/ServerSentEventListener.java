/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 */
package dev.langchain4j.http.client.sse;

import dev.langchain4j.Experimental;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.sse.ServerSentEvent;
import dev.langchain4j.http.client.sse.ServerSentEventContext;

public interface ServerSentEventListener {
    default public void onOpen(SuccessfulHttpResponse response) {
    }

    @Experimental
    default public void onEvent(ServerSentEvent event, ServerSentEventContext context) {
        this.onEvent(event);
    }

    default public void onEvent(ServerSentEvent event) {
    }

    public void onError(Throwable var1);

    default public void onClose() {
    }
}

