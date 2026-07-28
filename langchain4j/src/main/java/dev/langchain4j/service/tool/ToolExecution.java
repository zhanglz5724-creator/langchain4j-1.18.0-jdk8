/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.invocation.InvocationContext
 */
package dev.langchain4j.service.tool;

import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.service.tool.ToolExecutionResult;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ToolExecution {
    private final ToolExecutionRequest request;
    private final ToolExecutionResult result;
    private final LocalDateTime startTime;
    private final LocalDateTime finishTime;
    private final InvocationContext invocationContext;

    private ToolExecution(Builder builder) {
        this.request = (ToolExecutionRequest)ValidationUtils.ensureNotNull((Object)builder.request, (String)"request");
        this.result = (ToolExecutionResult)ValidationUtils.ensureNotNull((Object)builder.result, (String)"result");
        this.startTime = builder.startTime;
        this.finishTime = builder.finishTime;
        this.invocationContext = (InvocationContext)ValidationUtils.ensureNotNull((Object)builder.invocationContext, (String)"invocationContext");
    }

    public ToolExecutionRequest request() {
        return this.request;
    }

    public String result() {
        return this.result.resultText();
    }

    @Experimental
    public List<Content> resultContents() {
        return this.result.resultContents();
    }

    public Object resultObject() {
        return this.result.result();
    }

    public boolean hasFailed() {
        return this.result.isError();
    }

    public LocalDateTime startTime() {
        return this.startTime;
    }

    public LocalDateTime finishTime() {
        return this.finishTime;
    }

    public Duration duration() {
        if (this.startTime == null || this.finishTime == null) {
            return null;
        }
        return Duration.between(this.startTime, this.finishTime);
    }

    public InvocationContext invocationContext() {
        return this.invocationContext;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        ToolExecution that = (ToolExecution)object;
        return Objects.equals(this.request, that.request) && Objects.equals(this.result, that.result) && Objects.equals(this.startTime, that.startTime) && Objects.equals(this.finishTime, that.finishTime);
    }

    public int hashCode() {
        return Objects.hash(this.request, this.result, this.startTime, this.finishTime);
    }

    public String toString() {
        return "ToolExecution{request=" + this.request + ", result=" + this.result + ", startTime=" + this.startTime + ", finishTime=" + this.finishTime + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private ToolExecutionRequest request;
        private ToolExecutionResult result;
        private LocalDateTime startTime;
        private LocalDateTime finishTime;
        private InvocationContext invocationContext;

        private Builder() {
        }

        public Builder request(ToolExecutionRequest request) {
            this.request = request;
            return this;
        }

        public Builder result(ToolExecutionResult result) {
            this.result = result;
            return this;
        }

        public Builder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder finishTime(LocalDateTime finishTime) {
            this.finishTime = finishTime;
            return this;
        }

        public Builder invocationContext(InvocationContext invocationContext) {
            this.invocationContext = invocationContext;
            return this;
        }

        @Deprecated
        public Builder result(String result) {
            this.result = ToolExecutionResult.builder().resultText(result).build();
            return this;
        }

        public ToolExecution build() {
            return new ToolExecution(this);
        }
    }
}

