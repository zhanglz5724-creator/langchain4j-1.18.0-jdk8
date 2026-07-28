/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.SearchBehavior
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.invocation.InvocationContext
 */
package dev.langchain4j.service.tool.search;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.SearchBehavior;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.service.tool.ToolExecutionResult;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.service.tool.ToolServiceContext;
import dev.langchain4j.service.tool.search.ToolSearchRequest;
import dev.langchain4j.service.tool.search.ToolSearchResult;
import dev.langchain4j.service.tool.search.ToolSearchStrategy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Internal
public class ToolSearchService {
    private static final String FOUND_TOOLS_ATTRIBUTE = "found_tools";
    private final ToolSearchStrategy strategy;

    public ToolSearchService(ToolSearchStrategy toolSearchStrategy) {
        this.strategy = (ToolSearchStrategy)ValidationUtils.ensureNotNull((Object)toolSearchStrategy, (String)"toolSearchStrategy");
    }

    public ToolServiceContext adjust(ToolServiceContext toolServiceContext, List<ChatMessage> messages, InvocationContext invocationContext) {
        List<ToolSpecification> toolSearchTools = this.strategy.getToolSearchTools(invocationContext);
        List<ToolSpecification> availableTools = toolServiceContext.availableTools();
        List<ToolSpecification> effectiveTools = this.calculateEffectiveTools(toolSearchTools, availableTools, messages);
        List<ToolSpecification> searchableTools = this.calculateSearchableTools(availableTools, effectiveTools);
        Map<String, ToolExecutor> toolSearchToolExecutors = this.createExecutors(toolSearchTools, searchableTools);
        return toolServiceContext.toBuilder().effectiveTools(effectiveTools).toolExecutors(Utils.merge((Map[])new Map[]{toolServiceContext.toolExecutors(), toolSearchToolExecutors})).build();
    }

    private List<ToolSpecification> calculateEffectiveTools(List<ToolSpecification> toolSearchTools, List<ToolSpecification> availableTools, List<ChatMessage> messages) {
        ArrayList<ToolSpecification> effectiveTools = new ArrayList<ToolSpecification>();
        availableTools.forEach(tool -> {
            if (tool.metadata().get("searchBehavior") == SearchBehavior.ALWAYS_VISIBLE) {
                effectiveTools.add((ToolSpecification)tool);
            }
        });
        effectiveTools.addAll(toolSearchTools);
        if (Utils.isNullOrEmpty(messages)) {
            return effectiveTools;
        }
        Set toolNamesFoundEarlier = messages.stream().filter(it -> it instanceof ToolExecutionResultMessage).map(it -> (ToolExecutionResultMessage)it).map(it -> it.attributes().get(FOUND_TOOLS_ATTRIBUTE)).filter(Objects::nonNull).map(it -> (List)it).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
        if (toolNamesFoundEarlier.isEmpty()) {
            return effectiveTools;
        }
        HashMap toolsByName = new HashMap(availableTools.size());
        availableTools.forEach(tool -> toolsByName.put(tool.name(), tool));
        toolNamesFoundEarlier.forEach(toolName -> effectiveTools.add((ToolSpecification)toolsByName.get(toolName)));
        return effectiveTools;
    }

    private List<ToolSpecification> calculateSearchableTools(List<ToolSpecification> availableTools, List<ToolSpecification> effectiveTools) {
        LinkedHashSet<ToolSpecification> searchableTools = new LinkedHashSet<ToolSpecification>(availableTools);
        searchableTools.removeAll(effectiveTools);
        return new ArrayList<ToolSpecification>(searchableTools);
    }

    private Map<String, ToolExecutor> createExecutors(List<ToolSpecification> toolSearchTools, List<ToolSpecification> searchableTools) {
        HashMap<String, ToolExecutor> executors = new HashMap<String, ToolExecutor>();
        for (ToolSpecification toolSearchTool : toolSearchTools) {
            executors.put(toolSearchTool.name(), new ToolSearchExecutor(searchableTools));
        }
        return executors;
    }

    public static ToolServiceContext addFoundTools(ToolServiceContext toolServiceContext, Collection<ToolExecutionResult> toolResults) {
        LinkedHashSet foundToolNames = new LinkedHashSet();
        for (ToolExecutionResult toolResult : toolResults) {
            Object attribute = toolResult.attributes().get(FOUND_TOOLS_ATTRIBUTE);
            if (!(attribute instanceof List)) continue;
            List foundToolNamesList = (List)attribute;
            foundToolNames.addAll(foundToolNamesList);
        }
        if (foundToolNames.isEmpty()) {
            return toolServiceContext;
        }
        Set effectiveToolNames = toolServiceContext.effectiveTools().stream().map(ToolSpecification::name).collect(Collectors.toSet());
        HashMap availableToolsByName = new HashMap(toolServiceContext.availableTools().size());
        toolServiceContext.availableTools().forEach(tool -> availableToolsByName.put(tool.name(), tool));
        ArrayList<ToolSpecification> foundTools = new ArrayList<ToolSpecification>();
        for (String foundToolName : foundToolNames) {
            if (effectiveToolNames.contains(foundToolName)) continue;
            ToolSpecification foundTool = (ToolSpecification)availableToolsByName.get(foundToolName);
            if (foundTool == null) {
                throw new IllegalArgumentException(String.format("No tool with name '%s' exists", foundToolName));
            }
            foundTools.add(foundTool);
        }
        if (foundTools.isEmpty()) {
            return toolServiceContext;
        }
        return toolServiceContext.toBuilder().effectiveTools(Utils.merge((List[])new List[]{toolServiceContext.effectiveTools(), foundTools})).build();
    }

    private class ToolSearchExecutor
    implements ToolExecutor {
        private final List<ToolSpecification> searchableTools;

        private ToolSearchExecutor(List<ToolSpecification> searchableTools) {
            this.searchableTools = Utils.copy(searchableTools);
        }

        @Override
        public ToolExecutionResult executeWithContext(ToolExecutionRequest request, InvocationContext context) {
            ToolSearchRequest toolSearchRequest = ToolSearchRequest.builder().toolExecutionRequest(request).searchableTools(this.searchableTools).invocationContext(context).build();
            ToolSearchResult toolSearchResult = ToolSearchService.this.strategy.search(toolSearchRequest);
            return ToolExecutionResult.builder().result(toolSearchResult).resultText(toolSearchResult.toolResultMessageText()).attributes(Collections.singletonMap(ToolSearchService.FOUND_TOOLS_ATTRIBUTE, toolSearchResult.foundToolNames())).build();
        }

        @Override
        public String execute(ToolExecutionRequest request, Object memoryId) {
            throw new IllegalStateException("executeWithContext must be called instead");
        }
    }
}

