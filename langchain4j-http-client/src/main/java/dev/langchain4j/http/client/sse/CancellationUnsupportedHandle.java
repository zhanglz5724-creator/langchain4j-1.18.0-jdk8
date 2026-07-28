/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.exception.UnsupportedFeatureException
 */
package dev.langchain4j.http.client.sse;

import dev.langchain4j.Internal;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.http.client.sse.ServerSentEventParsingHandle;

@Internal
public class CancellationUnsupportedHandle
implements ServerSentEventParsingHandle {
    @Override
    public void cancel() {
        throw new UnsupportedFeatureException("Streaming cancellation is not supported when calling ServerSentEventListener.onEvent(ServerSentEvent). Please call ServerSentEventListener.onEvent(ServerSentEvent, ServerSentEventContext) instead.");
    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}

