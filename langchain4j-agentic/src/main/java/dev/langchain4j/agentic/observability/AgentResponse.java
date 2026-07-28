/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.response.ChatResponse
 */
package dev.langchain4j.agentic.observability;

import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import java.util.Map;
import java.util.Objects;

public class AgentResponse {
    private final AgenticScope agenticScope;
    private final AgentInstance agent;
    private final Map<String, Object> inputs;
    private final Object output;
    private final ChatRequest chatRequest;
    private final ChatResponse chatResponse;

    public AgentResponse(AgenticScope agenticScope, AgentInstance agent, Map<String, Object> inputs, Object output, ChatRequest chatRequest, ChatResponse chatResponse) {
        this.agenticScope = agenticScope;
        this.agent = agent;
        this.inputs = inputs;
        this.output = output;
        this.chatRequest = chatRequest;
        this.chatResponse = chatResponse;
    }

    public AgentResponse(AgenticScope agenticScope, AgentInstance agent, Map<String, Object> inputs, Object output) {
        this(agenticScope, agent, inputs, output, null, null);
    }

    public String agentName() {
        return this.agent.name();
    }

    public String agentId() {
        return this.agent.agentId();
    }

    public AgenticScope agenticScope() {
        return this.agenticScope;
    }

    public AgentInstance agent() {
        return this.agent;
    }

    public Map<String, Object> inputs() {
        return this.inputs;
    }

    public Object output() {
        return this.output;
    }

    public ChatRequest chatRequest() {
        return this.chatRequest;
    }

    public ChatResponse chatResponse() {
        return this.chatResponse;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentResponse)) {
            return false;
        }
        AgentResponse other = (AgentResponse)o;
        if (!Objects.equals(this.agenticScope, other.agenticScope)) {
            return false;
        }
        if (!Objects.equals(this.agent, other.agent)) {
            return false;
        }
        if (!Objects.equals(this.inputs, other.inputs)) {
            return false;
        }
        if (!Objects.equals(this.output, other.output)) {
            return false;
        }
        if (!Objects.equals(this.chatRequest, other.chatRequest)) {
            return false;
        }
        return Objects.equals(this.chatResponse, other.chatResponse);
    }

    public int hashCode() {
        return Objects.hash(this.agenticScope, this.agent, this.inputs, this.output, this.chatRequest, this.chatResponse);
    }

    public String toString() {
        return "AgentResponse{agenticScope=" + this.agenticScope + ", agent=" + this.agent + ", inputs=" + this.inputs + ", output=" + this.output + ", chatRequest=" + this.chatRequest + ", chatResponse=" + this.chatResponse + "}";
    }
}

