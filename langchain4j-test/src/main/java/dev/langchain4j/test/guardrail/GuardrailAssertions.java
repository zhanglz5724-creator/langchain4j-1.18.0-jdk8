/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.guardrail.InputGuardrailResult
 *  dev.langchain4j.guardrail.OutputGuardrailResult
 *  org.assertj.core.api.Assertions
 */
package dev.langchain4j.test.guardrail;

import dev.langchain4j.guardrail.InputGuardrailResult;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import dev.langchain4j.test.guardrail.InputGuardrailResultAssert;
import dev.langchain4j.test.guardrail.OutputGuardrailResultAssert;
import org.assertj.core.api.Assertions;

public class GuardrailAssertions
extends Assertions {
    public static OutputGuardrailResultAssert assertThat(OutputGuardrailResult actual) {
        return OutputGuardrailResultAssert.assertThat(actual);
    }

    public static InputGuardrailResultAssert assertThat(InputGuardrailResult actual) {
        return InputGuardrailResultAssert.assertThat(actual);
    }
}

