/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.guardrail.Guardrail;
import dev.langchain4j.internal.ValidationUtils;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public interface GuardrailResult<GR extends GuardrailResult<GR>> {
    public Result result();

    public <F extends Failure> List<F> failures();

    public String successfulText();

    default public boolean hasRewrittenResult() {
        return this.result() == Result.SUCCESS_WITH_RESULT;
    }

    default public boolean isFatal() {
        return this.result() == Result.FATAL;
    }

    default public boolean isSuccess() {
        Result result = this.result();
        return result == Result.SUCCESS || result == Result.SUCCESS_WITH_RESULT;
    }

    default public Throwable getFirstFailureException() {
        return !this.isSuccess() ? (Throwable)this.failures().stream().map(Failure::cause).filter(Objects::nonNull).findFirst().orElse(null) : null;
    }

    default public GR validatedBy(Class<? extends Guardrail> guardrailClass) {
        ValidationUtils.ensureNotNull(guardrailClass, "guardrailClass");
        if (!this.isSuccess()) {
            List failures = this.failures();
            if (failures.size() != 1) {
                throw new IllegalArgumentException();
            }
            failures.set(0, ((Failure)failures.get(0)).withGuardrailClass(guardrailClass));
        }
        return (GR)this;
    }

    default public String asString() {
        if (this.isSuccess()) {
            return this.hasRewrittenResult() ? String.format("Success with '%s'", this.successfulText()) : "Success";
        }
        return this.failures().stream().map(Object::toString).collect(Collectors.joining(", "));
    }

    public static interface Failure {
        public Failure withGuardrailClass(Class<? extends Guardrail> var1);

        public String message();

        public Throwable cause();

        public Class<? extends Guardrail> guardrailClass();

        default public String asString() {
            String guardrailName = Optional.ofNullable(this.guardrailClass()).map(Class::getName).orElse("");
            return String.format("The guardrail %s failed with this message: %s", guardrailName, this.message());
        }
    }

    public static enum Result {
        SUCCESS,
        SUCCESS_WITH_RESULT,
        FAILURE,
        FATAL;

    }
}

