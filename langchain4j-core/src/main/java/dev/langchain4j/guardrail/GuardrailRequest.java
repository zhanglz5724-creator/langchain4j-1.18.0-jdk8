/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.guardrail.GuardrailRequestParams;

public interface GuardrailRequest<P extends GuardrailRequest<P>> {
    public GuardrailRequestParams requestParams();

    public P withText(String var1);
}

