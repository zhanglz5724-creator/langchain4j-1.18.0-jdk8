/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.planner;

import dev.langchain4j.agentic.planner.AgentInstance;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public interface AgentsRegistry {
    public Map<String, AgentInstance> allAgents();

    public AgentInstance getAgent(String var1);

    public static AgentsRegistry get() {
        return LazyHolder.INSTANCE;
    }

    public static void refresh() {
        LazyHolder.INSTANCE = LazyHolder.discover();
    }

    public static class EmptyAgentsRegistry
    implements AgentsRegistry {
        private EmptyAgentsRegistry() {
        }

        @Override
        public Map<String, AgentInstance> allAgents() {
            return Collections.emptyMap();
        }

        @Override
        public AgentInstance getAgent(String name) {
            throw new RuntimeException("No agent found with name: " + name);
        }
    }

    public static class CompositeAgentsRegistry
    implements AgentsRegistry {
        private final Map<String, AgentInstance> mergedAgents = new HashMap<String, AgentInstance>();

        CompositeAgentsRegistry(List<AgentsRegistry> registries) {
            for (AgentsRegistry registry : registries) {
                for (Map.Entry<String, AgentInstance> entry : registry.allAgents().entrySet()) {
                    if (this.mergedAgents.put(entry.getKey(), entry.getValue()) == null) continue;
                    throw new RuntimeException("Duplicate agent name across registries: " + entry.getKey());
                }
            }
        }

        @Override
        public Map<String, AgentInstance> allAgents() {
            return Collections.unmodifiableMap(this.mergedAgents);
        }

        @Override
        public AgentInstance getAgent(String name) {
            AgentInstance agent = this.mergedAgents.get(name);
            if (agent == null) {
                throw new RuntimeException("No agent found with name: " + name);
            }
            return agent;
        }
    }

    public static class LazyHolder {
        private static volatile AgentsRegistry INSTANCE = LazyHolder.discover();

        private LazyHolder() {
        }

        private static AgentsRegistry discover() {
            ArrayList<AgentsRegistry> registries = new ArrayList<AgentsRegistry>();
            for (AgentsRegistry registry : ServiceLoader.load(AgentsRegistry.class)) {
                registries.add(registry);
            }
            switch (registries.size()) {
                case 0: {
                    return new EmptyAgentsRegistry();
                }
                case 1: {
                    return (AgentsRegistry)registries.get(0);
                }
            }
            return new CompositeAgentsRegistry(registries);
        }
    }
}

