/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.guardrail.InputGuardrailResult
 *  dev.langchain4j.guardrail.InputGuardrailResult$Failure
 */
package dev.langchain4j.test.guardrail;

import dev.langchain4j.guardrail.InputGuardrailResult;
import dev.langchain4j.test.guardrail.GuardrailResultAssert;

public final class InputGuardrailResultAssert
extends GuardrailResultAssert<InputGuardrailResultAssert, InputGuardrailResult, InputGuardrailResult.Failure> {
    private InputGuardrailResultAssert(InputGuardrailResult inputGuardrailResult) {
        super(inputGuardrailResult, InputGuardrailResultAssert.class, InputGuardrailResult.Failure.class);
    }

    public static InputGuardrailResultAssert assertThat(InputGuardrailResult actual) {
        return new InputGuardrailResultAssert(actual);
    }
}

