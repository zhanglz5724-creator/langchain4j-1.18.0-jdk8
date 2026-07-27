/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.Guardrail;
import dev.langchain4j.guardrail.GuardrailResult;
import dev.langchain4j.guardrail.OutputGuardrailRequest;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.response.ChatResponse;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class OutputGuardrailResult
implements GuardrailResult<OutputGuardrailResult> {
    private static final OutputGuardrailResult SUCCESS = new OutputGuardrailResult();
    private final GuardrailResult.Result result;
    private final AiMessage successfulAiMessage;
    private final Object successfulResult;
    private final List<Failure> failures;

    private OutputGuardrailResult(GuardrailResult.Result result, AiMessage successfulAiMessage, Object successfulResult, List<Failure> failures) {
        this.result = ValidationUtils.ensureNotNull(result, "result");
        this.successfulAiMessage = successfulAiMessage;
        this.successfulResult = successfulResult;
        this.failures = Optional.ofNullable(failures).orElseGet(Collections::emptyList);
    }

    private OutputGuardrailResult(GuardrailResult.Result result, String successfulText, Object successfulResult, List<Failure> failures) {
        this(result, successfulText == null ? null : AiMessage.from(successfulText), successfulResult, failures);
    }

    private OutputGuardrailResult() {
        this(GuardrailResult.Result.SUCCESS, (AiMessage)null, null, Collections.emptyList());
    }

    private OutputGuardrailResult(String successfulText) {
        this(GuardrailResult.Result.SUCCESS_WITH_RESULT, successfulText, null, Collections.emptyList());
    }

    private OutputGuardrailResult(String successfulText, Object successfulResult) {
        this(GuardrailResult.Result.SUCCESS_WITH_RESULT, successfulText, successfulResult, Collections.emptyList());
    }

    private OutputGuardrailResult(AiMessage successfulAiMessage) {
        this(GuardrailResult.Result.SUCCESS_WITH_RESULT, successfulAiMessage, null, Collections.emptyList());
    }

    private OutputGuardrailResult(AiMessage successfulAiMessage, Object successfulResult) {
        this(GuardrailResult.Result.SUCCESS_WITH_RESULT, successfulAiMessage, successfulResult, Collections.emptyList());
    }

    OutputGuardrailResult(List<Failure> failures, boolean fatal) {
        this(fatal ? GuardrailResult.Result.FATAL : GuardrailResult.Result.FAILURE, (AiMessage)null, null, failures);
    }

    OutputGuardrailResult(Failure failure, boolean fatal) {
        this(Stream.of(failure).collect(Collectors.toList()), fatal);
    }

    public static OutputGuardrailResult success() {
        return SUCCESS;
    }

    public static OutputGuardrailResult successWith(String successfulText) {
        return successfulText == null ? OutputGuardrailResult.success() : new OutputGuardrailResult(successfulText);
    }

    public static OutputGuardrailResult successWith(String successfulText, Object successfulResult) {
        return new OutputGuardrailResult(successfulText, successfulResult);
    }

    public static OutputGuardrailResult successWith(AiMessage successfulAiMessage) {
        return successfulAiMessage == null ? OutputGuardrailResult.success() : new OutputGuardrailResult(successfulAiMessage);
    }

    public static OutputGuardrailResult successWith(AiMessage successfulAiMessage, Object successfulResult) {
        return new OutputGuardrailResult(successfulAiMessage, successfulResult);
    }

    public static OutputGuardrailResult failure(List<Failure> failures) {
        return new OutputGuardrailResult(failures, false);
    }

    public boolean isRetry() {
        return !this.isSuccess() && this.failures.stream().anyMatch(Failure::retry);
    }

    public boolean isReprompt() {
        return !this.isSuccess() && this.failures.stream().map(Failure::reprompt).filter(Objects::nonNull).count() > 0L;
    }

    public OutputGuardrailResult blockRetry() {
        this.failures.set(0, this.failures.get(0).blockRetry());
        return this;
    }

    public Optional<String> getReprompt() {
        return !this.isSuccess() ? this.failures.stream().map(Failure::reprompt).filter(Objects::nonNull).findFirst() : Optional.empty();
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return this.asString();
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        OutputGuardrailResult that = (OutputGuardrailResult)o;
        return this.result == that.result && Objects.equals(this.successfulAiMessage, that.successfulAiMessage) && Objects.equals(this.successfulResult, that.successfulResult) && Objects.equals(this.failures, that.failures);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        return Objects.hash(new Object[]{this.result, this.successfulAiMessage, this.successfulResult, this.failures});
    }

    public <T> T response(OutputGuardrailRequest request) {
        return (T)Optional.ofNullable(this.successfulResult).orElseGet(() -> this.createResponse(request));
    }

    private ChatResponse createResponse(OutputGuardrailRequest params) {
        ChatResponse response = params.responseFromLLM();
        return response.toBuilder().aiMessage(this.hasRewrittenResult() ? this.successfulAiMessage : response.aiMessage()).build();
    }

    @Override
    public GuardrailResult.Result result() {
        return this.result;
    }

    @Override
    public <F extends GuardrailResult.Failure> List<F> failures() {
        return (List<F>) this.failures;
    }

    @Override
    public String successfulText() {
        return this.successfulAiMessage.text();
    }

    public Object successfulResult() {
        return this.successfulResult;
    }

    public boolean shouldRemoveViolatingMessage() {
        return !this.isSuccess() && this.failures.stream().anyMatch(Failure::removeViolatingMessage);
    }

    public static OutputGuardrailResult failureWithMessageRemoval(String message) {
        return new OutputGuardrailResult(new Failure(message, null, null, false, null, true), false);
    }

    public static OutputGuardrailResult failureWithMessageRemoval(String message, Throwable cause) {
        return new OutputGuardrailResult(new Failure(message, cause, null, false, null, true), false);
    }

    public static OutputGuardrailResult fatalWithMessageRemoval(String message) {
        return new OutputGuardrailResult(new Failure(message, null, null, false, null, true), true);
    }

    public static OutputGuardrailResult fatalWithMessageRemoval(String message, Throwable cause) {
        return new OutputGuardrailResult(new Failure(message, cause, null, false, null, true), true);
    }

    public static final class Failure
    implements GuardrailResult.Failure {
        private final String message;
        private final Throwable cause;
        private final Class<? extends Guardrail> guardrailClass;
        private final boolean retry;
        private final String reprompt;
        private final boolean removeViolatingMessage;

        Failure(String message, Throwable cause, Class<? extends Guardrail> guardrailClass, boolean retry, String reprompt, boolean removeViolatingMessage) {
            this.message = ValidationUtils.ensureNotNull(message, "message");
            this.cause = cause;
            this.guardrailClass = guardrailClass;
            this.retry = retry;
            this.reprompt = reprompt;
            this.removeViolatingMessage = removeViolatingMessage;
        }

        Failure(String message) {
            this(message, null, null, false, null, false);
        }

        Failure(String message, Throwable cause) {
            this(message, cause, null, false, null, false);
        }

        Failure(String message, Throwable cause, boolean retry) {
            this(message, cause, null, retry, null, false);
        }

        Failure(String message, Throwable cause, boolean retry, String reprompt) {
            this(message, cause, null, retry, reprompt, false);
        }

        @Override
        public Failure withGuardrailClass(Class<? extends Guardrail> guardrailClass) {
            ValidationUtils.ensureNotNull(guardrailClass, "guardrailClass");
            return new Failure(this.message(), this.cause(), guardrailClass, this.retry, this.reprompt, this.removeViolatingMessage);
        }

        @Override
        public String message() {
            return this.message;
        }

        @Override
        public Throwable cause() {
            return this.cause;
        }

        @Override
        public Class<? extends Guardrail> guardrailClass() {
            return this.guardrailClass;
        }

        public Failure blockRetry() {
            return this.retry ? new Failure("Retry or reprompt is not allowed after a rewritten output", this.cause(), this.guardrailClass, false, this.reprompt, this.removeViolatingMessage) : this;
        }

        public String toString() {
            return this.asString();
        }

        public boolean retry() {
            return this.retry;
        }

        public String reprompt() {
            return this.reprompt;
        }

        public boolean removeViolatingMessage() {
            return this.removeViolatingMessage;
        }
    }
}

