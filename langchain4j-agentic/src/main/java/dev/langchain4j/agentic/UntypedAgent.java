/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.service.V
 */
package dev.langchain4j.agentic;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.scope.AgenticScopeAccess;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import dev.langchain4j.service.V;
import java.util.Map;

public interface UntypedAgent
extends AgenticScopeAccess {
    @Agent
    public Object invoke(@V(value="input") Map<String, Object> var1);

    public ResultWithAgenticScope<String> invokeWithAgenticScope(Map<String, Object> var1);
}

