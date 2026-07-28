/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.agentic.scope;

import dev.langchain4j.Internal;
import dev.langchain4j.agentic.scope.DefaultAgenticScope;

@Internal
public interface AgenticScopeJsonCodec {
    public DefaultAgenticScope fromJson(String var1);

    public String toJson(DefaultAgenticScope var1);
}

