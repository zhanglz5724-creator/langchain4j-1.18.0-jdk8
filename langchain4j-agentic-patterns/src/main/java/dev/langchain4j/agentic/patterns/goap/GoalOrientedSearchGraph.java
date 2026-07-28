/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agentic.planner.AgentArgument
 *  dev.langchain4j.agentic.planner.AgentInstance
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.agentic.patterns.goap;

import dev.langchain4j.agentic.patterns.goap.DependencyGraphSearch;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgentInstance;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoalOrientedSearchGraph {
    private static final Logger LOG = LoggerFactory.getLogger(GoalOrientedSearchGraph.class);
    private final Map<String, DependencyGraphSearch.Node> nodes = new HashMap<String, DependencyGraphSearch.Node>();
    private final Map<NodePair, AgentInstance> edges = new HashMap<NodePair, AgentInstance>();

    public GoalOrientedSearchGraph(List<AgentInstance> agents) {
        this.init(agents);
    }

    private void init(List<AgentInstance> agents) {
        for (AgentInstance agent : agents) {
            List<DependencyGraphSearch.Node> inputs = agent.arguments().stream().map(AgentArgument::name).map(arg -> this.nodes.computeIfAbsent((String)arg, DependencyGraphSearch.Node::new)).collect(Collectors.toList());
            DependencyGraphSearch.Node output = this.nodes.computeIfAbsent(agent.outputKey(), DependencyGraphSearch.Node::new);
            inputs.forEach(input -> {
                input.addOutput(output);
                this.edges.put(new NodePair((DependencyGraphSearch.Node)input, output), agent);
            });
        }
    }

    public List<AgentInstance> search(Collection<String> preconditions, String goal) {
        List<DependencyGraphSearch.Node> nodesPath = DependencyGraphSearch.search(this.nodes.get(goal), preconditions.stream().map(this.nodes::get).filter(Objects::nonNull).collect(Collectors.toList()));
        if (nodesPath == null) {
            return Collections.emptyList();
        }
        int preconditionsCounter = 0;
        ArrayList<AgentInstance> agentsPath = new ArrayList<AgentInstance>();
        for (int i = 1; i < nodesPath.size(); ++i) {
            DependencyGraphSearch.Node output = nodesPath.get(i);
            if (preconditions.contains(output.getId())) {
                ++preconditionsCounter;
                continue;
            }
            for (int j = i - 1; j >= 0; --j) {
                AgentInstance agent = this.edges.get(new NodePair(nodesPath.get(j), output));
                if (agent == null) continue;
                agentsPath.add(agent);
                break;
            }
            if (agentsPath.size() == i - preconditionsCounter) continue;
            throw new IllegalStateException("No path found for node: " + output.getId());
        }
        LOG.info("Agents path sequence: {}", agentsPath.stream().map(AgentInstance::name).collect(Collectors.toList()));
        return agentsPath;
    }

    private class NodePair {
        private final DependencyGraphSearch.Node input;
        private final DependencyGraphSearch.Node output;

        public NodePair(DependencyGraphSearch.Node input, DependencyGraphSearch.Node output) {
            this.input = input;
            this.output = output;
        }

        public DependencyGraphSearch.Node input() {
            return this.input;
        }

        public DependencyGraphSearch.Node output() {
            return this.output;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof NodePair)) {
                return false;
            }
            NodePair other = (NodePair)o;
            if (!Objects.equals(this.input, other.input)) {
                return false;
            }
            return Objects.equals(this.output, other.output);
        }

        public int hashCode() {
            return Objects.hash(this.input, this.output);
        }

        public String toString() {
            return "NodePair{input=" + this.input + ", output=" + this.output + "}";
        }
    }
}

