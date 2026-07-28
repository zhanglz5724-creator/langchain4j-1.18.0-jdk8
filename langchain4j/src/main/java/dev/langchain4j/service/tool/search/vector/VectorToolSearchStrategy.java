/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.exception.ToolArgumentsException
 *  dev.langchain4j.exception.ToolExecutionException
 *  dev.langchain4j.internal.Json
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.embedding.EmbeddingModel
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 */
package dev.langchain4j.service.tool.search.vector;

import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.exception.ToolArgumentsException;
import dev.langchain4j.exception.ToolExecutionException;
import dev.langchain4j.internal.Json;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.service.tool.search.ToolSearchRequest;
import dev.langchain4j.service.tool.search.ToolSearchResult;
import dev.langchain4j.service.tool.search.ToolSearchStrategy;
import dev.langchain4j.service.tool.search.vector.ToolCachingEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Experimental
public class VectorToolSearchStrategy
implements ToolSearchStrategy {
    private static final String DEFAULT_TOOL_NAME = "tool_search_tool";
    private static final String DEFAULT_TOOL_DESCRIPTION = "Finds available tools using semantic vector search";
    private static final String DEFAULT_TOOL_ARGUMENT_NAME = "query";
    private static final String DEFAULT_TOOL_ARGUMENT_DESCRIPTION = "Natural language query describing desired tool";
    private static final int DEFAULT_MAX_RESULTS = 5;
    private static final double DEFAULT_MIN_SCORE = 0.0;
    private static final Function<List<String>, String> DEFAULT_TOOL_RESULT_MESSAGE_TEXT_PROVIDER = foundToolNames -> {
        if (foundToolNames.isEmpty()) {
            return "No matching tools found";
        }
        return "Tools found: " + String.join((CharSequence)", ", foundToolNames);
    };
    private static final String METADATA_TOOL_NAME = "toolName";
    private final ToolSpecification toolSearchTool;
    private final EmbeddingModel embeddingModel;
    private final int maxResults;
    private final double minScore;
    private final String toolArgumentName;
    private final boolean throwToolArgumentsExceptions;
    private final Function<List<String>, String> toolResultMessageTextProvider;

    public VectorToolSearchStrategy(EmbeddingModel embeddingModel) {
        this(VectorToolSearchStrategy.builder().embeddingModel(embeddingModel));
    }

    public VectorToolSearchStrategy(Builder builder) {
        Boolean cacheEmbeddings = (Boolean)Utils.getOrDefault((Object)builder.cacheEmbeddings, (Object)true);
        this.embeddingModel = cacheEmbeddings != false ? (EmbeddingModel)ValidationUtils.ensureNotNull((Object)new ToolCachingEmbeddingModel(builder.embeddingModel), (String)"embeddingModel") : (EmbeddingModel)ValidationUtils.ensureNotNull((Object)builder.embeddingModel, (String)"embeddingModel");
        this.toolArgumentName = (String)Utils.getOrDefault((Object)builder.toolArgumentName, (Object)DEFAULT_TOOL_ARGUMENT_NAME);
        this.toolSearchTool = ToolSpecification.builder().name((String)Utils.getOrDefault((Object)builder.toolName, (Object)DEFAULT_TOOL_NAME)).description((String)Utils.getOrDefault((Object)builder.toolDescription, (Object)DEFAULT_TOOL_DESCRIPTION)).parameters(JsonObjectSchema.builder().addStringProperty(this.toolArgumentName, (String)Utils.getOrDefault((Object)builder.toolArgumentDescription, (Object)DEFAULT_TOOL_ARGUMENT_DESCRIPTION)).required(new String[]{this.toolArgumentName}).build()).build();
        this.maxResults = (Integer)Utils.getOrDefault((Object)builder.maxResults, (Object)5);
        this.minScore = (Double)Utils.getOrDefault((Object)builder.minScore, (Object)0.0);
        this.throwToolArgumentsExceptions = (Boolean)Utils.getOrDefault((Object)builder.throwToolArgumentsExceptions, (Object)false);
        this.toolResultMessageTextProvider = (Function)Utils.getOrDefault((Object)builder.toolResultMessageTextProvider, DEFAULT_TOOL_RESULT_MESSAGE_TEXT_PROVIDER);
    }

    @Override
    public List<ToolSpecification> getToolSearchTools(InvocationContext context) {
        return Collections.singletonList(this.toolSearchTool);
    }

    @Override
    public ToolSearchResult search(ToolSearchRequest request) {
        String query = this.extractQuery(request.toolExecutionRequest().arguments());
        ArrayList<TextSegment> segments = new ArrayList<TextSegment>();
        segments.add(TextSegment.from((String)query));
        request.searchableTools().stream().map(tool -> {
            String text = this.format((ToolSpecification)tool);
            Metadata metadata = Metadata.from((String)METADATA_TOOL_NAME, (String)tool.name());
            return TextSegment.from((String)text, (Metadata)metadata);
        }).forEach(segments::add);
        List embeddings = (List)this.embeddingModel.embedAll(segments).content();
        InMemoryEmbeddingStore store = new InMemoryEmbeddingStore();
        store.addAll(embeddings.subList(1, embeddings.size()), segments.subList(1, segments.size()));
        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder().query(query).queryEmbedding((Embedding)embeddings.get(0)).maxResults(Integer.valueOf(this.maxResults)).minScore(Double.valueOf(this.minScore)).build();
        EmbeddingSearchResult searchResult = store.search(embeddingSearchRequest);
        List<String> toolNames = searchResult.matches().stream().map(match -> ((TextSegment)match.embedded()).metadata().getString(METADATA_TOOL_NAME)).collect(Collectors.toList());
        String toolResultMessageText = this.toolResultMessageTextProvider.apply(toolNames);
        return new ToolSearchResult(toolNames, toolResultMessageText);
    }

    private String extractQuery(String argumentsJson) {
        Map<String, Object> map = this.parseMap(argumentsJson);
        if (Utils.isNullOrEmpty(map) || !map.containsKey(this.toolArgumentName)) {
            String message = String.format("Missing required tool argument '%s'", this.toolArgumentName);
            this.throwException(message, null);
        }
        return map.get(this.toolArgumentName).toString();
    }

    private Map<String, Object> parseMap(String json) {
        try {
            return (Map)Json.fromJson((String)json, Map.class);
        }
        catch (Exception e) {
            String message = String.format("Failed to parse tool search arguments: '%s' (base64: '%s')", json, Utils.toBase64((String)json));
            this.throwException(message, e);
            return null;
        }
    }

    protected String format(ToolSpecification tool) {
        if (Utils.isNullOrBlank((String)tool.description())) {
            return tool.name();
        }
        return tool.name() + ": " + tool.description();
    }

    private void throwException(String message, Exception e) {
        if (this.throwToolArgumentsExceptions) {
            throw e == null ? new ToolArgumentsException(message) : new ToolArgumentsException(message, (Throwable)e);
        }
        throw e == null ? new ToolExecutionException(message) : new ToolExecutionException(message, (Throwable)e);
    }

    public void clearEmbeddingsCache() {
        if (!(this.embeddingModel instanceof ToolCachingEmbeddingModel)) {
            throw new IllegalStateException("Not caching embeddings, nothing to clear");
        }
        ToolCachingEmbeddingModel cachingEmbeddingModel = (ToolCachingEmbeddingModel)this.embeddingModel;
        cachingEmbeddingModel.clearCache();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private EmbeddingModel embeddingModel;
        private Integer maxResults;
        private Double minScore;
        private String toolName;
        private String toolDescription;
        private String toolArgumentName;
        private String toolArgumentDescription;
        private Boolean throwToolArgumentsExceptions;
        private Boolean cacheEmbeddings;
        private Function<List<String>, String> toolResultMessageTextProvider;

        public Builder embeddingModel(EmbeddingModel embeddingModel) {
            this.embeddingModel = embeddingModel;
            return this;
        }

        public Builder maxResults(Integer maxResults) {
            this.maxResults = maxResults;
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

        public Builder cacheEmbeddings(Boolean cacheEmbeddings) {
            this.cacheEmbeddings = cacheEmbeddings;
            return this;
        }

        public Builder toolResultMessageTextProvider(Function<List<String>, String> toolResultMessageTextProvider) {
            this.toolResultMessageTextProvider = toolResultMessageTextProvider;
            return this;
        }

        public VectorToolSearchStrategy build() {
            return new VectorToolSearchStrategy(this);
        }
    }
}

