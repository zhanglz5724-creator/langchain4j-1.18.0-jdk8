/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ReturnBehavior
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.service.tool;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ReturnBehavior;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.service.tool.ToolProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Internal
public class ToolServiceContext {
    private final List<ToolSpecification> effectiveTools;
    private final List<ToolSpecification> availableTools;
    private final Map<String, ToolExecutor> toolExecutors;
    private final Map<String, ReturnBehavior> returnBehaviors;
    private final List<ToolProvider> dynamicToolProviders;

    public ToolServiceContext(Builder builder) {
        this.effectiveTools = Utils.copy((List)builder.effectiveTools);
        this.availableTools = Utils.copy((List)builder.availableTools);
        this.toolExecutors = Utils.copy((Map)builder.toolExecutors);
        this.returnBehaviors = Utils.copy((Map)builder.returnBehaviors);
        this.dynamicToolProviders = Utils.copy((List)builder.dynamicToolProviders);
    }

    @Deprecated
    public ToolServiceContext(List<ToolSpecification> toolSpecifications, Map<String, ToolExecutor> toolExecutors) {
        this.effectiveTools = Utils.copy(toolSpecifications);
        this.availableTools = Utils.copy(toolSpecifications);
        this.toolExecutors = Utils.copy(toolExecutors);
        this.returnBehaviors = Collections.emptyMap();
        this.dynamicToolProviders = Collections.emptyList();
    }

    public List<ToolSpecification> effectiveTools() {
        return this.effectiveTools;
    }

    @Deprecated
    public List<ToolSpecification> toolSpecifications() {
        return this.effectiveTools;
    }

    public List<ToolSpecification> availableTools() {
        return this.availableTools;
    }

    public Map<String, ToolExecutor> toolExecutors() {
        return this.toolExecutors;
    }

    public Map<String, ReturnBehavior> returnBehaviors() {
        return this.returnBehaviors;
    }

    public ReturnBehavior returnBehavior(String toolName) {
        return this.returnBehaviors.getOrDefault(toolName, ReturnBehavior.TO_LLM);
    }

    @Deprecated
    public Set<String> immediateReturnTools() {
        return this.returnBehaviors.entrySet().stream().filter(entry -> entry.getValue() == ReturnBehavior.IMMEDIATE).map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    public List<ToolProvider> dynamicToolProviders() {
        return this.dynamicToolProviders;
    }

    public Builder toBuilder() {
        return ToolServiceContext.builder().effectiveTools(this.effectiveTools).availableTools(this.availableTools).toolExecutors(this.toolExecutors).returnBehaviors(this.returnBehaviors).dynamicToolProviders(this.dynamicToolProviders);
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ToolServiceContext that = (ToolServiceContext)o;
        return Objects.equals(this.effectiveTools, that.effectiveTools) && Objects.equals(this.availableTools, that.availableTools) && Objects.equals(this.toolExecutors, that.toolExecutors) && Objects.equals(this.returnBehaviors, that.returnBehaviors) && Objects.equals(this.dynamicToolProviders, that.dynamicToolProviders);
    }

    public int hashCode() {
        return Objects.hash(this.effectiveTools, this.availableTools, this.toolExecutors, this.returnBehaviors, this.dynamicToolProviders);
    }

    public String toString() {
        return "ToolServiceContext{effectiveTools=" + this.effectiveTools + ", availableTools=" + this.availableTools + ", toolExecutors=" + this.toolExecutors + ", returnBehaviorByName=" + this.returnBehaviors + ", dynamicToolProviders=" + this.dynamicToolProviders + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Empty
    extends ToolServiceContext {
        public static final Empty INSTANCE = new Empty();

        private Empty() {
            super(Collections.emptyList(), Collections.emptyMap());
        }
    }

    public static class Builder {
        private List<ToolSpecification> effectiveTools;
        private List<ToolSpecification> availableTools;
        private Map<String, ToolExecutor> toolExecutors;
        private Map<String, ReturnBehavior> returnBehaviors = new HashMap<String, ReturnBehavior>();
        private List<ToolProvider> dynamicToolProviders;

        public Builder effectiveTools(List<ToolSpecification> effectiveTools) {
            this.effectiveTools = effectiveTools;
            return this;
        }

        @Deprecated
        public Builder toolSpecifications(List<ToolSpecification> toolSpecifications) {
            this.effectiveTools = toolSpecifications;
            return this;
        }

        public Builder availableTools(List<ToolSpecification> availableTools) {
            this.availableTools = availableTools;
            return this;
        }

        public Builder toolExecutors(Map<String, ToolExecutor> toolExecutors) {
            this.toolExecutors = toolExecutors;
            return this;
        }

        @Deprecated
        public Builder immediateReturnTools(Set<String> immediateReturnTools) {
            if (immediateReturnTools != null) {
                immediateReturnTools.forEach(name -> this.returnBehaviors.put((String)name, ReturnBehavior.IMMEDIATE));
            }
            return this;
        }

        public Builder returnBehaviors(Map<String, ReturnBehavior> returnBehaviorByName) {
            if (returnBehaviorByName != null) {
                this.returnBehaviors.putAll(returnBehaviorByName);
            }
            return this;
        }

        public Builder dynamicToolProviders(List<ToolProvider> dynamicToolProviders) {
            this.dynamicToolProviders = dynamicToolProviders;
            return this;
        }

        public ToolServiceContext build() {
            return new ToolServiceContext(this);
        }
    }
}

