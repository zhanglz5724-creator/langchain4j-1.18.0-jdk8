/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.http.client.sse;

import dev.langchain4j.Experimental;
import dev.langchain4j.http.client.sse.ServerSentEventParsingHandle;
import dev.langchain4j.internal.ValidationUtils;

@Experimental
public class ServerSentEventContext {
    private final ServerSentEventParsingHandle parsingHandle;

    public ServerSentEventContext(ServerSentEventParsingHandle parsingHandle) {
        this.parsingHandle = (ServerSentEventParsingHandle)ValidationUtils.ensureNotNull((Object)parsingHandle, (String)"parsingHandle");
    }

    public ServerSentEventParsingHandle parsingHandle() {
        return this.parsingHandle;
    }
}

