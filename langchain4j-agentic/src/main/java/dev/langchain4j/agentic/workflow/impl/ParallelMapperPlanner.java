/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow.impl;

import dev.langchain4j.agentic.internal.AgentExecutor;
import dev.langchain4j.agentic.internal.MapperAgentInvoker;
import dev.langchain4j.agentic.planner.Action;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.InitPlanningContext;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.agentic.planner.PlanningContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelMapperPlanner
implements Planner {
    private final String itemsProvider;
    private final boolean isArrayResult;
    private final Class<? extends Object[]> arrayclass;
    private AgentExecutor subagent;
    private String outputKey;
    private String resultKeyPrefix;
    private int itemCount;
    private final AtomicInteger completedCount = new AtomicInteger();

    public ParallelMapperPlanner(String itemsProvider, boolean isArrayResult, Class<? extends Object[]> arrayclass) {
        this.itemsProvider = itemsProvider;
        this.isArrayResult = isArrayResult;
        this.arrayclass = arrayclass;
    }

    @Override
    public void init(InitPlanningContext initPlanningContext) {
        this.subagent = (AgentExecutor)initPlanningContext.subagents().get(0);
        this.outputKey = initPlanningContext.plannerAgent().outputKey();
    }

    @Override
    public Action firstAction(PlanningContext planningContext) {
        Object collectionObj = planningContext.agenticScope().readState(this.itemsProvider);
        if (collectionObj == null) {
            return this.done();
        }
        List<?> items = this.collectItems(collectionObj);
        if (items.isEmpty()) {
            return this.done();
        }
        this.itemCount = items.size();
        this.resultKeyPrefix = this.subagent.agentInvoker().outputKey();
        ArrayList<AgentInstance> instances = new ArrayList<AgentInstance>(items.size());
        for (int i = 0; i < items.size(); ++i) {
            Object item = items.get(i);
            MapperAgentInvoker instanceInvoker = new MapperAgentInvoker(this.subagent.agentInvoker(), item, i);
            instances.add(new AgentExecutor(instanceInvoker, this.subagent.agent()));
        }
        return this.call(instances);
    }

    private List<?> collectItems(Object collectionObj) {
        List<Object> items;
        if (collectionObj instanceof List) {
            ArrayList list;
            items = list = (ArrayList)collectionObj;
        } else if (collectionObj instanceof Collection) {
            Collection collection = (Collection)collectionObj;
            items = new ArrayList(collection);
        } else if (collectionObj.getClass().isArray()) {
            items = Arrays.asList((Object[])collectionObj);
        } else {
            throw new IllegalArgumentException("The value for itemsProvider '" + this.itemsProvider + "' must be a Collection or array, but was: " + collectionObj.getClass().getName());
        }
        return items;
    }

    @Override
    public Action nextAction(PlanningContext planningContext) {
        if (this.completedCount.incrementAndGet() >= this.itemCount) {
            T[] result;
            T[] results = new ArrayList(this.itemCount);
            for (int i = 0; i < this.itemCount; ++i) {
                results.add(planningContext.agenticScope().readState(this.resultKeyPrefix + "_" + i));
                planningContext.agenticScope().writeState(this.resultKeyPrefix + "_" + i, null);
            }
            T[] TArray = result = this.isArrayResult ? Arrays.copyOf(results.toArray(), results.size(), this.arrayclass) : results;
            if (this.outputKey != null && !this.outputKey.trim().isEmpty()) {
                planningContext.agenticScope().writeState(this.outputKey, result);
            }
            return this.done(result);
        }
        return this.done();
    }

    @Override
    public AgenticSystemTopology topology() {
        return AgenticSystemTopology.PARALLEL;
    }
}

