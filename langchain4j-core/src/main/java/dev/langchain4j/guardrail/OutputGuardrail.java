/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.Guardrail;
import dev.langchain4j.guardrail.OutputGuardrailRequest;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import java.util.Arrays;

public interface OutputGuardrail
extends Guardrail<OutputGuardrailRequest, OutputGuardrailResult> {
    default public OutputGuardrailResult validate(AiMessage responseFromLLM) {
        return this.failure("Validation not implemented");
    }

    @Override
    default public OutputGuardrailResult validate(OutputGuardrailRequest request) {
        return this.validate(request.responseFromLLM().aiMessage());
    }

    default public OutputGuardrailResult success() {
        return OutputGuardrailResult.success();
    }

    default public OutputGuardrailResult successWith(String successfulText) {
        return OutputGuardrailResult.successWith(successfulText);
    }

    default public OutputGuardrailResult successWith(String successfulText, Object successfulResult) {
        return OutputGuardrailResult.successWith(successfulText, successfulResult);
    }

    default public OutputGuardrailResult successWith(AiMessage successfulAiMessage) {
        return OutputGuardrailResult.successWith(successfulAiMessage);
    }

    default public OutputGuardrailResult successWith(AiMessage successfulAiMessage, Object successfulResult) {
        return OutputGuardrailResult.successWith(successfulAiMessage, successfulResult);
    }

    default public OutputGuardrailResult failure(String message) {
        return new OutputGuardrailResult(new OutputGuardrailResult.Failure(message), false);
    }

    default public OutputGuardrailResult failure(String message, Throwable cause) {
        return new OutputGuardrailResult(new OutputGuardrailResult.Failure(message, cause), false);
    }

    default public OutputGuardrailResult fatal(String message) {
        return new OutputGuardrailResult(Arrays.asList(new OutputGuardrailResult.Failure(message)), true);
    }

    default public OutputGuardrailResult fatal(String message, Throwable cause) {
        return new OutputGuardrailResult(Arrays.asList(new OutputGuardrailResult.Failure(message, cause)), true);
    }

    default public OutputGuardrailResult retry(String message) {
        return new OutputGuardrailResult(Arrays.asList(new OutputGuardrailResult.Failure(message, null, true)), true);
    }

    default public OutputGuardrailResult retry(String message, Throwable cause) {
        return new OutputGuardrailResult(Arrays.asList(new OutputGuardrailResult.Failure(message, cause, true)), true);
    }

    default public OutputGuardrailResult reprompt(String message, String reprompt) {
        return new OutputGuardrailResult(Arrays.asList(new OutputGuardrailResult.Failure(message, null, true, reprompt)), true);
    }

    default public OutputGuardrailResult reprompt(String message, Throwable cause, String reprompt) {
        return new OutputGuardrailResult(Arrays.asList(new OutputGuardrailResult.Failure(message, cause, true, reprompt)), true);
    }

    default public OutputGuardrailResult failureWithMessageRemoval(String message) {
        return OutputGuardrailResult.failureWithMessageRemoval(message);
    }

    default public OutputGuardrailResult failureWithMessageRemoval(String message, Throwable cause) {
        return OutputGuardrailResult.failureWithMessageRemoval(message, cause);
    }

    default public OutputGuardrailResult fatalWithMessageRemoval(String message) {
        return OutputGuardrailResult.fatalWithMessageRemoval(message);
    }

    default public OutputGuardrailResult fatalWithMessageRemoval(String message, Throwable cause) {
        return OutputGuardrailResult.fatalWithMessageRemoval(message, cause);
    }
}

