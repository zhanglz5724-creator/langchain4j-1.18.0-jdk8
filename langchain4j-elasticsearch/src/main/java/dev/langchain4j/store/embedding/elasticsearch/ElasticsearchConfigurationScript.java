/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  co.elastic.clients.elasticsearch.ElasticsearchClient
 *  co.elastic.clients.elasticsearch._types.ElasticsearchException
 *  co.elastic.clients.elasticsearch._types.query_dsl.Query
 *  co.elastic.clients.elasticsearch._types.query_dsl.ScriptScoreQuery
 *  co.elastic.clients.elasticsearch.core.SearchRequest
 *  co.elastic.clients.elasticsearch.core.SearchResponse
 *  co.elastic.clients.elasticsearch.core.search.SourceConfig$Builder
 *  co.elastic.clients.json.JsonData
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.filter.Filter
 */
package dev.langchain4j.store.embedding.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.ScriptScoreQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.elasticsearch.Document;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchConfiguration;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchMetadataFilterMapper;
import dev.langchain4j.store.embedding.filter.Filter;
import java.io.IOException;

public class ElasticsearchConfigurationScript
implements ElasticsearchConfiguration {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final boolean includeVectorResponse;

    public static Builder builder() {
        return new Builder();
    }

    private ElasticsearchConfigurationScript(boolean includeVectorResponse) {
        this.includeVectorResponse = includeVectorResponse;
    }

    @Override
    public boolean isIncludeVectorResponse() {
        return this.includeVectorResponse;
    }

    @Override
    public SearchResponse<Document> vectorSearch(ElasticsearchClient client, String indexName, EmbeddingSearchRequest embeddingSearchRequest) throws ElasticsearchException, IOException {
        ScriptScoreQuery scriptScoreQuery = this.buildDefaultScriptScoreQuery(embeddingSearchRequest.queryEmbedding().vector(), (float)embeddingSearchRequest.minScore(), embeddingSearchRequest.filter());
        return client.search(SearchRequest.of(s -> s.source(sr -> {
            if (this.includeVectorResponse) {
                return sr.filter(f -> f.excludeVectors(Boolean.valueOf(false)));
            }
            return new SourceConfig.Builder().filter(f -> f);
        }).index(indexName, new String[0]).query(n -> n.scriptScore(scriptScoreQuery)).size(Integer.valueOf(embeddingSearchRequest.maxResults()))), Document.class);
    }

    private ScriptScoreQuery buildDefaultScriptScoreQuery(float[] vector, float minScore, Filter filter) throws JsonProcessingException {
        JsonData queryVector = this.toJsonData(vector);
        Query query = filter == null ? Query.of(q -> q.matchAll(m -> m)) : ElasticsearchMetadataFilterMapper.map(filter);
        return ScriptScoreQuery.of(q -> q.minScore(Float.valueOf(minScore)).query(query).script(s -> s.source(sb -> sb.scriptString("(cosineSimilarity(params.query_vector, 'vector') + 1.0) / 2")).params("query_vector", queryVector)));
    }

    private <T> JsonData toJsonData(T rawData) throws JsonProcessingException {
        return JsonData.fromJson((String)this.objectMapper.writeValueAsString(rawData));
    }

    public static class Builder {
        private boolean includeVectorResponse = false;

        public Builder includeVectorResponse(boolean includeVectorResponse) {
            this.includeVectorResponse = includeVectorResponse;
            return this;
        }

        public ElasticsearchConfigurationScript build() {
            return new ElasticsearchConfigurationScript(this.includeVectorResponse);
        }
    }
}

