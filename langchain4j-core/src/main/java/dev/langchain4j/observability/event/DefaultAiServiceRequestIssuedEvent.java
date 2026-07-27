/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.event;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.observability.api.event.AiServiceRequestIssuedEvent;
import dev.langchain4j.observability.event.AbstractAiServiceEvent;

public class DefaultAiServiceRequestIssuedEvent
extends AbstractAiServiceEvent
implements AiServiceRequestIssuedEvent {
    private final ChatRequest request;

    public DefaultAiServiceRequestIssuedEvent(AiServiceRequestIssuedEvent.AiServiceRequestIssuedEventBuilder builder) {
        super(builder);
        this.request = ValidationUtils.ensureNotNull(builder.request(), "request");
    }

    @Override
    public ChatRequest request() {
        return this.request;
    }
}

