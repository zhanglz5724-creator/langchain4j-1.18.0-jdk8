/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.listener;

import dev.langchain4j.observability.api.event.AiServiceErrorEvent;
import dev.langchain4j.observability.api.listener.AiServiceListener;

@FunctionalInterface
public interface AiServiceErrorListener
extends AiServiceListener<AiServiceErrorEvent> {
    @Override
    default public Class<AiServiceErrorEvent> getEventClass() {
        return AiServiceErrorEvent.class;
    }
}

