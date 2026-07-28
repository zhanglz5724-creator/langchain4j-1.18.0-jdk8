/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.chat.response.StreamingHandle
 */
package dev.langchain4j.model.vertexai.gemini;

import dev.langchain4j.model.chat.response.StreamingHandle;

class VertexAiGeminiStreamingHandle
implements StreamingHandle {
    private volatile boolean isCancelled;

    VertexAiGeminiStreamingHandle() {
    }

    public void cancel() {
        this.isCancelled = true;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }
}

