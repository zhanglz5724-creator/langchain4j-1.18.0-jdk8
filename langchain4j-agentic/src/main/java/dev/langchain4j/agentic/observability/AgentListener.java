/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.observability;

import dev.langchain4j.agentic.observability.AfterAgentToolExecution;
import dev.langchain4j.agentic.observability.AgentInvocationError;
import dev.langchain4j.agentic.observability.AgentRequest;
import dev.langchain4j.agentic.observability.AgentResponse;
import dev.langchain4j.agentic.observability.BeforeAgentToolExecution;
import dev.langchain4j.agentic.scope.AgenticScope;

public interface AgentListener {
    default public void beforeAgentInvocation(AgentRequest agentRequest) {
    }

    default public void afterAgentInvocation(AgentResponse agentResponse) {
    }

    default public void onAgentInvocationError(AgentInvocationError agentInvocationError) {
    }

    default public void afterAgenticScopeCreated(AgenticScope agenticScope) {
    }

    default public void beforeAgenticScopeDestroyed(AgenticScope agenticScope) {
    }

    default public void onAgenticSystemSuspended(AgenticScope agenticScope) {
    }

    default public void beforeAgentToolExecution(BeforeAgentToolExecution beforeAgentToolExecution) {
    }

    default public void afterAgentToolExecution(AfterAgentToolExecution afterAgentToolExecution) {
    }

    default public boolean inheritedBySubagents() {
        return false;
    }
}

