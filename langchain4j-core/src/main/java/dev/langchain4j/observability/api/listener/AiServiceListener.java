/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.listener;

import dev.langchain4j.observability.api.event.AiServiceEvent;

public interface AiServiceListener<T extends AiServiceEvent> {
    public Class<T> getEventClass();

    public void onEvent(T var1);
}

