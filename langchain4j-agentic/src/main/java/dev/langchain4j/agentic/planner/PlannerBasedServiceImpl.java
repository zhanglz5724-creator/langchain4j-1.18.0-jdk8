/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.planner;

import dev.langchain4j.agentic.declarative.DeclarativeUtil;
import dev.langchain4j.agentic.declarative.PlannerSupplier;
import dev.langchain4j.agentic.internal.AbstractServiceBuilder;
import dev.langchain4j.agentic.internal.AgentUtil;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.planner.PlannerBasedService;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Supplier;

public class PlannerBasedServiceImpl<T>
extends AbstractServiceBuilder<T, PlannerBasedService<T>>
implements PlannerBasedService<T> {
    private Supplier<Planner> plannerSupplier;

    public PlannerBasedServiceImpl(Class<T> agentServiceClass, Method agenticMethod) {
        super(agentServiceClass, agenticMethod);
        this.configurePlanner(agentServiceClass);
    }

    public static <T> PlannerBasedService<T> builder(Class<T> agentServiceClass) {
        return new PlannerBasedServiceImpl<T>(agentServiceClass, AgentUtil.validateAgentClass(agentServiceClass, false));
    }

    @Override
    public String serviceType() {
        return "Planner";
    }

    @Override
    public PlannerBasedService<T> planner(Supplier<Planner> plannerSupplier) {
        this.plannerSupplier = plannerSupplier;
        return this;
    }

    @Override
    public T build() {
        return this.build(this.plannerSupplier);
    }

    private void configurePlanner(Class<T> agentServiceClass) {
        DeclarativeUtil.configureOutput(agentServiceClass, this);
        DeclarativeUtil.buildAgentFeatures(agentServiceClass, this);
        Optional<Method> suppliedPlanner = AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, PlannerSupplier.class);
        if (!suppliedPlanner.isPresent()) {
            throw new IllegalArgumentException("A planner agent requires a method annotated with @PlannerSupplier that returns the Planner instance.");
        }
        Method method = suppliedPlanner.get();
        DeclarativeUtil.checkReturnType(method, Planner.class);
        this.planner(() -> (Planner)DeclarativeUtil.invokeStatic(method, new Object[0]));
    }
}

