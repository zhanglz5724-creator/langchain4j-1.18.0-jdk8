/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.Guardrail;
import dev.langchain4j.guardrail.GuardrailResult;
import dev.langchain4j.guardrail.InputGuardrailRequest;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.internal.ValidationUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class InputGuardrailResult
implements GuardrailResult<InputGuardrailResult> {
    private static final InputGuardrailResult SUCCESS = new InputGuardrailResult();
    private final GuardrailResult.Result result;
    private final String successfulText;
    private final List<Failure> failures;

    private InputGuardrailResult(GuardrailResult.Result result, String successfulText, List<Failure> failures) {
        this.result = ValidationUtils.ensureNotNull(result, "result");
        this.successfulText = successfulText;
        this.failures = Optional.ofNullable(failures).orElseGet(Collections::emptyList);
    }

    private InputGuardrailResult() {
        this(GuardrailResult.Result.SUCCESS, null, Collections.emptyList());
    }

    InputGuardrailResult(List<Failure> failures, boolean fatal) {
        this(fatal ? GuardrailResult.Result.FATAL : GuardrailResult.Result.FAILURE, null, failures);
    }

    InputGuardrailResult(Failure failure, boolean fatal) {
        this(new ArrayList<Failure>(Collections.singletonList(failure)), fatal);
    }

    private InputGuardrailResult(String successfulText) {
        this(GuardrailResult.Result.SUCCESS_WITH_RESULT, successfulText, Collections.emptyList());
    }

    public static InputGuardrailResult success() {
        return SUCCESS;
    }

    public static InputGuardrailResult successWith(String successfulText) {
        return successfulText == null ? InputGuardrailResult.success() : new InputGuardrailResult(successfulText);
    }

    @Override
    public GuardrailResult.Result result() {
        return this.result;
    }

    @Override
    public String successfulText() {
        return this.successfulText;
    }

    @Override
    public <F extends GuardrailResult.Failure> List<F> failures() {
        return (List<F>) this.failures;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return this.asString();
    }

    public UserMessage userMessage(InputGuardrailRequest params) {
        return this.hasRewrittenResult() ? params.rewriteUserMessage(this.successfulText()) : params.userMessage();
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        InputGuardrailResult that = (InputGuardrailResult)o;
        return this.result == that.result && Objects.equals(this.successfulText, that.successfulText) && Objects.equals(this.failures, that.failures);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        return Objects.hash(new Object[]{this.result, this.successfulText, this.failures});
    }

    public static final class Failure
    implements GuardrailResult.Failure {
        private final String message;
        private final Throwable cause;
        private final Class<? extends Guardrail> guardrailClass;

        Failure(String message, Throwable cause, Class<? extends Guardrail> guardrailClass) {
            this.message = ValidationUtils.ensureNotNull(message, "message");
            this.cause = cause;
            this.guardrailClass = guardrailClass;
        }

        Failure(String message) {
            this(message, null, null);
        }

        Failure(String message, Throwable cause) {
            this(message, cause, null);
        }

        @Override
        public Failure withGuardrailClass(Class<? extends Guardrail> guardrailClass) {
            ValidationUtils.ensureNotNull(guardrailClass, "guardrailClass");
            return new Failure(this.message, this.cause, guardrailClass);
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

        public String toString() {
            return this.asString();
        }
    }
}

