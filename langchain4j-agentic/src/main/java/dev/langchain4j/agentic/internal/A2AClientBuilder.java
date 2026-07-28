/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.observability.AgentListener;
import java.util.function.Consumer;

public interface A2AClientBuilder<T> {
    public A2AClientBuilder<T> inputKeys(String ... var1);

    public A2AClientBuilder<T> outputKey(String var1);

    public A2AClientBuilder<T> async(boolean var1);

    public A2AClientBuilder<T> listener(AgentListener var1);

    public A2AClientBuilder<T> clientCustomizer(Consumer<?> var1);

    public T build();
}

