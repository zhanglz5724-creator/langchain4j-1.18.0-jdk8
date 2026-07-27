/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.listener;

import dev.langchain4j.observability.api.event.AiServiceRequestIssuedEvent;
import dev.langchain4j.observability.api.listener.AiServiceListener;

@FunctionalInterface
public interface AiServiceRequestIssuedListener
extends AiServiceListener<AiServiceRequestIssuedEvent> {
    @Override
    default public Class<AiServiceRequestIssuedEvent> getEventClass() {
        return AiServiceRequestIssuedEvent.class;
    }
}

