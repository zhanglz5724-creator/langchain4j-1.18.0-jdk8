/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.core.credential.AzureKeyCredential
 *  com.azure.core.credential.TokenCredential
 *  com.azure.cosmos.models.CosmosFullTextPolicy
 *  com.azure.cosmos.models.CosmosVectorEmbeddingPolicy
 *  com.azure.cosmos.models.IndexingPolicy
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.embedding.EmbeddingModel
 *  dev.langchain4j.rag.content.Content
 *  dev.langchain4j.rag.content.ContentMetadata
 *  dev.langchain4j.rag.content.retriever.ContentRetriever
 *  dev.langchain4j.rag.query.Query
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.filter.Filter
 */
package dev.langchain4j.rag.content.retriever.azure.cosmos.nosql;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.TokenCredential;
import com.azure.cosmos.models.CosmosFullTextPolicy;
import com.azure.cosmos.models.CosmosVectorEmbeddingPolicy;
import com.azure.cosmos.models.IndexingPolicy;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.ContentMetadata;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.azure.cosmos.nosql.AbstractAzureCosmosDBNoSqlEmbeddingStore;
import dev.langchain4j.store.embedding.azure.cosmos.nosql.AzureCosmosDBNoSqlRuntimeException;
import dev.langchain4j.store.embedding.azure.cosmos.nosql.AzureCosmosDBSearchQueryType;
import dev.langchain4j.store.embedding.filter.Filter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AzureCosmosDBNoSqlContentRetriever
extends AbstractAzureCosmosDBNoSqlEmbeddingStore
implements ContentRetriever {
    private final EmbeddingModel embeddingModel;
    private final AzureCosmosDBSearchQueryType azureCosmosDBSearchQueryType;
    private final int maxResults;
    private final double minScore;
    private final Filter filter;

    public AzureCosmosDBNoSqlContentRetriever(Builder builder) {
        ValidationUtils.ensureNotNull((Object)builder.endpoint, (String)"endpoint");
        ValidationUtils.ensureTrue((builder.keyCredential != null && builder.tokenCredential == null || builder.keyCredential == null && builder.tokenCredential != null ? 1 : 0) != 0, (String)"either keyCredential or tokenCredential must be set");
        if (builder.keyCredential != null) {
            this.initialize(builder.endpoint, builder.keyCredential, null, builder.databaseName, builder.containerName, builder.partitionKeyPath, builder.indexingPolicy, builder.cosmosVectorEmbeddingPolicy, builder.cosmosFullTextPolicy, builder.vectorStoreThroughput, builder.azureCosmosDBSearchQueryType, null);
        } else {
            this.initialize(builder.endpoint, null, builder.tokenCredential, builder.databaseName, builder.containerName, builder.partitionKeyPath, builder.indexingPolicy, builder.cosmosVectorEmbeddingPolicy, builder.cosmosFullTextPolicy, builder.vectorStoreThroughput, builder.azureCosmosDBSearchQueryType, null);
        }
        this.embeddingModel = builder.embeddingModel;
        this.azureCosmosDBSearchQueryType = builder.azureCosmosDBSearchQueryType;
        this.maxResults = builder.maxResults;
        this.minScore = builder.minScore;
        this.filter = builder.filter;
    }

    @Deprecated
    public AzureCosmosDBNoSqlContentRetriever(String endpoint, AzureKeyCredential keyCredential, TokenCredential tokenCredential, EmbeddingModel embeddingModel, String databaseName, String containerName, String partitionKeyPath, IndexingPolicy indexingPolicy, CosmosVectorEmbeddingPolicy cosmosVectorEmbeddingPolicy, CosmosFullTextPolicy cosmosFullTextPolicy, Integer vectorStoreThroughput, AzureCosmosDBSearchQueryType azureCosmosDBSearchQueryType, Integer maxResults, Double minScore, Filter filter) {
        ValidationUtils.ensureNotNull((Object)endpoint, (String)"endpoint");
        ValidationUtils.ensureTrue((keyCredential != null && tokenCredential == null || keyCredential == null && tokenCredential != null ? 1 : 0) != 0, (String)"either keyCredential or tokenCredential must be set");
        if (keyCredential != null) {
            this.initialize(endpoint, keyCredential, null, databaseName, containerName, partitionKeyPath, indexingPolicy, cosmosVectorEmbeddingPolicy, cosmosFullTextPolicy, vectorStoreThroughput, azureCosmosDBSearchQueryType, null);
        } else {
            this.initialize(endpoint, null, tokenCredential, databaseName, containerName, partitionKeyPath, indexingPolicy, cosmosVectorEmbeddingPolicy, cosmosFullTextPolicy, vectorStoreThroughput, azureCosmosDBSearchQueryType, null);
        }
        this.embeddingModel = embeddingModel;
        this.azureCosmosDBSearchQueryType = azureCosmosDBSearchQueryType;
        this.maxResults = maxResults;
        this.minScore = minScore;
        this.filter = filter;
    }

    public List<Content> retrieve(Query query) {
        switch (this.azureCosmosDBSearchQueryType) {
            case VECTOR: {
                return this.retrieveWithVectorSearch(query);
            }
            case FULL_TEXT_SEARCH: {
                return this.retrieveWithFullTextSearch(query);
            }
            case FULL_TEXT_RANKING: {
                return this.retrieveWithFullTextRanking(query);
            }
            case HYBRID: {
                return this.retrieveWithHybridSearch(query);
            }
        }
        throw new AzureCosmosDBNoSqlRuntimeException("Unknown Azure AI Search Query Type: " + (Object)((Object)this.azureCosmosDBSearchQueryType));
    }

    private List<Content> retrieveWithVectorSearch(Query query) {
        Embedding referenceEmbedding = (Embedding)this.embeddingModel.embed(query.text()).content();
        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder().queryEmbedding(referenceEmbedding).maxResults(Integer.valueOf(this.maxResults)).minScore(Double.valueOf(this.minScore)).filter(this.filter).build();
        List searchResult = super.search(request).matches();
        return this.mapToContentWithScore(searchResult);
    }

    private List<Content> retrieveWithFullTextSearch(Query query) {
        String content = query.text();
        List searchResult = super.findRelevantWithFullTextSearch(content, this.maxResults, this.minScore, this.filter).matches();
        return this.mapToContent(searchResult);
    }

    private List<Content> retrieveWithFullTextRanking(Query query) {
        String content = query.text();
        List searchResult = super.findRelevantWithFullTextRanking(content, this.maxResults, this.minScore, this.filter).matches();
        return this.mapToContent(searchResult);
    }

    private List<Content> retrieveWithHybridSearch(Query query) {
        Embedding referenceEmbedding = (Embedding)this.embeddingModel.embed(query.text()).content();
        String content = query.text();
        List searchResult = super.findRelevantWithHybridSearch(referenceEmbedding, content, this.maxResults, this.minScore, this.filter).matches();
        return this.mapToContentWithScore(searchResult);
    }

    private List<Content> mapToContent(List<EmbeddingMatch<TextSegment>> searchResult) {
        return searchResult.stream().map(embeddingMatch -> Content.from((TextSegment)((TextSegment)embeddingMatch.embedded()))).collect(Collectors.toList());
    }

    private List<Content> mapToContentWithScore(List<EmbeddingMatch<TextSegment>> searchResult) {
        return searchResult.stream().map(embeddingMatch -> {
            HashMap<ContentMetadata, Double> metadata = new HashMap<ContentMetadata, Double>();
            metadata.put(ContentMetadata.SCORE, embeddingMatch.score());
            metadata.put(ContentMetadata.EMBEDDING_ID, (Double)embeddingMatch.embedding());
            return Content.from((TextSegment)((TextSegment)embeddingMatch.embedded()), metadata);
        }).collect(Collectors.toList());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String endpoint;
        private AzureKeyCredential keyCredential;
        private TokenCredential tokenCredential;
        private EmbeddingModel embeddingModel;
        private String databaseName;
        private String containerName;
        private String partitionKeyPath;
        private IndexingPolicy indexingPolicy;
        private CosmosVectorEmbeddingPolicy cosmosVectorEmbeddingPolicy;
        private CosmosFullTextPolicy cosmosFullTextPolicy;
        private Integer vectorStoreThroughput;
        private AzureCosmosDBSearchQueryType azureCosmosDBSearchQueryType;
        private Integer maxResults;
        private Double minScore;
        private Filter filter;

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.keyCredential = new AzureKeyCredential(apiKey);
            return this;
        }

        public Builder tokenCredential(TokenCredential tokenCredential) {
            this.tokenCredential = tokenCredential;
            return this;
        }

        public Builder embeddingModel(EmbeddingModel embeddingModel) {
            this.embeddingModel = embeddingModel;
            return this;
        }

        public Builder databaseName(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }

        public Builder containerName(String containerName) {
            this.containerName = containerName;
            return this;
        }

        public Builder partitionKeyPath(String partitionKeyPath) {
            this.partitionKeyPath = partitionKeyPath;
            return this;
        }

        public Builder indexingPolicy(IndexingPolicy indexingPolicy) {
            this.indexingPolicy = indexingPolicy;
            return this;
        }

        public Builder cosmosVectorEmbeddingPolicy(CosmosVectorEmbeddingPolicy cosmosVectorEmbeddingPolicy) {
            this.cosmosVectorEmbeddingPolicy = cosmosVectorEmbeddingPolicy;
            return this;
        }

        public Builder cosmosFullTextPolicy(CosmosFullTextPolicy cosmosFullTextPolicy) {
            this.cosmosFullTextPolicy = cosmosFullTextPolicy;
            return this;
        }

        public Builder vectorStoreThroughput(Integer vectorStoreThroughput) {
            this.vectorStoreThroughput = vectorStoreThroughput;
            return this;
        }

        public Builder searchQueryType(AzureCosmosDBSearchQueryType azureCosmosDBSearchQueryType) {
            this.azureCosmosDBSearchQueryType = azureCosmosDBSearchQueryType;
            return this;
        }

        public Builder maxResults(Integer maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public Builder minScore(Double minScore) {
            this.minScore = minScore;
            return this;
        }

        public Builder filter(Filter filter) {
            this.filter = filter;
            return this;
        }

        public AzureCosmosDBNoSqlContentRetriever build() {
            return new AzureCosmosDBNoSqlContentRetriever(this);
        }
    }
}

