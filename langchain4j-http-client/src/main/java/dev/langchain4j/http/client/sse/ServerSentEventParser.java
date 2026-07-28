/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.http.client.sse;

import dev.langchain4j.http.client.sse.ServerSentEventListener;
import java.io.InputStream;

public interface ServerSentEventParser {
    public void parse(InputStream var1, ServerSentEventListener var2);
}

