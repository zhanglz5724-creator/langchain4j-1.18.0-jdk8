/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.service.tool;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.service.tool.ToolExecution;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Internal
public class ToolServiceResult {
    private final List<ChatResponse> intermediateResponses;
    private final ChatResponse finalResponse;
    private final List<ToolExecution> toolExecutions;
    private final TokenUsage aggregateTokenUsage;
    private final boolean immediateToolReturn;

    public ToolServiceResult(Builder builder) {
        this.intermediateResponses = Utils.copy((List)builder.intermediateResponses);
        this.finalResponse = (ChatResponse)ValidationUtils.ensureNotNull((Object)builder.finalResponse, (String)"finalResponse");
        this.toolExecutions = (List)ValidationUtils.ensureNotNull((Object)builder.toolExecutions, (String)"toolExecutions");
        this.aggregateTokenUsage = builder.aggregateTokenUsage;
        this.immediateToolReturn = builder.immediateToolReturn;
    }

    @Deprecated
    public ToolServiceResult(ChatResponse chatResponse, List<ToolExecution> toolExecutions) {
        this.intermediateResponses = Collections.emptyList();
        this.finalResponse = (ChatResponse)ValidationUtils.ensureNotNull((Object)chatResponse, (String)"chatResponse");
        this.toolExecutions = (List)ValidationUtils.ensureNotNull(toolExecutions, (String)"toolExecutions");
        this.aggregateTokenUsage = chatResponse.tokenUsage();
        this.immediateToolReturn = false;
    }

    public List<ChatResponse> intermediateResponses() {
        return this.intermediateResponses;
    }

    public ChatResponse finalResponse() {
        return this.finalResponse;
    }

    public ChatResponse aggregateResponse() {
        return ChatResponse.builder().aiMessage(this.finalResponse.aiMessage()).metadata(this.finalResponse.metadata().toBuilder().tokenUsage(this.aggregateTokenUsage).build()).build();
    }

    @Deprecated
    public ChatResponse chatResponse() {
        return this.aggregateResponse();
    }

    public List<ToolExecution> toolExecutions() {
        return this.toolExecutions;
    }

    public TokenUsage aggregateTokenUsage() {
        return this.aggregateTokenUsage;
    }

    public boolean immediateToolReturn() {
        return this.immediateToolReturn;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        ToolServiceResult that = (ToolServiceResult)obj;
        return Objects.equals(this.intermediateResponses, that.intermediateResponses) && Objects.equals(this.finalResponse, that.finalResponse) && Objects.equals(this.toolExecutions, that.toolExecutions) && Objects.equals(this.aggregateTokenUsage, that.aggregateTokenUsage) && Objects.equals(this.immediateToolReturn, that.immediateToolReturn);
    }

    public int hashCode() {
        return Objects.hash(this.intermediateResponses, this.finalResponse, this.toolExecutions, this.aggregateTokenUsage, this.immediateToolReturn);
    }

    public String toString() {
        return "ToolServiceResult{intermediateResponses=" + this.intermediateResponses + ", finalResponse=" + this.finalResponse + ", toolExecutions=" + this.toolExecutions + ", aggregateTokenUsage=" + this.aggregateTokenUsage + ", immediateToolReturn=" + this.immediateToolReturn + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<ChatResponse> intermediateResponses;
        private ChatResponse finalResponse;
        private List<ToolExecution> toolExecutions;
        private TokenUsage aggregateTokenUsage;
        private boolean immediateToolReturn;

        public Builder intermediateResponses(List<ChatResponse> intermediateResponses) {
            this.intermediateResponses = intermediateResponses;
            return this;
        }

        public Builder finalResponse(ChatResponse finalResponse) {
            this.finalResponse = finalResponse;
            return this;
        }

        public Builder toolExecutions(List<ToolExecution> toolExecutions) {
            this.toolExecutions = toolExecutions;
            return this;
        }

        public Builder aggregateTokenUsage(TokenUsage aggregateTokenUsage) {
            this.aggregateTokenUsage = aggregateTokenUsage;
            return this;
        }

        public Builder immediateToolReturn(boolean immediateToolReturn) {
            this.immediateToolReturn = immediateToolReturn;
            return this;
        }

        public ToolServiceResult build() {
            return new ToolServiceResult(this);
        }
    }
}

