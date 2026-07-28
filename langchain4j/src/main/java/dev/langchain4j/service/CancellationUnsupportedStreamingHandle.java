/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.model.chat.response.StreamingHandle
 */
package dev.langchain4j.service;

import dev.langchain4j.Internal;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.model.chat.response.StreamingHandle;

@Internal
class CancellationUnsupportedStreamingHandle
implements StreamingHandle {
    CancellationUnsupportedStreamingHandle() {
    }

    public void cancel() {
        throw new UnsupportedFeatureException("Streaming cancellation is not supported by this StreamingChatModel implementation. It should invoke StreamingChatResponseHandler.onPartialResponse(PartialResponse, PartialResponseContext) instead of StreamingChatResponseHandler.onPartialResponse(String).");
    }

    public boolean isCancelled() {
        return false;
    }
}

