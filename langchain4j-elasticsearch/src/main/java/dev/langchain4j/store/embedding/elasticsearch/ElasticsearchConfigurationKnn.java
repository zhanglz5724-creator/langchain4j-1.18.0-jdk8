/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  co.elastic.clients.elasticsearch.ElasticsearchClient
 *  co.elastic.clients.elasticsearch._types.ElasticsearchException
 *  co.elastic.clients.elasticsearch._types.KnnQuery
 *  co.elastic.clients.elasticsearch._types.KnnQuery$Builder
 *  co.elastic.clients.elasticsearch._types.query_dsl.Query
 *  co.elastic.clients.elasticsearch.core.SearchResponse
 *  co.elastic.clients.elasticsearch.core.search.SourceConfig$Builder
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.KnnQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.elasticsearch.Document;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchConfiguration;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchMetadataFilterMapper;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticsearchConfigurationKnn
implements ElasticsearchConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ElasticsearchConfigurationKnn.class);
    private final Integer numCandidates;
    private final boolean includeVectorResponse;

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean isIncludeVectorResponse() {
        return this.includeVectorResponse;
    }

    private ElasticsearchConfigurationKnn(Integer numCandidates, boolean includeVectorResponse) {
        this.numCandidates = numCandidates;
        this.includeVectorResponse = includeVectorResponse;
    }

    @Override
    public SearchResponse<Document> vectorSearch(ElasticsearchClient client, String indexName, EmbeddingSearchRequest embeddingSearchRequest) throws ElasticsearchException, IOException {
        KnnQuery.Builder krb = new KnnQuery.Builder().field("vector").queryVector(embeddingSearchRequest.queryEmbedding().vectorAsList());
        if (embeddingSearchRequest.filter() != null) {
            krb.filter(ElasticsearchMetadataFilterMapper.map(embeddingSearchRequest.filter()), new Query[0]);
        }
        if (this.numCandidates != null) {
            krb.numCandidates(this.numCandidates);
        }
        KnnQuery knn = krb.build();
        log.trace("Searching for embeddings in index [{}] with query [{}].", (Object)indexName, (Object)knn);
        return client.search(s -> s.source(sr -> {
            if (this.includeVectorResponse) {
                return sr.filter(f -> f.excludeVectors(Boolean.valueOf(false)));
            }
            return new SourceConfig.Builder().filter(f -> f);
        }).index(indexName, new String[0]).size(Integer.valueOf(embeddingSearchRequest.maxResults())).query(q -> q.knn(knn)).minScore(Double.valueOf(embeddingSearchRequest.minScore())), Document.class);
    }

    public static class Builder {
        private Integer numCandidates;
        private boolean includeVectorResponse = false;

        public ElasticsearchConfigurationKnn build() {
            return new ElasticsearchConfigurationKnn(this.numCandidates, this.includeVectorResponse);
        }

        public Builder numCandidates(Integer numCandidates) {
            this.numCandidates = numCandidates;
            return this;
        }

        public Builder includeVectorResponse(boolean includeVectorResponse) {
            this.includeVectorResponse = includeVectorResponse;
            return this;
        }
    }
}

