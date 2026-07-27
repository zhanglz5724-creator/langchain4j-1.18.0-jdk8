/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.event;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.observability.api.event.AiServiceResponseReceivedEvent;
import dev.langchain4j.observability.event.AbstractAiServiceEvent;

public class DefaultAiServiceResponseReceivedEvent
extends AbstractAiServiceEvent
implements AiServiceResponseReceivedEvent {
    private final ChatResponse response;
    private final ChatRequest request;

    public DefaultAiServiceResponseReceivedEvent(AiServiceResponseReceivedEvent.AiServiceResponseReceivedEventBuilder builder) {
        super(builder);
        this.response = ValidationUtils.ensureNotNull(builder.response(), "response");
        this.request = ValidationUtils.ensureNotNull(builder.request(), "request");
    }

    @Override
    public ChatResponse response() {
        return this.response;
    }

    @Override
    public ChatRequest request() {
        return this.request;
    }
}

