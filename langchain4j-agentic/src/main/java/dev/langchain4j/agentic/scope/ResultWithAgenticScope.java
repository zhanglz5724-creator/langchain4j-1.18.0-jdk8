/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.scope;

import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public final class ResultWithAgenticScope<T> {
    private final AgenticScope agenticScope;
    private final T result;
    private final boolean suspended;
    private final transient Supplier<ResultWithAgenticScope<T>> resumeCallback;

    public ResultWithAgenticScope(AgenticScope agenticScope, T result) {
        this(agenticScope, result, false, null);
    }

    public ResultWithAgenticScope(AgenticScope agenticScope, T result, boolean suspended) {
        this(agenticScope, result, suspended, null);
    }

    public ResultWithAgenticScope(AgenticScope agenticScope, T result, boolean suspended, Supplier<ResultWithAgenticScope<T>> resumeCallback) {
        this.agenticScope = agenticScope;
        this.result = result;
        this.suspended = suspended;
        this.resumeCallback = resumeCallback;
    }

    public AgenticScope agenticScope() {
        return this.agenticScope;
    }

    public T result() {
        return this.result;
    }

    public boolean suspended() {
        return this.suspended;
    }

    public ResultWithAgenticScope<T> completePendingResponse(Object value) {
        return this.completePendingResponse(this.singlePendingResponseId(), value);
    }

    public ResultWithAgenticScope<T> completePendingResponse(String responseId, Object value) {
        if (!this.suspended) {
            throw new IllegalStateException("Cannot complete a pending response on a non-suspended result");
        }
        if (this.resumeCallback == null) {
            throw new IllegalStateException("No resume callback available. After a crash/restart, use AgenticScope.completePendingResponse() and re-invoke the agent method directly.");
        }
        this.agenticScope.completePendingResponse(responseId, value);
        return this.resumeCallback.get();
    }

    private String singlePendingResponseId() {
        Set<String> ids = this.agenticScope.pendingResponseIds();
        if (ids.size() != 1) {
            throw new IllegalStateException("Expected exactly 1 pending response, but found " + ids.size() + ": " + ids);
        }
        return ids.iterator().next();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResultWithAgenticScope)) {
            return false;
        }
        ResultWithAgenticScope that = (ResultWithAgenticScope)o;
        return this.suspended == that.suspended && Objects.equals(this.agenticScope, that.agenticScope) && Objects.equals(this.result, that.result);
    }

    public int hashCode() {
        return Objects.hash(this.agenticScope, this.result, this.suspended);
    }

    public String toString() {
        return "ResultWithAgenticScope[agenticScope=" + this.agenticScope + ", result=" + this.result + ", suspended=" + this.suspended + "]";
    }
}

