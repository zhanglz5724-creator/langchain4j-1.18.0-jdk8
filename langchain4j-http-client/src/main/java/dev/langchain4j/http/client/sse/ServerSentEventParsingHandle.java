/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 */
package dev.langchain4j.http.client.sse;

import dev.langchain4j.Experimental;

@Experimental
public interface ServerSentEventParsingHandle {
    public void cancel();

    public boolean isCancelled();
}

