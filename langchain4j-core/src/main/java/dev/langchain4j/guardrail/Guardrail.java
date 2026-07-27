/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.guardrail.GuardrailRequest;
import dev.langchain4j.guardrail.GuardrailResult;

public interface Guardrail<P extends GuardrailRequest, R extends GuardrailResult<R>> {
    default public String name() {
        return this.getClass().getSimpleName();
    }

    public R validate(P var1);
}

