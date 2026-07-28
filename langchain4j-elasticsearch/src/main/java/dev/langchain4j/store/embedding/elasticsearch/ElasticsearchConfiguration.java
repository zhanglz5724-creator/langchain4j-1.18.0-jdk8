/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  co.elastic.clients.elasticsearch.ElasticsearchClient
 *  co.elastic.clients.elasticsearch._types.ElasticsearchException
 *  co.elastic.clients.elasticsearch.core.SearchResponse
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 */
package dev.langchain4j.store.embedding.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.elasticsearch.Document;
import java.io.IOException;

public interface ElasticsearchConfiguration {
    public static final String VECTOR_FIELD = "vector";
    public static final String TEXT_FIELD = "text";

    default public boolean isIncludeVectorResponse() {
        return false;
    }

    default public SearchResponse<Document> vectorSearch(ElasticsearchClient client, String indexName, EmbeddingSearchRequest embeddingSearchRequest) throws ElasticsearchException, IOException {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " configuration does not support vector search");
    }

    default public SearchResponse<Document> fullTextSearch(ElasticsearchClient client, String indexName, String textQuery) throws ElasticsearchException, IOException {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " configuration does not support fulltext search");
    }

    default public SearchResponse<Document> hybridSearch(ElasticsearchClient client, String indexName, EmbeddingSearchRequest embeddingSearchRequest, String textQuery) throws ElasticsearchException, IOException {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " configuration does not support hybrid search");
    }
}

