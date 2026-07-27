/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.filter.Filter;
import java.util.Objects;

public class EmbeddingSearchRequest {
    private final String query;
    private final Embedding queryEmbedding;
    private final int maxResults;
    private final double minScore;
    private final Filter filter;

    public EmbeddingSearchRequest(Embedding queryEmbedding, Integer maxResults, Double minScore, Filter filter) {
        this(EmbeddingSearchRequest.builder().queryEmbedding(queryEmbedding).maxResults(maxResults).minScore(minScore).filter(filter));
    }

    public EmbeddingSearchRequest(EmbeddingSearchRequestBuilder builder) {
        this.query = builder.query;
        this.queryEmbedding = ValidationUtils.ensureNotNull(builder.queryEmbedding, "queryEmbedding");
        this.maxResults = ValidationUtils.ensureGreaterThanZero(Utils.getOrDefault(builder.maxResults, 3), "maxResults");
        this.minScore = ValidationUtils.ensureBetween(Utils.getOrDefault(builder.minScore, 0.0), 0.0, 1.0, "minScore");
        this.filter = builder.filter;
    }

    public String query() {
        return this.query;
    }

    public Embedding queryEmbedding() {
        return this.queryEmbedding;
    }

    public int maxResults() {
        return this.maxResults;
    }

    public double minScore() {
        return this.minScore;
    }

    public Filter filter() {
        return this.filter;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        EmbeddingSearchRequest that = (EmbeddingSearchRequest)o;
        return this.maxResults == that.maxResults && Double.compare(this.minScore, that.minScore) == 0 && Objects.equals(this.query, that.query) && Objects.equals(this.queryEmbedding, that.queryEmbedding) && Objects.equals(this.filter, that.filter);
    }

    public int hashCode() {
        return Objects.hash(this.query, this.queryEmbedding, this.maxResults, this.minScore, this.filter);
    }

    public String toString() {
        return "EmbeddingSearchRequest{query='" + this.query + '\'' + ", queryEmbedding=" + this.queryEmbedding + ", maxResults=" + this.maxResults + ", minScore=" + this.minScore + ", filter=" + this.filter + '}';
    }

    public static EmbeddingSearchRequestBuilder builder() {
        return new EmbeddingSearchRequestBuilder();
    }

    public static class EmbeddingSearchRequestBuilder {
        private String query;
        private Embedding queryEmbedding;
        private Integer maxResults;
        private Double minScore;
        private Filter filter;

        EmbeddingSearchRequestBuilder() {
        }

        public EmbeddingSearchRequestBuilder query(String query) {
            this.query = query;
            return this;
        }

        public EmbeddingSearchRequestBuilder queryEmbedding(Embedding queryEmbedding) {
            this.queryEmbedding = queryEmbedding;
            return this;
        }

        public EmbeddingSearchRequestBuilder maxResults(Integer maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public EmbeddingSearchRequestBuilder minScore(Double minScore) {
            this.minScore = minScore;
            return this;
        }

        public EmbeddingSearchRequestBuilder filter(Filter filter) {
            this.filter = filter;
            return this;
        }

        public EmbeddingSearchRequest build() {
            return new EmbeddingSearchRequest(this);
        }
    }
}

