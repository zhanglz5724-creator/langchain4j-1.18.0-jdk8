/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ReturnBehavior
 *  dev.langchain4j.agent.tool.ToolSpecification
 */
package dev.langchain4j.service.tool;

import dev.langchain4j.agent.tool.ReturnBehavior;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.service.IllegalConfigurationException;
import dev.langchain4j.service.tool.AiServiceTool;
import dev.langchain4j.service.tool.ToolExecutor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ToolProviderResult {
    private final List<AiServiceTool> tools;

    public ToolProviderResult(Builder builder) {
        this.tools = builder.buildFinalToolList();
    }

    public ToolProviderResult(List<AiServiceTool> tools) {
        this(ToolProviderResult.builder().addAll(tools));
    }

    public ToolProviderResult(Map<ToolSpecification, ToolExecutor> tools) {
        this(ToolProviderResult.builder().addAll(tools));
    }

    public List<AiServiceTool> aiServiceTools() {
        return this.tools;
    }

    @Deprecated
    public ToolSpecification toolSpecificationByName(String name) {
        for (AiServiceTool tool : this.tools) {
            if (!tool.name().equals(name)) continue;
            return tool.toolSpecification();
        }
        return null;
    }

    @Deprecated
    public ToolExecutor toolExecutorByName(String name) {
        for (AiServiceTool tool : this.tools) {
            if (!tool.name().equals(name)) continue;
            return tool.toolExecutor();
        }
        return null;
    }

    @Deprecated
    public Map<ToolSpecification, ToolExecutor> tools() {
        LinkedHashMap<ToolSpecification, ToolExecutor> result = new LinkedHashMap<ToolSpecification, ToolExecutor>(this.tools.size());
        for (AiServiceTool tool : this.tools) {
            result.put(tool.toolSpecification(), tool.toolExecutor());
        }
        return result;
    }

    @Deprecated
    public Set<String> immediateReturnToolNames() {
        return this.tools.stream().filter(tool -> tool.returnBehavior() == ReturnBehavior.IMMEDIATE).map(tool -> tool.name()).collect(Collectors.toSet());
    }

    public Builder toBuilder() {
        return ToolProviderResult.builder().addAll(this.tools);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<AiServiceTool> tools = new ArrayList<AiServiceTool>();
        private final Set<String> immediateReturnToolNames = new HashSet<String>();

        public Builder add(AiServiceTool tool) {
            this.tools.add(tool);
            return this;
        }

        public Builder add(ToolSpecification tool, ToolExecutor executor) {
            return this.add(tool, executor, ReturnBehavior.TO_LLM);
        }

        public Builder add(ToolSpecification tool, ToolExecutor executor, ReturnBehavior returnBehavior) {
            this.tools.add(AiServiceTool.builder().toolSpecification(tool).toolExecutor(executor).returnBehavior(returnBehavior).build());
            return this;
        }

        public Builder addAll(Collection<AiServiceTool> tools) {
            tools.forEach(this::add);
            return this;
        }

        public Builder addAll(Map<ToolSpecification, ToolExecutor> tools) {
            tools.forEach(this::add);
            return this;
        }

        @Deprecated
        public Builder immediateReturnToolNames(Set<String> immediateReturnToolNames) {
            if (immediateReturnToolNames != null) {
                this.immediateReturnToolNames.addAll(immediateReturnToolNames);
            }
            return this;
        }

        public ToolProviderResult build() {
            return new ToolProviderResult(this);
        }

        private List<AiServiceTool> buildFinalToolList() {
            HashMap<String, Integer> toolsByName = new HashMap<String, Integer>(this.tools.size());
            for (int i = 0; i < this.tools.size(); ++i) {
                String name = this.tools.get(i).name();
                if (toolsByName.putIfAbsent(name, i) == null) continue;
                throw new IllegalConfigurationException("Duplicated definition for tool: " + name);
            }
            for (String name : this.immediateReturnToolNames) {
                AiServiceTool existing;
                Integer idx = (Integer)toolsByName.get(name);
                if (idx == null || (existing = this.tools.get(idx)).returnBehavior() == ReturnBehavior.IMMEDIATE) continue;
                this.tools.set(idx, existing.toBuilder().returnBehavior(ReturnBehavior.IMMEDIATE).build());
            }
            return this.tools;
        }
    }
}

