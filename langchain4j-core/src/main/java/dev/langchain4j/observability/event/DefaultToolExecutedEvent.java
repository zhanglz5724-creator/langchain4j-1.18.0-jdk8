/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.event;

import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.observability.api.event.ToolExecutedEvent;
import dev.langchain4j.observability.event.AbstractAiServiceEvent;
import java.util.Collections;
import java.util.List;

public class DefaultToolExecutedEvent
extends AbstractAiServiceEvent
implements ToolExecutedEvent {
    private final ToolExecutionRequest request;
    private final List<Content> resultContents;

    public DefaultToolExecutedEvent(ToolExecutedEvent.ToolExecutedEventBuilder builder) {
        super(builder);
        boolean hasResultContents;
        this.request = ValidationUtils.ensureNotNull(builder.request(), "request");
        boolean hasResultText = builder.resultText() != null;
        boolean bl = hasResultContents = builder.resultContents() != null && !builder.resultContents().isEmpty();
        if (hasResultText && hasResultContents) {
            throw new IllegalArgumentException("resultText and resultContents are mutually exclusive");
        }
        if (hasResultText) {
            this.resultContents = Collections.singletonList(TextContent.from(builder.resultText()));
        } else if (hasResultContents) {
            this.resultContents = Utils.copy(builder.resultContents());
        } else {
            throw new IllegalArgumentException("Either resultText or resultContents must be provided");
        }
    }

    @Override
    public ToolExecutionRequest request() {
        return this.request;
    }

    @Override
    public String resultText() {
        if (this.resultContents.size() == 1 && this.resultContents.get(0) instanceof TextContent) {
            TextContent textContent = (TextContent)this.resultContents.get(0);
            return textContent.text();
        }
        throw new IllegalStateException("resultText() cannot be called when resultContents contains non-text or multiple content elements. Use resultContents() instead.");
    }

    @Override
    @Experimental
    public List<Content> resultContents() {
        return this.resultContents;
    }
}

