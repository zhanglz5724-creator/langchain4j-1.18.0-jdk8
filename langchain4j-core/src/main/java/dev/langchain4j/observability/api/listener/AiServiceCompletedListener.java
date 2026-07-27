/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.listener;

import dev.langchain4j.observability.api.event.AiServiceCompletedEvent;
import dev.langchain4j.observability.api.listener.AiServiceListener;

@FunctionalInterface
public interface AiServiceCompletedListener
extends AiServiceListener<AiServiceCompletedEvent> {
    @Override
    default public Class<AiServiceCompletedEvent> getEventClass() {
        return AiServiceCompletedEvent.class;
    }
}

