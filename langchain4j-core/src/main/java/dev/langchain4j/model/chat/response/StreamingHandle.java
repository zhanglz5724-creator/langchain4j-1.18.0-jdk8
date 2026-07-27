/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.response;

import dev.langchain4j.Experimental;

@Experimental
public interface StreamingHandle {
    public void cancel();

    public boolean isCancelled();
}

