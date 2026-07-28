/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.http.client.sse;

import dev.langchain4j.http.client.sse.ServerSentEventParsingHandle;
import dev.langchain4j.internal.ValidationUtils;
import java.io.InputStream;

public class DefaultServerSentEventParsingHandle
implements ServerSentEventParsingHandle {
    private final InputStream inputStream;
    private volatile boolean isCancelled;

    public DefaultServerSentEventParsingHandle(InputStream inputStream) {
        this.inputStream = (InputStream)ValidationUtils.ensureNotNull((Object)inputStream, (String)"inputStream");
    }

    @Override
    public void cancel() {
        this.isCancelled = true;
        try {
            this.inputStream.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }
}

