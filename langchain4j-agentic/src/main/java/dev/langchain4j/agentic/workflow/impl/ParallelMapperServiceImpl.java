/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow.impl;

import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.declarative.DeclarativeUtil;
import dev.langchain4j.agentic.declarative.ParallelMapperAgent;
import dev.langchain4j.agentic.internal.AbstractServiceBuilder;
import dev.langchain4j.agentic.internal.AgentUtil;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgenticSystemConfigurationException;
import dev.langchain4j.agentic.workflow.ParallelMapperService;
import dev.langchain4j.agentic.workflow.impl.ParallelMapperPlanner;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

public class ParallelMapperServiceImpl<T>
extends AbstractServiceBuilder<T, ParallelMapperService<T>>
implements ParallelMapperService<T> {
    public static final String SERVICE_TYPE = "ParallelMapper";
    private String itemsProvider;

    public ParallelMapperServiceImpl(Class<T> agentServiceClass, Method agenticMethod) {
        super(agentServiceClass, agenticMethod);
        this.configureParallelMapper(agentServiceClass);
    }

    @Override
    public ParallelMapperService<T> itemsProvider(String itemsProvider) {
        this.itemsProvider = itemsProvider;
        return this;
    }

    @Override
    public T build() {
        boolean isArrayResult = this.isArrayResult();
        Class<?> arrayclass = isArrayResult ? this.agenticMethod.getReturnType() : null;
        return this.build(() -> new ParallelMapperPlanner(this.itemsProvider(), isArrayResult, arrayclass));
    }

    private String itemsProvider() {
        if (this.itemsProvider != null && !this.itemsProvider.trim().isEmpty()) {
            return this.itemsProvider;
        }
        if (this.agenticMethod == null) {
            throw new AgenticSystemConfigurationException("It is mandatory to declare an itemsProvider using an untyped parallel mapper.");
        }
        AgentArgument itemsArgument = null;
        List<AgentArgument> agentArguments = AgentUtil.argumentsFromMethod(this.agenticMethod);
        for (AgentArgument agentArgument : agentArguments) {
            if (!Collection.class.isAssignableFrom(agentArgument.rawType()) && !agentArgument.rawType().isArray()) continue;
            if (itemsArgument != null) {
                throw new AgenticSystemConfigurationException("Multiple collection arguments found in class " + this.agentServiceClass.getName() + ", please disambiguate specifying the itemsProvider.");
            }
            itemsArgument = agentArgument;
        }
        if (itemsArgument == null) {
            throw new AgenticSystemConfigurationException("Class " + this.agentServiceClass.getName() + " doesn't have a collection argument on which to iterate.");
        }
        return itemsArgument.name();
    }

    private boolean isArrayResult() {
        if (this.agenticMethod == null) {
            return false;
        }
        Class<?> returnType = this.agenticMethod.getReturnType();
        if (returnType.isArray()) {
            return true;
        }
        if (Collection.class.isAssignableFrom(returnType) || this.output != null) {
            return false;
        }
        throw new AgenticSystemConfigurationException("The return type of " + this.agentServiceClass.getName() + " must be either a collection or an array.");
    }

    public static ParallelMapperServiceImpl<UntypedAgent> builder() {
        return new ParallelMapperServiceImpl<UntypedAgent>(UntypedAgent.class, null);
    }

    public static <T> ParallelMapperServiceImpl<T> builder(Class<T> agentServiceClass) {
        return new ParallelMapperServiceImpl<T>(agentServiceClass, AgentUtil.validateAgentClass(agentServiceClass, false, ParallelMapperAgent.class));
    }

    @Override
    public String serviceType() {
        return SERVICE_TYPE;
    }

    private void configureParallelMapper(Class<T> agentServiceClass) {
        DeclarativeUtil.configureOutput(agentServiceClass, this);
        DeclarativeUtil.buildAgentFeatures(agentServiceClass, this);
        DeclarativeUtil.parallelExecutor(agentServiceClass).ifPresent(this::executor);
    }
}

