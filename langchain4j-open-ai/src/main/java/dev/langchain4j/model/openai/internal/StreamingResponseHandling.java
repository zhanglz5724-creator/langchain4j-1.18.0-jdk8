/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.openai.internal;

import dev.langchain4j.model.openai.internal.AsyncResponseHandling;
import dev.langchain4j.model.openai.internal.StreamingCompletionHandling;

public interface StreamingResponseHandling
extends AsyncResponseHandling {
    public StreamingCompletionHandling onComplete(Runnable var1);
}

