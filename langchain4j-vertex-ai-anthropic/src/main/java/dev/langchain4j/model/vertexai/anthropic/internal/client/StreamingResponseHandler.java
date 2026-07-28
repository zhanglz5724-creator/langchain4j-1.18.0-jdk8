/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.vertexai.anthropic.internal.client;

import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicResponse;

public interface StreamingResponseHandler {
    default public void onResponse(AnthropicResponse response) {
    }

    public void onChunk(String var1);

    public void onComplete();

    public void onError(Throwable var1);
}

