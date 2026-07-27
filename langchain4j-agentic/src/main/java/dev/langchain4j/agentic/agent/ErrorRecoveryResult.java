package dev.langchain4j.agentic.agent;
public class ErrorRecoveryResult {
    private final Type type;
    private final Object result;

    public ErrorRecoveryResult(Type type, Object result) {
        this.type = type;
        this.result = result;
    }

    public Type getType() {
        return type;
    }

    public Object getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorRecoveryResult that = (ErrorRecoveryResult) o;
        return java.util.Objects.equals(this.type, that.type) && java.util.Objects.equals(this.result, that.result);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(type, result);
    }

    @Override
    public String toString() {
        return "ErrorRecoveryResult{"type=" + type + , "result=" + result + "}"";
    }


    public enum Type {
        THROW_EXCEPTION, RETURN_RESULT, RETRY
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
}
