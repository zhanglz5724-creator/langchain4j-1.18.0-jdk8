/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.Internal;
import dev.langchain4j.guardrail.Guardrail;
import dev.langchain4j.guardrail.GuardrailException;
import dev.langchain4j.guardrail.GuardrailExecutor;
import dev.langchain4j.guardrail.GuardrailRequest;
import dev.langchain4j.guardrail.GuardrailResult;
import dev.langchain4j.guardrail.config.GuardrailsConfig;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.observability.api.event.GuardrailExecutedEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Internal
public abstract class AbstractGuardrailExecutor<C extends GuardrailsConfig, P extends GuardrailRequest<P>, R extends GuardrailResult<R>, G extends Guardrail<P, R>, E extends GuardrailExecutedEvent<P, R, G>, F extends GuardrailResult.Failure>
implements GuardrailExecutor<C, P, R, G, E> {
    private final C config;
    private final List<G> guardrails;

    protected AbstractGuardrailExecutor(C config, List<G> guardrails) {
        ValidationUtils.ensureNotNull(config, "config");
        this.config = config;
        this.guardrails = Optional.ofNullable(guardrails).orElseGet(Collections::emptyList);
    }

    protected abstract R createFailure(List<F> var1);

    protected abstract R createSuccess();

    protected abstract GuardrailException createGuardrailException(String var1, Throwable var2);

    protected abstract GuardrailExecutedEvent.GuardrailExecutedEventBuilder<P, R, G, E> createEmptyObservabilityEventBuilderInstance();

    @Override
    public C config() {
        return this.config;
    }

    @Override
    public List<G> guardrails() {
        return this.guardrails;
    }

    protected R validate(P request, G guardrail) {
        ValidationUtils.ensureNotNull(request, "request");
        ValidationUtils.ensureNotNull(guardrail, "guardrail");
        try {
            return (R)guardrail.validate(request).validatedBy(guardrail.getClass());
        }
        catch (Exception e) {
            throw this.createGuardrailException(e.getMessage(), e);
        }
    }

    protected R handleFatalResult(R accumulatedResult, R result) {
        return result;
    }

    protected void fireObservabilityEvent(InvocationContext invocationContext, P request, R result, G guardrail, Duration duration) {
        request.requestParams().aiservicelistenerregistrar().fireEvent(((GuardrailExecutedEvent.GuardrailExecutedEventBuilder)this.createEmptyObservabilityEventBuilderInstance().invocationContext(invocationContext)).request(request).result(result).guardrailClass(guardrail.getClass()).guardrailName(guardrail.name()).duration(duration).build());
    }

    protected R executeGuardrails(P request) {
        ValidationUtils.ensureNotNull(request, "request");
        P accumulatedRequest = request;
        R accumulatedResult = this.createSuccess();
        for (Guardrail guardrail : this.guardrails) {
            if (guardrail == null) continue;
            long before = System.nanoTime();
            R result = this.validate(accumulatedRequest, (G) guardrail);
            long after = System.nanoTime();
            Duration duration = Duration.ofNanos(after - before);
            this.fireObservabilityEvent(request.requestParams().invocationContext(), accumulatedRequest, result, (G) guardrail, duration);
            if (result.isFatal()) {
                return this.handleFatalResult(accumulatedResult, result);
            }
            if (result.hasRewrittenResult()) {
                accumulatedRequest = accumulatedRequest.withText(result.successfulText());
            }
            accumulatedResult = this.composeResult(accumulatedResult, result);
        }
        return accumulatedResult;
    }

    protected R composeResult(R oldResult, R newResult) {
        if (oldResult.isSuccess()) {
            return newResult;
        }
        if (newResult.isSuccess()) {
            return oldResult;
        }
        ArrayList<F> failures = new ArrayList<>(oldResult.failures());
        failures.addAll(newResult.failures());
        return this.createFailure(failures);
    }

    public static abstract class GuardrailExecutorBuilder<C extends GuardrailsConfig, R extends GuardrailResult<R>, P extends GuardrailRequest<P>, G extends Guardrail<P, R>, E extends GuardrailExecutedEvent<P, R, G>, B extends GuardrailExecutorBuilder<C, R, P, G, E, B>> {
        private final C defaultConfig;
        private C config;
        private List<G> guardrails = new ArrayList<G>();

        protected GuardrailExecutorBuilder(C defaultConfig) {
            this.defaultConfig = (C) ValidationUtils.ensureNotNull(defaultConfig, "defaultConfig");
        }

        public abstract GuardrailExecutor<C, P, R, G, E> build();

        protected C config() {
            return this.config != null ? this.config : this.defaultConfig;
        }

        protected List<G> guardrails() {
            return this.guardrails;
        }

        public B config(C config) {
            this.config = config;
            return (B)this;
        }

        public B guardrails(List<G> guardrails) {
            this.guardrails.clear();
            if (guardrails != null) {
                this.guardrails.addAll(guardrails);
            }
            return (B)this;
        }

        public B guardrails(G ... guardrails) {
            Optional.ofNullable(guardrails).map(Arrays::asList).ifPresent(this::guardrails);
            return (B)this;
        }
    }
}

