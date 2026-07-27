package dev.langchain4j.agentic.observability;

import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.service.tool.ToolExecution;
public class AfterAgentToolExecution {
    private final AgentInstance agentInstance;
    private final ToolExecution toolExecution;

    public AfterAgentToolExecution(AgentInstance agentInstance, ToolExecution toolExecution) {
        this.agentInstance = agentInstance;
        this.toolExecution = toolExecution;
    }

    public AgentInstance getAgentInstance() {
        return agentInstance;
    }

    public ToolExecution getToolExecution() {
        return toolExecution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AfterAgentToolExecution that = (AfterAgentToolExecution) o;
        return java.util.Objects.equals(this.agentInstance, that.agentInstance) && java.util.Objects.equals(this.toolExecution, that.toolExecution);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(agentInstance, toolExecution);
    }

    @Override
    public String toString() {
        return "AfterAgentToolExecution{"agentInstance=" + agentInstance + , "toolExecution=" + toolExecution + "}"";
    }


    public AgenticScope agenticScope() {
        return (AgenticScope) toolExecution.invocationContext().managedParameters().get(AgenticScope.class);
    }
}
