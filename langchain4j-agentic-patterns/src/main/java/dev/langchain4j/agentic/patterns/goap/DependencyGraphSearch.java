/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.patterns.goap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DependencyGraphSearch {
    public static List<Node> search(Node goal, Node ... preconditions) {
        return DependencyGraphSearch.search(goal, Stream.of(preconditions).collect(Collectors.toSet()));
    }

    public static List<Node> search(Node goal, Collection<Node> preconditions) {
        Heuristic heuristic = (state, goalNode) -> {
            if (state.activatedNodes.contains(goalNode)) {
                return 0.0;
            }
            HashSet<Node> remaining = new HashSet<Node>();
            LinkedList<Node> toCheck = new LinkedList<Node>();
            toCheck.add(goalNode);
            while (!toCheck.isEmpty()) {
                Node node = (Node)toCheck.poll();
                if (state.activatedNodes.contains(node)) continue;
                remaining.add(node);
                toCheck.addAll(node.getInputNodes());
            }
            return remaining.size();
        };
        return DependencyGraphSearch.search(preconditions, goal, heuristic);
    }

    private static List<Node> search(Collection<Node> startNodes, Node goal, Heuristic heuristic) {
        if (startNodes == null || startNodes.isEmpty()) {
            throw new IllegalArgumentException("Must provide at least one start node");
        }
        HashSet<Node> initialActivated = new HashSet<Node>(startNodes);
        SearchState initialState = new SearchState(initialActivated, startNodes.iterator().next(), 0);
        PriorityQueue<StateScore> openSet = new PriorityQueue<StateScore>();
        HashSet<SearchState> visited = new HashSet<SearchState>();
        HashMap<SearchState, SearchState> cameFrom = new HashMap<SearchState, SearchState>();
        HashMap<SearchState, Double> gScore = new HashMap<SearchState, Double>();
        gScore.put(initialState, 0.0);
        openSet.add(new StateScore(initialState, heuristic.estimate(initialState, goal)));
        while (!openSet.isEmpty()) {
            SearchState current = ((StateScore)openSet.poll()).state;
            if (visited.contains(current)) continue;
            visited.add(current);
            if (current.currentNode.equals(goal)) {
                return DependencyGraphSearch.reconstructPath(cameFrom, current);
            }
            double currentGScore = (Double)gScore.get(current);
            for (Node nextNode : DependencyGraphSearch.findActivatableNodes(current)) {
                double tentativeGScore;
                SearchState nextState = current.activateNode(nextNode);
                if (visited.contains(nextState) || !((tentativeGScore = currentGScore + 1.0) < gScore.getOrDefault(nextState, Double.POSITIVE_INFINITY))) continue;
                cameFrom.put(nextState, current);
                gScore.put(nextState, tentativeGScore);
                double fScore = tentativeGScore + heuristic.estimate(nextState, goal);
                openSet.add(new StateScore(nextState, fScore));
            }
        }
        return null;
    }

    private static Set<Node> findActivatableNodes(SearchState state) {
        HashSet<Node> activatable = new HashSet<Node>();
        for (Node activatedNode : state.activatedNodes) {
            for (Node outputNode : activatedNode.getOutputNodes()) {
                if (state.activatedNodes.contains(outputNode) || !state.canActivate(outputNode)) continue;
                activatable.add(outputNode);
            }
        }
        return activatable;
    }

    private static List<Node> reconstructPath(Map<SearchState, SearchState> cameFrom, SearchState current) {
        ArrayList<Node> path = new ArrayList<Node>();
        ArrayList<SearchState> states = new ArrayList<SearchState>();
        states.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            states.add(0, current);
        }
        Set previouslyActivated = new HashSet();
        for (SearchState state : states) {
            HashSet newNodes = new HashSet(state.activatedNodes);
            newNodes.removeAll(previouslyActivated);
            path.addAll(newNodes);
            previouslyActivated = state.activatedNodes;
        }
        return path;
    }

    static interface Heuristic {
        public double estimate(SearchState var1, Node var2);
    }

    static class StateScore {
        private final SearchState state;
        private final double fScore;

        public StateScore(SearchState state, double fScore) {
            this.state = state;
            this.fScore = fScore;
        }

        public SearchState state() {
            return this.state;
        }

        public double fScore() {
            return this.fScore;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof StateScore)) {
                return false;
            }
            StateScore other = (StateScore)o;
            if (!Objects.equals(this.state, other.state)) {
                return false;
            }
            return this.fScore == other.fScore;
        }

        public int hashCode() {
            return Objects.hash(this.state, this.fScore);
        }

        public String toString() {
            return "StateScore{state=" + this.state + ", fScore=" + this.fScore + "}";
        }
    }

    static class SearchState {
        private final Set<Node> activatedNodes;
        private final Node currentNode;
        private final int depth;

        public SearchState(Set<Node> activatedNodes, Node currentNode, int depth) {
            this.activatedNodes = activatedNodes;
            this.currentNode = currentNode;
            this.depth = depth;
        }

        public Set<Node> activatedNodes() {
            return this.activatedNodes;
        }

        public Node currentNode() {
            return this.currentNode;
        }

        public int depth() {
            return this.depth;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SearchState)) {
                return false;
            }
            SearchState other = (SearchState)o;
            if (!Objects.equals(this.activatedNodes, other.activatedNodes)) {
                return false;
            }
            if (!Objects.equals(this.currentNode, other.currentNode)) {
                return false;
            }
            return this.depth == other.depth;
        }

        public int hashCode() {
            return Objects.hash(this.activatedNodes, this.currentNode, this.depth);
        }

        public String toString() {
            return "SearchState{activatedNodes=" + this.activatedNodes + ", currentNode=" + this.currentNode + ", depth=" + this.depth + "}";
        }

        SearchState activateNode(Node node) {
            HashSet<Node> newActivated = new HashSet<Node>(this.activatedNodes);
            newActivated.add(node);
            return new SearchState(newActivated, node, this.depth + 1);
        }

        boolean canActivate(Node node) {
            return this.activatedNodes.containsAll(node.getInputNodes());
        }
    }

    public static class Node {
        private final String id;
        private final Set<Node> inputNodes = new HashSet<Node>();
        private final List<Node> outputNodes = new ArrayList<Node>();

        public Node(String id) {
            this.id = id;
        }

        private void addInput(Node input) {
            this.inputNodes.add(input);
        }

        public void addOutput(Node output) {
            this.outputNodes.add(output);
            output.addInput(this);
        }

        public Set<Node> getInputNodes() {
            return this.inputNodes;
        }

        public List<Node> getOutputNodes() {
            return this.outputNodes;
        }

        public String getId() {
            return this.id;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Node)) {
                return false;
            }
            Node node = (Node)o;
            return this.id.equals(node.id);
        }

        public int hashCode() {
            return this.id.hashCode();
        }

        public String toString() {
            return this.id;
        }
    }
}

