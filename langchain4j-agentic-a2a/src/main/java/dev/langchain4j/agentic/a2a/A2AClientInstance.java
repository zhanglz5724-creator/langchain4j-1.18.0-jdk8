/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agentic.internal.InternalAgent
 *  org.a2aproject.sdk.spec.AgentCard
 */
package dev.langchain4j.agentic.a2a;

import dev.langchain4j.agentic.internal.InternalAgent;
import org.a2aproject.sdk.spec.AgentCard;

public interface A2AClientInstance
extends InternalAgent {
    public String[] inputKeys();

    public AgentCard agentCard();
}

