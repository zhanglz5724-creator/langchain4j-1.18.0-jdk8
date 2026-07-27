/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.event;

import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.observability.api.event.AiServiceEvent;
import dev.langchain4j.observability.event.DefaultToolExecutedEvent;
import java.util.List;

public interface ToolExecutedEvent
extends AiServiceEvent {
    public ToolExecutionRequest request();

    public String resultText();

    @Experimental
    public List<Content> resultContents();

    public static ToolExecutedEventBuilder builder() {
        return new ToolExecutedEventBuilder();
    }

    default public Class<ToolExecutedEvent> eventClass() {
        return ToolExecutedEvent.class;
    }

    default public ToolExecutedEventBuilder toBuilder() {
        return new ToolExecutedEventBuilder(this);
    }

    public static class ToolExecutedEventBuilder
    extends AiServiceEvent.Builder<ToolExecutedEvent> {
        private ToolExecutionRequest request;
        private String resultText;
        private List<Content> resultContents;

        protected ToolExecutedEventBuilder() {
        }

        protected ToolExecutedEventBuilder(ToolExecutedEvent src) {
            super(src);
            this.request(src.request());
            this.resultText(src.resultText());
            this.resultContents(src.resultContents());
        }

        public ToolExecutedEventBuilder request(ToolExecutionRequest request) {
            this.request = request;
            return this;
        }

        public ToolExecutedEventBuilder resultText(String resultText) {
            this.resultText = resultText;
            return this;
        }

        @Experimental
        public ToolExecutedEventBuilder resultContents(List<Content> resultContents) {
            this.resultContents = resultContents;
            return this;
        }

        public ToolExecutionRequest request() {
            return this.request;
        }

        public String resultText() {
            return this.resultText;
        }

        public List<Content> resultContents() {
            return this.resultContents;
        }

        public ToolExecutedEventBuilder invocationContext(InvocationContext invocationContext) {
            return (ToolExecutedEventBuilder)super.invocationContext(invocationContext);
        }

        @Override
        public ToolExecutedEvent build() {
            return new DefaultToolExecutedEvent(this);
        }
    }
}

