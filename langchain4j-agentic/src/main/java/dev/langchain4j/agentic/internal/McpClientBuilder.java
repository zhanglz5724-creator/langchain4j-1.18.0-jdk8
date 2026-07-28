/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.observability.AgentListener;

public interface McpClientBuilder<T> {
    public McpClientBuilder<T> toolName(String var1);

    public McpClientBuilder<T> inputKeys(String ... var1);

    public McpClientBuilder<T> outputKey(String var1);

    public McpClientBuilder<T> async(boolean var1);

    public McpClientBuilder<T> listener(AgentListener var1);

    public T build();
}

