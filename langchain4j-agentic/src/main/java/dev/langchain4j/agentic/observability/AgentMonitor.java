/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.agentic.observability;

import dev.langchain4j.Internal;
import dev.langchain4j.agentic.observability.AfterAgentToolExecution;
import dev.langchain4j.agentic.observability.AgentInvocationError;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.observability.AgentRequest;
import dev.langchain4j.agentic.observability.AgentResponse;
import dev.langchain4j.agentic.observability.MonitoredExecution;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AgentMonitor
implements AgentListener {
    private static final int DEFAULT_MAX_RETAINED_SESSIONS = 100;
    private volatile int maxRetainedSessions = 100;
    private AgentInstance rootAgent;
    private final Map<Object, List<MonitoredExecution>> successfulExecutions;
    private final Map<Object, List<MonitoredExecution>> failedExecutions;
    private final Map<Object, MonitoredExecution> ongoingExecutions = new ConcurrentHashMap<Object, MonitoredExecution>();

    public AgentMonitor() {
        this.successfulExecutions = this.createBoundedMap();
        this.failedExecutions = this.createBoundedMap();
    }

    public void setMaxRetainedSessions(int maxRetainedSessions) {
        if (maxRetainedSessions < 0) {
            throw new IllegalArgumentException("maxRetainedSessions must be >= 0");
        }
        this.maxRetainedSessions = maxRetainedSessions;
        AgentMonitor.trimToSize(this.successfulExecutions, maxRetainedSessions);
        AgentMonitor.trimToSize(this.failedExecutions, maxRetainedSessions);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void clear() {
        Map<Object, List<MonitoredExecution>> map = this.successfulExecutions;
        synchronized (map) {
            this.successfulExecutions.clear();
        }
        map = this.failedExecutions;
        synchronized (map) {
            this.failedExecutions.clear();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void trimToSize(Map<Object, ?> map, int maxSize) {
        Map<Object, ?> map2 = map;
        synchronized (map2) {
            Iterator<Map.Entry<Object, ?>> it = map.entrySet().iterator();
            while (map.size() > maxSize && it.hasNext()) {
                it.next();
                it.remove();
            }
        }
    }

    private Map<Object, List<MonitoredExecution>> createBoundedMap() {
        return new LinkedHashMap<Object, List<MonitoredExecution>>(){

            @Override
            protected boolean removeEldestEntry(Map.Entry<Object, List<MonitoredExecution>> eldest) {
                return this.size() > AgentMonitor.this.maxRetainedSessions;
            }
        };
    }

    @Internal
    public void setRootAgent(AgentInstance rootAgent) {
        this.rootAgent = rootAgent;
    }

    AgentInstance rootAgent() {
        return this.rootAgent;
    }

    @Override
    public void beforeAgentInvocation(AgentRequest agentRequest) {
        MonitoredExecution candidate;
        Object memoryId = agentRequest.agenticScope().memoryId();
        MonitoredExecution existing = this.ongoingExecutions.putIfAbsent(memoryId, candidate = new MonitoredExecution(agentRequest));
        if (existing != null) {
            existing.beforeAgentInvocation(agentRequest);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void afterAgentInvocation(AgentResponse agentResponse) {
        Object memoryId = agentResponse.agenticScope().memoryId();
        MonitoredExecution execution = this.ongoingExecutions.get(memoryId);
        execution.afterAgentInvocation(agentResponse);
        if (execution.done()) {
            this.ongoingExecutions.remove(memoryId, execution);
            Map<Object, List<MonitoredExecution>> map = this.successfulExecutions;
            synchronized (map) {
                this.successfulExecutions.computeIfAbsent(memoryId, k -> new ArrayList()).add(execution);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onAgentInvocationError(AgentInvocationError agentInvocationError) {
        Object memoryId = agentInvocationError.agenticScope().memoryId();
        MonitoredExecution execution = this.ongoingExecutions.get(memoryId);
        if (execution != null) {
            execution.onAgentInvocationError(agentInvocationError);
            if (execution.ongoingInvocations().isEmpty()) {
                this.ongoingExecutions.remove(memoryId, execution);
                Map<Object, List<MonitoredExecution>> map = this.failedExecutions;
                synchronized (map) {
                    this.failedExecutions.computeIfAbsent(memoryId, k -> new ArrayList()).add(execution);
                }
            }
        }
    }

    @Override
    public void afterAgentToolExecution(AfterAgentToolExecution afterAgentToolExecution) {
        Object memoryId = afterAgentToolExecution.agenticScope().memoryId();
        MonitoredExecution execution = this.ongoingExecutions.get(memoryId);
        if (execution != null) {
            execution.afterToolExecution(afterAgentToolExecution);
        }
    }

    @Override
    public boolean inheritedBySubagents() {
        return true;
    }

    public Map<Object, MonitoredExecution> ongoingExecutions() {
        return this.ongoingExecutions;
    }

    public MonitoredExecution ongoingExecutionFor(AgenticScope agenticScope) {
        return this.ongoingExecutionFor(agenticScope.memoryId());
    }

    public MonitoredExecution ongoingExecutionFor(Object memoryId) {
        return this.ongoingExecutions.get(memoryId);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<MonitoredExecution> successfulExecutions() {
        Map<Object, List<MonitoredExecution>> map = this.successfulExecutions;
        synchronized (map) {
            return this.successfulExecutions.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        }
    }

    public List<MonitoredExecution> successfulExecutionsFor(AgenticScope agenticScope) {
        return this.successfulExecutionsFor(agenticScope.memoryId());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<MonitoredExecution> successfulExecutionsFor(Object memoryId) {
        Map<Object, List<MonitoredExecution>> map = this.successfulExecutions;
        synchronized (map) {
            return Collections.unmodifiableList(new ArrayList(this.successfulExecutions.getOrDefault(memoryId, Collections.emptyList())));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<MonitoredExecution> failedExecutions() {
        Map<Object, List<MonitoredExecution>> map = this.failedExecutions;
        synchronized (map) {
            return this.failedExecutions.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        }
    }

    public List<MonitoredExecution> failedExecutionsFor(AgenticScope agenticScope) {
        return this.failedExecutionsFor(agenticScope.memoryId());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<MonitoredExecution> failedExecutionsFor(Object memoryId) {
        Map<Object, List<MonitoredExecution>> map = this.failedExecutions;
        synchronized (map) {
            return Collections.unmodifiableList(new ArrayList(this.failedExecutions.getOrDefault(memoryId, Collections.emptyList())));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Set<Object> allMemoryIds() {
        LinkedHashSet<Object> ids = new LinkedHashSet<Object>();
        Map<Object, List<MonitoredExecution>> map = this.successfulExecutions;
        synchronized (map) {
            ids.addAll(this.successfulExecutions.keySet());
        }
        map = this.failedExecutions;
        synchronized (map) {
            ids.addAll(this.failedExecutions.keySet());
        }
        ids.addAll(this.ongoingExecutions.keySet());
        return Collections.unmodifiableSet(ids);
    }

    public List<MonitoredExecution> allExecutionsFor(AgenticScope agenticScope) {
        return this.allExecutionsFor(agenticScope.memoryId());
    }

    public List<MonitoredExecution> allExecutionsFor(Object memoryId) {
        ArrayList<MonitoredExecution> all = new ArrayList<MonitoredExecution>(this.successfulExecutionsFor(memoryId));
        all.addAll(this.failedExecutionsFor(memoryId));
        MonitoredExecution ongoing = this.ongoingExecutionFor(memoryId);
        if (ongoing != null) {
            all.add(ongoing);
        }
        return all;
    }
}

