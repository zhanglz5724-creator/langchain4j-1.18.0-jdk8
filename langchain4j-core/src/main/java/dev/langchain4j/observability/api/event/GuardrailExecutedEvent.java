/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api.event;

import dev.langchain4j.guardrail.Guardrail;
import dev.langchain4j.guardrail.GuardrailRequest;
import dev.langchain4j.guardrail.GuardrailResult;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.observability.api.event.AiServiceEvent;
import java.time.Duration;

public interface GuardrailExecutedEvent<P extends GuardrailRequest<P>, R extends GuardrailResult<R>, G extends Guardrail<P, R>>
extends AiServiceEvent {
    public P request();

    public R result();

    public Class<G> guardrailClass();

    default public String guardrailName() {
        return this.guardrailClass().getSimpleName();
    }

    public Duration duration();

    public static abstract class GuardrailExecutedEventBuilder<P extends GuardrailRequest<P>, R extends GuardrailResult<R>, G extends Guardrail<P, R>, T extends GuardrailExecutedEvent<P, R, G>>
    extends AiServiceEvent.Builder<T> {
        private P request;
        private R result;
        private Class<G> guardrailClass;
        private String guardrailName;
        private Duration duration;

        protected GuardrailExecutedEventBuilder() {
        }

        protected GuardrailExecutedEventBuilder(T src) {
            super(src);
            this.request(src.request());
            this.result(src.result());
            this.guardrailClass(src.guardrailClass());
            this.guardrailName(src.guardrailName());
            this.duration(src.duration());
        }

        public Class<G> guardrailClass() {
            return this.guardrailClass;
        }

        public P request() {
            return this.request;
        }

        public String guardrailName() {
            return this.guardrailName;
        }

        public R result() {
            return this.result;
        }

        public Duration duration() {
            return this.duration;
        }

        public GuardrailExecutedEventBuilder<P, R, G, T> request(P request) {
            this.request = request;
            return this;
        }

        public GuardrailExecutedEventBuilder<P, R, G, T> result(R result) {
            this.result = result;
            return this;
        }

        public GuardrailExecutedEventBuilder<P, R, G, T> invocationContext(InvocationContext invocationContext) {
            return (GuardrailExecutedEventBuilder)super.invocationContext(invocationContext);
        }

        public <C extends G> GuardrailExecutedEventBuilder<P, R, G, T> guardrailClass(Class<C> guardrailClass) {
            this.guardrailClass = (Class<G>) guardrailClass;
            return this;
        }

        public GuardrailExecutedEventBuilder<P, R, G, T> guardrailName(String guardrailName) {
            this.guardrailName = guardrailName;
            return this;
        }

        public GuardrailExecutedEventBuilder<P, R, G, T> duration(Duration duration) {
            this.duration = duration;
            return this;
        }
    }
}

