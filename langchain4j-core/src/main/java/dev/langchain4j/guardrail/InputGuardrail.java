/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.Guardrail;
import dev.langchain4j.guardrail.InputGuardrailRequest;
import dev.langchain4j.guardrail.InputGuardrailResult;
import dev.langchain4j.internal.ValidationUtils;

public interface InputGuardrail
extends Guardrail<InputGuardrailRequest, InputGuardrailResult> {
    default public InputGuardrailResult validate(UserMessage userMessage) {
        return this.failure("Validation not implemented");
    }

    @Override
    default public InputGuardrailResult validate(InputGuardrailRequest request) {
        ValidationUtils.ensureNotNull(request, "params");
        return this.validate(request.userMessage());
    }

    default public InputGuardrailResult success() {
        return InputGuardrailResult.success();
    }

    default public InputGuardrailResult successWith(String successfulText) {
        return InputGuardrailResult.successWith(successfulText);
    }

    default public InputGuardrailResult failure(String message) {
        return new InputGuardrailResult(new InputGuardrailResult.Failure(message), false);
    }

    default public InputGuardrailResult failure(String message, Throwable cause) {
        return new InputGuardrailResult(new InputGuardrailResult.Failure(message, cause), false);
    }

    default public InputGuardrailResult fatal(String message) {
        return new InputGuardrailResult(new InputGuardrailResult.Failure(message), true);
    }

    default public InputGuardrailResult fatal(String message, Throwable cause) {
        return new InputGuardrailResult(new InputGuardrailResult.Failure(message, cause), true);
    }
}

