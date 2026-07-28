import java.util.Arrays;

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  co.elastic.clients.elasticsearch.ElasticsearchClient
 *  co.elastic.clients.elasticsearch._types.ElasticsearchException
 *  co.elastic.clients.elasticsearch._types.KnnRetriever
 *  co.elastic.clients.elasticsearch._types.KnnRetriever$Builder
 *  co.elastic.clients.elasticsearch._types.RRFRetrieverEntry
 *  co.elastic.clients.elasticsearch._types.StandardRetriever
 *  co.elastic.clients.elasticsearch._types.StandardRetriever$Builder
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
import co.elastic.clients.elasticsearch._types.KnnRetriever;
import co.elastic.clients.elasticsearch._types.RRFRetrieverEntry;
import co.elastic.clients.elasticsearch._types.StandardRetriever;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.elasticsearch.Document;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchConfiguration;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchMetadataFilterMapper;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticsearchConfigurationHybrid
implements ElasticsearchConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ElasticsearchConfigurationHybrid.class);
    private final Integer numCandidates;
    private final boolean includeVectorResponse;

    public static Builder builder() {
        return new Builder();
    }

    private ElasticsearchConfigurationHybrid(Integer numCandidates, boolean includeVectorResponse) {
        this.numCandidates = numCandidates;
        this.includeVectorResponse = includeVectorResponse;
    }

    @Override
    public boolean isIncludeVectorResponse() {
        return this.includeVectorResponse;
    }

    @Override
    public SearchResponse<Document> hybridSearch(ElasticsearchClient client, String indexName, EmbeddingSearchRequest embeddingSearchRequest, String textQuery) throws ElasticsearchException, IOException {
        KnnRetriever.Builder krb = new KnnRetriever.Builder().field("vector").queryVector(embeddingSearchRequest.queryEmbedding().vectorAsList());
        Query filter = null;
        if (embeddingSearchRequest.filter() != null) {
            filter = ElasticsearchMetadataFilterMapper.map(embeddingSearchRequest.filter());
            krb.filter(filter, new Query[0]);
        }
        if (this.numCandidates != null) {
            krb.numCandidates(this.numCandidates.intValue());
            krb.k(Math.min(this.numCandidates, embeddingSearchRequest.maxResults()));
        } else {
            krb.numCandidates(embeddingSearchRequest.maxResults());
            krb.k(embeddingSearchRequest.maxResults());
        }
        KnnRetriever knn = krb.build();
        Query matchQuery = Query.of(q -> q.match(m -> m.field("text").query(textQuery)));
        StandardRetriever.Builder srb = new StandardRetriever.Builder().query(matchQuery);
        if (filter != null) {
            srb.filter(filter, new Query[0]);
        }
        StandardRetriever standard = srb.build();
        log.trace("Searching for embeddings in index [{}] with hybrid query [{}], [{}].", new Object[]{indexName, knn, matchQuery});
        return client.search(s -> s.source(sr -> {
            if (this.includeVectorResponse) {
                return sr.filter(f -> f.excludeVectors(Boolean.valueOf(false)));
            }
            return new SourceConfig.Builder().filter(f -> f);
        }).index(indexName, new String[0]).retriever(r -> r.rrf(rf -> rf.retrievers(Arrays.asList((Object)RRFRetrieverEntry.of(rre -> rre.retriever(rt -> rt.standard(standard))), (Object)RRFRetrieverEntry.of(rre -> rre.retriever(rt -> rt.knn(knn))))))).size(Integer.valueOf(embeddingSearchRequest.maxResults())).minScore(Double.valueOf(embeddingSearchRequest.minScore())), Document.class);
    }

    public static class Builder {
        private Integer numCandidates;
        private boolean includeVectorResponse = false;

        public ElasticsearchConfigurationHybrid build() {
            return new ElasticsearchConfigurationHybrid(this.numCandidates, this.includeVectorResponse);
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

