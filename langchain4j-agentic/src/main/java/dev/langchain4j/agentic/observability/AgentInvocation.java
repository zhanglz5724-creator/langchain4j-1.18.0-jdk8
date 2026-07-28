/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.service.tool.ToolExecution
 */
package dev.langchain4j.agentic.observability;

import dev.langchain4j.agentic.observability.AgentRequest;
import dev.langchain4j.agentic.observability.AgentResponse;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.service.tool.ToolExecution;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AgentInvocation {
    private final List<AgentInvocation> nestedInvocations = Collections.synchronizedList(new ArrayList());
    private final List<ToolExecution> toolExecutions = Collections.synchronizedList(new ArrayList());
    private final AgentRequest agentRequest;
    private final LocalDateTime startTime;
    private AgentResponse agentResponse;
    private LocalDateTime finishTime;
    private int iterationIndex = -1;

    AgentInvocation(AgentRequest agentRequest) {
        this.agentRequest = agentRequest;
        this.startTime = LocalDateTime.now();
    }

    void finished(AgentResponse agentResponse) {
        this.agentResponse = agentResponse;
        this.finishTime = LocalDateTime.now();
    }

    void addNestedInvocation(AgentInvocation agentInvocation) {
        this.nestedInvocations.add(agentInvocation);
    }

    void addToolExecution(ToolExecution toolExecution) {
        this.toolExecutions.add(toolExecution);
    }

    public boolean done() {
        return this.finishTime != null;
    }

    public LocalDateTime startTime() {
        return this.startTime;
    }

    public LocalDateTime finishTime() {
        return this.finishTime;
    }

    public Duration duration() {
        if (!this.done()) {
            throw new IllegalStateException("Agent call is not finished yet");
        }
        return Duration.between(this.startTime, this.finishTime);
    }

    public AgentInstance agent() {
        return this.agentRequest.agent();
    }

    public AgenticScope agenticScope() {
        return this.agentRequest.agenticScope();
    }

    public Map<String, Object> inputs() {
        return this.agentRequest.inputs();
    }

    public Object output() {
        if (!this.done()) {
            throw new IllegalStateException("Agent call is not finished yet");
        }
        return this.agentResponse.output();
    }

    public int totalTokenCount() {
        return this.tokenUsage().map(TokenUsage::totalTokenCount).orElse(0);
    }

    public Optional<TokenUsage> tokenUsage() {
        if (!this.done()) {
            throw new IllegalStateException("Agent call is not finished yet");
        }
        return Optional.ofNullable(this.agentResponse.chatResponse()).map(ChatResponse::metadata).map(ChatResponseMetadata::tokenUsage);
    }

    public int iterationIndex() {
        return this.iterationIndex;
    }

    void setIterationIndex(int iterationIndex) {
        this.iterationIndex = iterationIndex;
    }

    public List<AgentInvocation> nestedInvocations() {
        return this.nestedInvocations;
    }

    public List<ToolExecution> toolExecutions() {
        return this.toolExecutions;
    }

    public String toString() {
        return this.toString("");
    }

    private String toString(String prefix) {
        StringBuilder sb = new StringBuilder(prefix + "AgentInvocation{agent=" + this.agent().name() + (this.iterationIndex >= 0 ? ", iteration=" + this.iterationIndex : "") + ", startTime=" + this.startTime + ", finishTime=" + this.finishTime + ", duration=" + (this.done() ? this.duration().toMillis() + " ms" : "in progress") + ", tokens=" + (this.done() ? Integer.valueOf(this.totalTokenCount()) : "in progress") + ", inputs=" + this.shortToString(this.inputs()) + ", output=" + (this.done() ? this.shortToString(this.output()) : "in progress") + '}');
        if (!this.toolExecutions.isEmpty()) {
            String toolPrefix = prefix.isEmpty() ? "|-> " : "    " + prefix;
            for (ToolExecution toolExec : this.toolExecutions) {
                sb.append("\n").append(toolPrefix).append(toolExec);
            }
        }
        if (!this.nestedInvocations.isEmpty()) {
            String nestedPrefix = prefix.isEmpty() ? "|=> " : "    " + prefix;
            for (AgentInvocation nestedCall : this.nestedInvocations) {
                sb.append("\n").append(nestedCall.toString(nestedPrefix));
            }
        }
        return sb.toString();
    }

    private String shortToString(Object o) {
        if (o == null) {
            return "null";
        }
        String s = o.toString();
        return s.substring(0, Math.min(s.length(), 15)) + (s.length() > 15 ? "..." : "");
    }

    private String shortToString(Map<?, ?> map) {
        if (map.isEmpty()) {
            return "{}";
        }
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (Map.Entry<?, ?> e : map.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(',').append(' ');
            }
            Object key = e.getKey();
            Object value = e.getValue();
            sb.append((Object)(key == this ? "(this Map)" : key));
            sb.append('=');
            sb.append(value == this ? "(this Map)" : this.shortToString(value));
        }
        return sb.append('}').toString();
    }
}

