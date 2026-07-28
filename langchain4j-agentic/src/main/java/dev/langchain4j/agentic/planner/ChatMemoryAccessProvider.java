/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.service.memory.ChatMemoryAccess
 */
package dev.langchain4j.agentic.planner;

import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.service.memory.ChatMemoryAccess;

public interface ChatMemoryAccessProvider {
    public ChatMemoryAccess chatMemoryAccess(AgenticScope var1);
}

