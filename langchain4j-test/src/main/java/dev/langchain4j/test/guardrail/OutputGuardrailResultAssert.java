/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.guardrail.OutputGuardrailResult
 *  dev.langchain4j.guardrail.OutputGuardrailResult$Failure
 *  org.assertj.core.api.ObjectAssert
 */
package dev.langchain4j.test.guardrail;

import dev.langchain4j.guardrail.OutputGuardrailResult;
import dev.langchain4j.test.guardrail.GuardrailResultAssert;
import java.util.function.Function;
import org.assertj.core.api.ObjectAssert;

public final class OutputGuardrailResultAssert
extends GuardrailResultAssert<OutputGuardrailResultAssert, OutputGuardrailResult, OutputGuardrailResult.Failure> {
    private OutputGuardrailResultAssert(OutputGuardrailResult outputGuardrailResult) {
        super(outputGuardrailResult, OutputGuardrailResultAssert.class, OutputGuardrailResult.Failure.class);
    }

    public static OutputGuardrailResultAssert assertThat(OutputGuardrailResult actual) {
        return new OutputGuardrailResultAssert(actual);
    }

    public OutputGuardrailResultAssert hasSingleFailureWithMessageAndReprompt(String expectedFailureMessage, String expectedReprompt) {
        this.isNotNull();
        ((ObjectAssert)this.withFailures().singleElement()).extracting("message", "retry", "reprompt").containsExactly(new Object[]{expectedFailureMessage, true, expectedReprompt});
        return this;
    }
}

