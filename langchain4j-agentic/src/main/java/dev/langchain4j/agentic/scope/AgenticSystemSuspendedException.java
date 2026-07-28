/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.scope;

import dev.langchain4j.agentic.scope.AgenticScope;

public class AgenticSystemSuspendedException
extends RuntimeException {
    private final AgenticScope scope;

    public AgenticSystemSuspendedException(AgenticScope scope) {
        super("Agentic system suspended: awaiting responses for " + scope.pendingResponseIds(), null, false, false);
        this.scope = scope;
    }

    public AgenticScope scope() {
        return this.scope;
    }
}

