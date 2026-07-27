/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.guardrail.Guardrail;
import dev.langchain4j.guardrail.GuardrailRequest;
import dev.langchain4j.guardrail.GuardrailResult;
import dev.langchain4j.guardrail.config.GuardrailsConfig;
import dev.langchain4j.observability.api.event.GuardrailExecutedEvent;
import java.util.List;

public interface GuardrailExecutor<C extends GuardrailsConfig, P extends GuardrailRequest<P>, R extends GuardrailResult<R>, G extends Guardrail<P, R>, E extends GuardrailExecutedEvent<P, R, G>> {
    public C config();

    public List<G> guardrails();

    public R execute(P var1);
}

