/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.exception.ToolArgumentsException
 *  dev.langchain4j.exception.ToolExecutionException
 *  dev.langchain4j.internal.Json
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.model.chat.request.json.JsonArraySchema
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 *  dev.langchain4j.model.chat.request.json.JsonStringSchema
 */
package dev.langchain4j.service.tool.search.simple;

import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.exception.ToolArgumentsException;
import dev.langchain4j.exception.ToolExecutionException;
import dev.langchain4j.internal.Json;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.request.json.JsonStringSchema;
import dev.langchain4j.service.tool.search.ToolSearchRequest;
import dev.langchain4j.service.tool.search.ToolSearchResult;
import dev.langchain4j.service.tool.search.ToolSearchStrategy;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Experimental
public class SimpleToolSearchStrategy
implements ToolSearchStrategy {
    private static final String DEFAULT_TOOL_NAME = "tool_search_tool";
    private static final String DEFAULT_TOOL_DESCRIPTION = "Finds available tools whose name or description contains given search terms";
    private static final String DEFAULT_TOOL_ARGUMENT_NAME = "terms";
    private static final String DEFAULT_TOOL_ARGUMENT_DESCRIPTION = "A list of individual search terms (single words) used to find relevant tools";
    private static final int DEFAULT_MAX_RESULTS = 5;
    private static final int DEFAULT_MIN_SCORE = 1;
    private static final Function<List<String>, String> DEFAULT_TOOL_RESULT_MESSAGE_TEXT_PROVIDER = foundToolNames -> {
        if (foundToolNames.isEmpty()) {
            return "No matching tools found";
        }
        return "Tools found: " + String.join((CharSequence)", ", foundToolNames);
    };
    private final ToolSpecification toolSearchTool;
    private final int maxResults;
    private final int minScore;
    private final String toolArgumentName;
    private final boolean throwToolArgumentsExceptions;
    private final Function<List<String>, String> toolResultMessageTextProvider;

    public SimpleToolSearchStrategy() {
        this(SimpleToolSearchStrategy.builder());
    }

    public SimpleToolSearchStrategy(Builder builder) {
        this.toolArgumentName = (String)Utils.getOrDefault((Object)builder.toolArgumentName, (Object)DEFAULT_TOOL_ARGUMENT_NAME);
        this.toolSearchTool = ToolSpecification.builder().name((String)Utils.getOrDefault((Object)builder.toolName, (Object)DEFAULT_TOOL_NAME)).description((String)Utils.getOrDefault((Object)builder.toolDescription, (Object)DEFAULT_TOOL_DESCRIPTION)).parameters(JsonObjectSchema.builder().addProperty(this.toolArgumentName, (JsonSchemaElement)JsonArraySchema.builder().description((String)Utils.getOrDefault((Object)builder.toolArgumentDescription, (Object)DEFAULT_TOOL_ARGUMENT_DESCRIPTION)).items((JsonSchemaElement)new JsonStringSchema()).build()).required(new String[]{this.toolArgumentName}).build()).build();
        this.maxResults = (Integer)Utils.getOrDefault((Object)builder.maxResults, (Object)5);
        this.minScore = (Integer)Utils.getOrDefault((Object)builder.minScore, (Object)1);
        this.throwToolArgumentsExceptions = (Boolean)Utils.getOrDefault((Object)builder.throwToolArgumentsExceptions, (Object)false);
        this.toolResultMessageTextProvider = (Function)Utils.getOrDefault((Object)builder.toolResultMessageTextProvider, DEFAULT_TOOL_RESULT_MESSAGE_TEXT_PROVIDER);
    }

    @Override
    public List<ToolSpecification> getToolSearchTools(InvocationContext context) {
        return Collections.singletonList(this.toolSearchTool);
    }

    @Override
    public ToolSearchResult search(ToolSearchRequest request) {
        List<String> terms = this.extractTerms(request.toolExecutionRequest().arguments());
        List scoredTools = request.searchableTools().stream().map(tool -> new ScoredTool((ToolSpecification)tool, this.score((ToolSpecification)tool, terms))).filter(scoredTool -> scoredTool.score >= this.minScore).sorted(Comparator.comparingInt(st -> st.score).reversed()).limit(this.maxResults).collect(Collectors.toList());
        List<String> toolNames = scoredTools.stream().map(st -> st.tool.name()).collect(Collectors.toList());
        String toolResultMessageText = this.toolResultMessageTextProvider.apply(toolNames);
        return new ToolSearchResult(toolNames, toolResultMessageText);
    }

    protected int score(ToolSpecification tool, List<String> terms) {
        List<String> cleanedTerms = this.clean(terms);
        String name = SimpleToolSearchStrategy.lower(tool.name());
        String description = SimpleToolSearchStrategy.lower(tool.description());
        int score = 0;
        for (String term : cleanedTerms) {
            if (name.contains(term)) {
                score += 2;
            }
            if (description == null || !description.contains(term)) continue;
            ++score;
        }
        return score;
    }

    protected List<String> clean(List<String> terms) {
        return terms.stream().flatMap(term -> Arrays.stream(term.split("\\s+"))).map(String::trim).filter(s -> !s.isEmpty()).map(String::toLowerCase).distinct().collect(Collectors.toList());
    }

    protected List<String> extractTerms(String argumentsJson) {
        Object value;
        Map<String, Object> map = this.parseMap(argumentsJson);
        if (Utils.isNullOrEmpty(map) || !map.containsKey(this.toolArgumentName)) {
            String message = String.format("Missing required tool argument '%s'", this.toolArgumentName);
            this.throwArgumentException(message, null);
        }
        if ((value = map.get(this.toolArgumentName)) instanceof List) {
            List list = (List)value;
            return list.stream().filter(Objects::nonNull).map(Object::toString).collect(Collectors.toList());
        }
        String message = String.format("Tool argument '%s' must be an array of strings", this.toolArgumentName);
        this.throwArgumentException(message, null);
        return null;
    }

    private Map<String, Object> parseMap(String json) {
        try {
            return (Map)Json.fromJson((String)json, Map.class);
        }
        catch (Exception e) {
            String message = String.format("Failed to parse tool search arguments: '%s' (base64: '%s')", json, Utils.toBase64((String)json));
            this.throwArgumentException(message, e);
            return null;
        }
    }

    private void throwArgumentException(String message, Exception e) {
        if (this.throwToolArgumentsExceptions) {
            if (e == null) {
                throw new ToolArgumentsException(message);
            }
            throw new ToolArgumentsException(message, (Throwable)e);
        }
        if (e == null) {
            throw new ToolExecutionException(message);
        }
        throw new ToolExecutionException(message, (Throwable)e);
    }

    private static String lower(String value) {
        return value == null ? null : value.toLowerCase();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer maxResults;
        private Integer minScore;
        private String toolName;
        private String toolDescription;
        private String toolArgumentName;
        private String toolArgumentDescription;
        private Boolean throwToolArgumentsExceptions;
        private Function<List<String>, String> toolResultMessageTextProvider;

        public Builder maxResults(Integer maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public Builder minScore(Integer minScore) {
            this.minScore = minScore;
            return this;
        }

        public Builder toolName(String toolName) {
            this.toolName = toolName;
            return this;
        }

        public Builder toolDescription(String toolDescription) {
            this.toolDescription = toolDescription;
            return this;
        }

        public Builder toolArgumentName(String toolArgumentName) {
            this.toolArgumentName = toolArgumentName;
            return this;
        }

        public Builder toolArgumentDescription(String toolArgumentDescription) {
            this.toolArgumentDescription = toolArgumentDescription;
            return this;
        }

        public Builder throwToolArgumentsExceptions(Boolean throwToolArgumentsExceptions) {
            this.throwToolArgumentsExceptions = throwToolArgumentsExceptions;
            return this;
        }

        public Builder toolResultMessageTextProvider(Function<List<String>, String> toolResultMessageTextProvider) {
            this.toolResultMessageTextProvider = toolResultMessageTextProvider;
            return this;
        }

        public SimpleToolSearchStrategy build() {
            return new SimpleToolSearchStrategy(this);
        }
    }

    private static final class ScoredTool {
        final ToolSpecification tool;
        final int score;

        ScoredTool(ToolSpecification tool, int score) {
            this.tool = tool;
            this.score = score;
        }
    }
}

