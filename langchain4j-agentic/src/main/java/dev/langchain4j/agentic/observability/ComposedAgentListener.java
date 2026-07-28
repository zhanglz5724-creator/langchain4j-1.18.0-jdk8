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
import dev.langchain4j.agentic.observability.BeforeAgentToolExecution;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Internal
public class ComposedAgentListener
implements AgentListener {
    private final Set<AgentListener> listeners;

    private ComposedAgentListener(List<AgentListener> listeners) {
        this.listeners = new HashSet<AgentListener>(listeners);
    }

    public ComposedAgentListener(AgentListener ... listeners) {
        this(ComposedAgentListener.collectListeners(listeners));
    }

    private static List<AgentListener> collectListeners(AgentListener ... listeners) {
        ArrayList<AgentListener> collectedListeners = new ArrayList<AgentListener>();
        for (AgentListener listener : listeners) {
            if (listener == null) continue;
            if (listener instanceof ComposedAgentListener) {
                ComposedAgentListener composed = (ComposedAgentListener)listener;
                collectedListeners.addAll(composed.listeners);
                continue;
            }
            collectedListeners.add(listener);
        }
        return collectedListeners;
    }

    public void addListener(AgentListener listener) {
        if (listener instanceof ComposedAgentListener) {
            ComposedAgentListener composed = (ComposedAgentListener)listener;
            this.listeners.addAll(composed.listeners);
        } else {
            this.listeners.add(listener);
        }
    }

    public Collection<AgentListener> listeners() {
        return this.listeners;
    }

    @Override
    public void beforeAgentInvocation(AgentRequest agentRequest) {
        for (AgentListener listener : this.listeners) {
            listener.beforeAgentInvocation(agentRequest);
        }
    }

    @Override
    public void afterAgentInvocation(AgentResponse agentResponse) {
        for (AgentListener listener : this.listeners) {
            listener.afterAgentInvocation(agentResponse);
        }
    }

    @Override
    public void onAgentInvocationError(AgentInvocationError agentInvocationError) {
        for (AgentListener listener : this.listeners) {
            listener.onAgentInvocationError(agentInvocationError);
        }
    }

    @Override
    public void afterAgentToolExecution(AfterAgentToolExecution afterAgentToolExecution) {
        for (AgentListener listener : this.listeners) {
            listener.afterAgentToolExecution(afterAgentToolExecution);
        }
    }

    @Override
    public void beforeAgentToolExecution(BeforeAgentToolExecution beforeAgentToolExecution) {
        for (AgentListener listener : this.listeners) {
            listener.beforeAgentToolExecution(beforeAgentToolExecution);
        }
    }

    @Override
    public void afterAgenticScopeCreated(AgenticScope agenticScope) {
        for (AgentListener listener : this.listeners) {
            listener.afterAgenticScopeCreated(agenticScope);
        }
    }

    @Override
    public void beforeAgenticScopeDestroyed(AgenticScope agenticScope) {
        for (AgentListener listener : this.listeners) {
            listener.beforeAgenticScopeDestroyed(agenticScope);
        }
    }

    @Override
    public void onAgenticSystemSuspended(AgenticScope agenticScope) {
        for (AgentListener listener : this.listeners) {
            listener.onAgenticSystemSuspended(agenticScope);
        }
    }

    public boolean contains(AgentListener listener) {
        if (listener instanceof ComposedAgentListener) {
            ComposedAgentListener composed = (ComposedAgentListener)listener;
            return this.listeners.containsAll(composed.listeners);
        }
        return this.listeners.contains(listener);
    }

    @Override
    public boolean inheritedBySubagents() {
        return this.listeners.stream().anyMatch(AgentListener::inheritedBySubagents);
    }

    public static AgentListener composeWithInherited(AgentListener localListener, AgentListener parentListener) {
        if (parentListener == null) {
            return localListener;
        }
        ArrayList<AgentListener> listeners = new ArrayList<AgentListener>();
        ComposedAgentListener.addInherited(listeners, parentListener);
        ComposedAgentListener.addAll(listeners, localListener);
        return ComposedAgentListener.composedListener(listeners);
    }

    private static void addAll(List<AgentListener> existingListeners, AgentListener newListener) {
        ComposedAgentListener.add(existingListeners, newListener, l -> true);
    }

    private static void addInherited(List<AgentListener> existingListeners, AgentListener newListener) {
        ComposedAgentListener.add(existingListeners, newListener, AgentListener::inheritedBySubagents);
    }

    private static void add(List<AgentListener> existingListeners, AgentListener newListener, Predicate<AgentListener> filter) {
        if (newListener == null) {
            return;
        }
        if (newListener instanceof ComposedAgentListener) {
            ComposedAgentListener composed = (ComposedAgentListener)newListener;
            existingListeners.addAll(composed.listeners.stream().filter(filter).collect(Collectors.toList()));
        } else if (filter.test(newListener)) {
            existingListeners.add(newListener);
        }
    }

    private static AgentListener composedListener(List<AgentListener> inherited) {
        switch (inherited.size()) {
            case 0: {
                return null;
            }
            case 1: {
                return inherited.get(0);
            }
        }
        return new ComposedAgentListener(inherited);
    }

    public static <T extends AgentListener> T listenerOfType(AgentListener rootListener, Class<T> listenerType) {
        if (listenerType.isInstance(rootListener)) {
            return (T)rootListener;
        }
        if (rootListener instanceof ComposedAgentListener) {
            ComposedAgentListener composed = (ComposedAgentListener)rootListener;
            for (AgentListener listener : composed.listeners) {
                if (!listenerType.isInstance(listener)) continue;
                return (T)listener;
            }
        }
        return null;
    }
}

