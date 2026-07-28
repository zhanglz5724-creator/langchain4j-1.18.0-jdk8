/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agentic.Agent
 *  dev.langchain4j.agentic.scope.AgenticScopeAccess
 *  dev.langchain4j.agentic.scope.ResultWithAgenticScope
 *  dev.langchain4j.service.V
 */
package dev.langchain4j.agentic.patterns.p2p;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.scope.AgenticScopeAccess;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import dev.langchain4j.service.V;

public interface P2PAgent
extends AgenticScopeAccess {
    public static final String P2P_REQUEST_KEY = "p2pRequest";

    @Agent
    public String invoke(@V(value="p2pRequest") String var1);

    public ResultWithAgenticScope<String> invokeWithAgenticScope(@V(value="p2pRequest") String var1);
}

