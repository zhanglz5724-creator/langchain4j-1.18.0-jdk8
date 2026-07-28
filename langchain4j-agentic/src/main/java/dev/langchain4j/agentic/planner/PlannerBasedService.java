/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.planner;

import dev.langchain4j.agentic.planner.AgenticService;
import dev.langchain4j.agentic.planner.Planner;
import java.util.function.Supplier;

public interface PlannerBasedService<T>
extends AgenticService<PlannerBasedService<T>, T> {
    public PlannerBasedService<T> planner(Supplier<Planner> var1);
}

