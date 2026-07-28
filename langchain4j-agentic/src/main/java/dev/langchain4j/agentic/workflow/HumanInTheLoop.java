/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.workflow;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.internal.AgentSpecsProvider;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.scope.AgenticScope;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class HumanInTheLoop
implements AgentSpecsProvider {
    private final String outputKey;
    private final String description;
    private final boolean async;
    private final Function<AgenticScope, ?> responseProvider;
    private final AgentListener listener;
    private final List<AgentArgument> arguments;

    public HumanInTheLoop(String outputKey, String description, boolean async, Function<AgenticScope, ?> responseProvider, AgentListener listener, List<AgentArgument> arguments) {
        this.outputKey = outputKey;
        this.description = description;
        this.async = async;
        this.responseProvider = responseProvider;
        this.listener = listener;
        this.arguments = arguments;
    }

    @Override
    public String outputKey() {
        return this.outputKey;
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public boolean async() {
        return this.async;
    }

    public Function<AgenticScope, ?> responseProvider() {
        return this.responseProvider;
    }

    @Override
    public AgentListener listener() {
        return this.listener;
    }

    @Override
    public List<AgentArgument> arguments() {
        return this.arguments;
    }

    @Agent(value="An agent that asks the user for missing information")
    public Object askUser(AgenticScope scope) {
        return this.responseProvider.apply(scope);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HumanInTheLoop)) {
            return false;
        }
        HumanInTheLoop other = (HumanInTheLoop)o;
        if (this.async != other.async) {
            return false;
        }
        if (!Objects.equals(this.outputKey, other.outputKey)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.responseProvider, other.responseProvider)) {
            return false;
        }
        if (!Objects.equals(this.listener, other.listener)) {
            return false;
        }
        return Objects.equals(this.arguments, other.arguments);
    }

    public int hashCode() {
        return Objects.hash(this.outputKey, this.description, this.async, this.responseProvider, this.listener, this.arguments);
    }

    public String toString() {
        return "HumanInTheLoop{outputKey=" + this.outputKey + ", description=" + this.description + ", async=" + this.async + ", responseProvider=" + this.responseProvider + ", listener=" + this.listener + ", arguments=" + this.arguments + "}";
    }

    public static class HumanInTheLoopBuilder {
        private String outputKey = "response";
        private String description = "An agent that asks the user for missing information";
        private boolean async = false;
        private Function<AgenticScope, ?> responseProvider;
        private AgentListener agentListener;
        private List<AgentArgument> arguments;

        public HumanInTheLoopBuilder responseProvider(Supplier<?> responseProvider) {
            return this.responseProvider((AgenticScope scope) -> responseProvider.get());
        }

        public HumanInTheLoopBuilder responseProvider(Function<AgenticScope, ?> responseProvider) {
            this.responseProvider = responseProvider;
            return this;
        }

        public HumanInTheLoopBuilder outputKey(String outputKey) {
            this.outputKey = outputKey;
            return this;
        }

        public HumanInTheLoopBuilder description(String description) {
            this.description = description;
            return this;
        }

        public HumanInTheLoopBuilder async(boolean async) {
            this.async = async;
            return this;
        }

        public HumanInTheLoopBuilder inputs(List<AgentArgument> inputs) {
            this.arguments = inputs;
            return this;
        }

        public HumanInTheLoopBuilder inputKey(Class<?> type, String name) {
            this.arguments = Arrays.asList(new AgentArgument(type, name));
            return this;
        }

        public HumanInTheLoopBuilder inputKeys(Class<?> type1, String name1, Class<?> type2, String name2) {
            this.arguments = Arrays.asList(new AgentArgument(type1, name1), new AgentArgument(type2, name2));
            return this;
        }

        public HumanInTheLoopBuilder inputKeys(Class<?> type1, String name1, Class<?> type2, String name2, Class<?> type3, String name3) {
            this.arguments = Arrays.asList(new AgentArgument(type1, name1), new AgentArgument(type2, name2), new AgentArgument(type3, name3));
            return this;
        }

        public HumanInTheLoopBuilder inputKeys(Class<?> type1, String name1, Class<?> type2, String name2, Class<?> type3, String name3, Class<?> type4, String name4) {
            this.arguments = Arrays.asList(new AgentArgument(type1, name1), new AgentArgument(type2, name2), new AgentArgument(type3, name3), new AgentArgument(type4, name4));
            return this;
        }

        public HumanInTheLoopBuilder listener(AgentListener agentListener) {
            this.agentListener = agentListener;
            return this;
        }

        public HumanInTheLoop build() {
            return new HumanInTheLoop(this.outputKey, this.description, this.async, this.responseProvider, this.agentListener, this.arguments);
        }
    }
}

