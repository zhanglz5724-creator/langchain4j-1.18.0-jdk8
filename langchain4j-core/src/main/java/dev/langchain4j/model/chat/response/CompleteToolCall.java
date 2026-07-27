/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.response;

import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Objects;

@Experimental
@JacocoIgnoreCoverageGenerated
public class CompleteToolCall {
    private final int index;
    private final ToolExecutionRequest toolExecutionRequest;

    public CompleteToolCall(int index, ToolExecutionRequest toolExecutionRequest) {
        this.index = ValidationUtils.ensureNotNegative(index, "index");
        this.toolExecutionRequest = ValidationUtils.ensureNotNull(toolExecutionRequest, "toolExecutionRequest");
    }

    public int index() {
        return this.index;
    }

    public ToolExecutionRequest toolExecutionRequest() {
        return this.toolExecutionRequest;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        CompleteToolCall that = (CompleteToolCall)object;
        return this.index == that.index && Objects.equals(this.toolExecutionRequest, that.toolExecutionRequest);
    }

    public int hashCode() {
        return Objects.hash(this.index, this.toolExecutionRequest);
    }

    public String toString() {
        return "CompleteToolCall{index=" + this.index + ", toolExecutionRequest=" + this.toolExecutionRequest + '}';
    }
}

