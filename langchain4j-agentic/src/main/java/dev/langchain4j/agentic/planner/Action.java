/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.planner;

import dev.langchain4j.agentic.internal.AgentExecutor;
import dev.langchain4j.agentic.planner.AgentInstance;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Action {
    public boolean isDone();

    default public boolean isSuspended() {
        return false;
    }

    default public Object result() {
        return null;
    }

    public static class SuspendAction
    implements Action {
        static final Action INSTANCE = new SuspendAction();

        public String toString() {
            return "SuspendAction";
        }

        @Override
        public boolean isDone() {
            return true;
        }

        @Override
        public boolean isSuspended() {
            return true;
        }
    }

    public static class NoOpAction
    extends AgentCallAction {
        static final Action INSTANCE = new NoOpAction();

        public NoOpAction() {
            super(new AgentInstance[0]);
        }
    }

    public static class AgentCallAction
    implements Action {
        private final List<AgentExecutor> agents;

        public AgentCallAction(AgentInstance ... agents) {
            this(Stream.of(agents).map(AgentExecutor.class::cast).collect(Collectors.toList()));
        }

        public AgentCallAction(List<AgentExecutor> agents) {
            this.agents = agents;
        }

        public String toString() {
            return "AgentCallAction";
        }

        @Override
        public boolean isDone() {
            return false;
        }

        public List<AgentExecutor> agentsToCall() {
            return this.agents;
        }
    }

    public static class DoneWithResultAction
    implements Action {
        private final Object result;

        public DoneWithResultAction(Object result) {
            this.result = result;
        }

        public String toString() {
            return "DoneAction[" + this.result + "]";
        }

        @Override
        public boolean isDone() {
            return true;
        }

        @Override
        public Object result() {
            return this.result;
        }
    }

    public static class DoneAction
    implements Action {
        static final Action INSTANCE = new DoneAction();

        public String toString() {
            return "DoneAction";
        }

        @Override
        public boolean isDone() {
            return true;
        }
    }
}

