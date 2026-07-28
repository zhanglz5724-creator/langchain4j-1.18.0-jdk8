/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.model.chat.response.StreamingHandle
 */
package dev.langchain4j.http.client.sse;

import dev.langchain4j.Internal;
import dev.langchain4j.http.client.sse.ServerSentEventParsingHandle;
import dev.langchain4j.model.chat.response.StreamingHandle;

@Internal
public class ServerSentEventParsingHandleUtils {
    public static StreamingHandle toStreamingHandle(final ServerSentEventParsingHandle parsingHandle) {
        return new StreamingHandle(){

            public void cancel() {
                parsingHandle.cancel();
            }

            public boolean isCancelled() {
                return parsingHandle.isCancelled();
            }
        };
    }
}

