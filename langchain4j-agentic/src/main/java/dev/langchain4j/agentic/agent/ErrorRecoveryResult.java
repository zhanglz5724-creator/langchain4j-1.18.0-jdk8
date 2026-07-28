/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.agent;

import java.util.Objects;

public class ErrorRecoveryResult {
    private final Type type;
    private final Object result;

    public ErrorRecoveryResult(Type type, Object result) {
        this.type = type;
        this.result = result;
    }

    public Type type() {
        return this.type;
    }

    public Object result() {
        return this.result;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ErrorRecoveryResult)) {
            return false;
        }
        ErrorRecoveryResult other = (ErrorRecoveryResult)o;
        if (!Objects.equals((Object)this.type, (Object)other.type)) {
            return false;
        }
        return Objects.equals(this.result, other.result);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.type, this.result});
    }

    public String toString() {
        return "ErrorRecoveryResult{type=" + (Object)((Object)this.type) + ", result=" + this.result + "}";
    }

    public static ErrorRecoveryResult throwException() {
        return new ErrorRecoveryResult(Type.THROW_EXCEPTION, null);
    }

    public static ErrorRecoveryResult retry() {
        return new ErrorRecoveryResult(Type.RETRY, null);
    }

    public static ErrorRecoveryResult result(Object result) {
        return new ErrorRecoveryResult(Type.RETURN_RESULT, result);
    }

    public static enum Type {
        THROW_EXCEPTION,
        RETURN_RESULT,
        RETRY;

    }
}

