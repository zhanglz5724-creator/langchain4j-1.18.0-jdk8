/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.event;

import dev.langchain4j.invocation.InvocationContext;

public interface AiServiceEvent {
    public InvocationContext invocationContext();

    public <T extends AiServiceEvent> Class<T> eventClass();

    public <T extends AiServiceEvent> Builder<T> toBuilder();

    public static abstract class Builder<T extends AiServiceEvent> {
        private InvocationContext invocationContext;

        protected Builder() {
        }

        protected Builder(T src) {
            this.invocationContext = src.invocationContext();
        }

        public InvocationContext invocationContext() {
            return this.invocationContext;
        }

        public Builder<T> invocationContext(InvocationContext invocationContext) {
            this.invocationContext = invocationContext;
            return this;
        }

        public abstract T build();
    }
}

