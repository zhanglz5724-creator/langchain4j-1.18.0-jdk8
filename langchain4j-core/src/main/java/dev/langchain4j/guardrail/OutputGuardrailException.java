/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.guardrail.GuardrailException;
import dev.langchain4j.guardrail.OutputGuardrailResult;

public final class OutputGuardrailException
extends GuardrailException {
    private final OutputGuardrailResult result;

    public OutputGuardrailException(String message) {
        this(message, null);
    }

    public OutputGuardrailException(String message, Throwable cause) {
        this(message, cause, null);
    }

    public OutputGuardrailException(String message, Throwable cause, OutputGuardrailResult result) {
        super(message, cause);
        this.result = result;
    }

    public OutputGuardrailResult result() {
        return this.result;
    }
}

