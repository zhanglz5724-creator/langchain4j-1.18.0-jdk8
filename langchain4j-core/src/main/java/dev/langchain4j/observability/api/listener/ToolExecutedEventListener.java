/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.listener;

import dev.langchain4j.observability.api.event.ToolExecutedEvent;
import dev.langchain4j.observability.api.listener.AiServiceListener;

@FunctionalInterface
public interface ToolExecutedEventListener
extends AiServiceListener<ToolExecutedEvent> {
    @Override
    default public Class<ToolExecutedEvent> getEventClass() {
        return ToolExecutedEvent.class;
    }
}

