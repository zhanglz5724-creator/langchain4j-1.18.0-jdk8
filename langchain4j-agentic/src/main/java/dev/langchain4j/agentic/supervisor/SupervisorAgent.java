/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.service.V
 */
package dev.langchain4j.agentic.supervisor;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.scope.AgenticScopeAccess;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import dev.langchain4j.service.V;

public interface SupervisorAgent
extends AgenticScopeAccess {
    @Agent
    public String invoke(@V(value="request") String var1);

    public ResultWithAgenticScope<String> invokeWithAgenticScope(@V(value="request") String var1);
}

