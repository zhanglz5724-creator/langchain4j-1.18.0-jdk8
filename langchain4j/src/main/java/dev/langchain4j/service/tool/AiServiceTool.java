/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ReturnBehavior
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.service.tool;

import dev.langchain4j.agent.tool.ReturnBehavior;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.service.tool.ToolExecutor;
import java.util.Objects;

public class AiServiceTool {
    private final ToolSpecification toolSpecification;
    private final ToolExecutor toolExecutor;
    private final ReturnBehavior returnBehavior;

    private AiServiceTool(Builder builder) {
        this.toolSpecification = (ToolSpecification)ValidationUtils.ensureNotNull((Object)builder.toolSpecification, (String)"toolSpecification");
        this.toolExecutor = (ToolExecutor)ValidationUtils.ensureNotNull((Object)builder.toolExecutor, (String)"toolExecutor");
        this.returnBehavior = (ReturnBehavior)Utils.getOrDefault((Object)builder.returnBehavior, (Object)ReturnBehavior.TO_LLM);
    }

    public String name() {
        return this.toolSpecification.name();
    }

    public ToolSpecification toolSpecification() {
        return this.toolSpecification;
    }

    public ToolExecutor toolExecutor() {
        return this.toolExecutor;
    }

    public ReturnBehavior returnBehavior() {
        return this.returnBehavior;
    }

    @Deprecated
    public boolean immediateReturn() {
        return this.returnBehavior == ReturnBehavior.IMMEDIATE;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AiServiceTool that = (AiServiceTool)o;
        return Objects.equals(this.toolSpecification, that.toolSpecification) && Objects.equals(this.toolExecutor, that.toolExecutor) && this.returnBehavior == that.returnBehavior;
    }

    public int hashCode() {
        return Objects.hash(this.toolSpecification, this.toolExecutor, this.returnBehavior);
    }

    public String toString() {
        return "AiServiceTool{toolSpecification=" + this.toolSpecification + ", toolExecutor=" + this.toolExecutor + ", returnBehavior=" + this.returnBehavior + '}';
    }

    public Builder toBuilder() {
        return AiServiceTool.builder().toolSpecification(this.toolSpecification).toolExecutor(this.toolExecutor).returnBehavior(this.returnBehavior);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ToolSpecification toolSpecification;
        private ToolExecutor toolExecutor;
        private ReturnBehavior returnBehavior;

        public Builder toolSpecification(ToolSpecification toolSpecification) {
            this.toolSpecification = toolSpecification;
            return this;
        }

        public Builder toolExecutor(ToolExecutor toolExecutor) {
            this.toolExecutor = toolExecutor;
            return this;
        }

        public Builder returnBehavior(ReturnBehavior returnBehavior) {
            this.returnBehavior = returnBehavior;
            return this;
        }

        @Deprecated
        public Builder immediateReturn(boolean immediateReturn) {
            if (immediateReturn) {
                this.returnBehavior = ReturnBehavior.IMMEDIATE;
            }
            return this;
        }

        public AiServiceTool build() {
            return new AiServiceTool(this);
        }
    }
}

