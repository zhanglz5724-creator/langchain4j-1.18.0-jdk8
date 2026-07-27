package dev.langchain4j.agentic.observability;

import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import java.util.Map;
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

    public AgenticScope getAgenticScope() {
        return agenticScope;
    }

    public AgentInstance getAgent() {
        return agent;
    }

    public Map<String, Object> getInputs() {
        return inputs;
    }

    public Object getOutput() {
        return output;
    }

    public ChatRequest getChatRequest() {
        return chatRequest;
    }

    public ChatResponse getChatResponse() {
        return chatResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentResponse that = (AgentResponse) o;
        return java.util.Objects.equals(this.agenticScope, that.agenticScope) && java.util.Objects.equals(this.agent, that.agent) && java.util.Objects.equals(this.inputs, that.inputs) && java.util.Objects.equals(this.output, that.output) && java.util.Objects.equals(this.chatRequest, that.chatRequest) && java.util.Objects.equals(this.chatResponse, that.chatResponse);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(agenticScope, agent, inputs, output, chatRequest, chatResponse);
    }

    @Override
    public String toString() {
        return "AgentResponse{"agenticScope=" + agenticScope + , "agent=" + agent + , "inputs=" + inputs + , "output=" + output + , "chatRequest=" + chatRequest + , "chatResponse=" + chatResponse + "}"";
    }


    AgentResponse(AgenticScope agenticScope, AgentInstance agent, Map<String, Object> inputs, Object output) {
        this(agenticScope, agent, inputs, output, null, null);
    }

    public String agentName() {
        return agent.name();
    }

    public String agentId() {
        return agent.agentId();
    }
}
