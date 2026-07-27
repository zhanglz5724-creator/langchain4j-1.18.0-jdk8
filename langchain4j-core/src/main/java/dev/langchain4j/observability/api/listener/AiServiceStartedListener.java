/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.listener;

import dev.langchain4j.observability.api.event.AiServiceStartedEvent;
import dev.langchain4j.observability.api.listener.AiServiceListener;

@FunctionalInterface
public interface AiServiceStartedListener
extends AiServiceListener<AiServiceStartedEvent> {
    @Override
    default public Class<AiServiceStartedEvent> getEventClass() {
        return AiServiceStartedEvent.class;
    }
}

