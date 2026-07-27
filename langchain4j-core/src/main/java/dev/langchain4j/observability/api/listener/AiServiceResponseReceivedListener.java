/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.listener;

import dev.langchain4j.observability.api.event.AiServiceResponseReceivedEvent;
import dev.langchain4j.observability.api.listener.AiServiceListener;

@FunctionalInterface
public interface AiServiceResponseReceivedListener
extends AiServiceListener<AiServiceResponseReceivedEvent> {
    @Override
    default public Class<AiServiceResponseReceivedEvent> getEventClass() {
        return AiServiceResponseReceivedEvent.class;
    }
}

