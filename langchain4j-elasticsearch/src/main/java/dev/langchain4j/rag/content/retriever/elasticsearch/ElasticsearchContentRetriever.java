/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  co.elastic.clients.elasticsearch.ElasticsearchClient
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.model.embedding.EmbeddingModel
 *  dev.langchain4j.rag.content.Content
 *  dev.langchain4j.rag.content.ContentMetadata
 *  dev.langchain4j.rag.content.retriever.ContentRetriever
 *  dev.langchain4j.rag.query.Query
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.filter.Filter
 *  org.elasticsearch.client.RestClient
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.rag.content.retriever.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.ContentMetadata;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.elasticsearch.AbstractElasticsearchEmbeddingStore;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchConfiguration;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchConfigurationFullText;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchConfigurationHybrid;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchConfigurationKnn;
import dev.langchain4j.store.embedding.filter.Filter;
import java.util.List;
import java.util.Map;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticsearchContentRetriever
extends AbstractElasticsearchEmbeddingStore
implements ContentRetriever {
    private static final Logger log = LoggerFactory.getLogger(ElasticsearchContentRetriever.class);
    private final EmbeddingModel embeddingModel;
    private final int maxResults;
    private final double minScore;
    private final Filter filter;

    @Deprecated(forRemoval=true)
    public ElasticsearchContentRetriever(ElasticsearchConfiguration configuration, RestClient restClient, String indexName, EmbeddingModel embeddingModel, int maxResults, double minScore, Filter filter) {
        this.embeddingModel = embeddingModel;
        this.maxResults = maxResults;
        this.minScore = minScore;
        this.filter = filter;
        this.initialize(configuration, restClient, indexName);
    }

    public ElasticsearchContentRetriever(ElasticsearchConfiguration configuration, ElasticsearchClient client, String indexName, EmbeddingModel embeddingModel, int maxResults, double minScore, Filter filter) {
        this.embeddingModel = embeddingModel;
        this.maxResults = maxResults;
        this.minScore = minScore;
        this.filter = filter;
        this.initialize(configuration, client, indexName);
    }

    public List<Content> retrieve(Query query) {
        if (this.configuration instanceof ElasticsearchConfigurationFullText) {
            log.debug("Using a full text search query");
            return this.fullTextSearch(query.text()).stream().map(t -> Content.from((TextSegment)t, (Map)Map.of((Object)ContentMetadata.SCORE, (Object)t.metadata().getDouble(ContentMetadata.SCORE.name()), (Object)ContentMetadata.EMBEDDING_ID, (Object)t.metadata().getString(ContentMetadata.EMBEDDING_ID.name())))).toList();
        }
        Embedding referenceEmbedding = (Embedding)this.embeddingModel.embed(query.text()).content();
        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder().queryEmbedding(referenceEmbedding).maxResults(Integer.valueOf(this.maxResults)).minScore(Double.valueOf(this.minScore)).filter(this.filter).build();
        if (this.configuration instanceof ElasticsearchConfigurationHybrid) {
            return this.mapResultsToContentList(this.hybridSearch(request, query.text()));
        }
        return this.mapResultsToContentList(this.search(request));
    }

    private List<Content> mapResultsToContentList(EmbeddingSearchResult<TextSegment> searchResult) {
        List result = searchResult.matches().stream().filter(f -> f.score() >= this.minScore).map(m -> Content.from((TextSegment)((TextSegment)m.embedded()), (Map)Map.of((Object)ContentMetadata.SCORE, (Object)m.score(), (Object)ContentMetadata.EMBEDDING_ID, (Object)m.embeddingId()))).toList();
        log.debug("Found [{}] relevant documents in Elasticsearch index [{}].", (Object)result.size(), (Object)this.indexName);
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private RestClient restClient;
        private ElasticsearchClient client;
        private String indexName = "default";
        private ElasticsearchConfiguration configuration = ElasticsearchConfigurationKnn.builder().build();
        private EmbeddingModel embeddingModel;
        private int maxResults;
        private double minScore;
        private Filter filter;

        @Deprecated(forRemoval=true)
        public Builder restClient(RestClient restClient) {
            this.restClient = restClient;
            return this;
        }

        public Builder client(ElasticsearchClient client) {
            this.client = client;
            return this;
        }

        public Builder indexName(String indexName) {
            this.indexName = indexName;
            return this;
        }

        public Builder configuration(ElasticsearchConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }

        public Builder embeddingModel(EmbeddingModel embeddingModel) {
            this.embeddingModel = embeddingModel;
            return this;
        }

        public Builder maxResults(int maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public Builder minScore(double minScore) {
            this.minScore = minScore;
            return this;
        }

        public Builder filter(Filter filter) {
            this.filter = filter;
            return this;
        }

        public ElasticsearchContentRetriever build() {
            if (this.client != null) {
                return new ElasticsearchContentRetriever(this.configuration, this.client, this.indexName, this.embeddingModel, this.maxResults, this.minScore, this.filter);
            }
            log.warn("Using RestClient is deprecated and will be removed in future versions. Please use Elasticsearch Client instead (see client() method).");
            return new ElasticsearchContentRetriever(this.configuration, this.restClient, this.indexName, this.embeddingModel, this.maxResults, this.minScore, this.filter);
        }
    }
}

