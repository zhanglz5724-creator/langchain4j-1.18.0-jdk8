/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.core.credential.AzureKeyCredential
 *  com.azure.core.credential.TokenCredential
 *  com.azure.search.documents.indexes.models.SearchIndex
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.EmbeddingStore
 */
package dev.langchain4j.store.embedding.azure.search;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.TokenCredential;
import com.azure.search.documents.indexes.models.SearchIndex;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.rag.content.retriever.azure.search.AzureAiSearchFilterMapper;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.azure.search.AbstractAzureAiSearchEmbeddingStore;

public class AzureAiSearchEmbeddingStore
extends AbstractAzureAiSearchEmbeddingStore
implements EmbeddingStore<TextSegment> {
    public AzureAiSearchEmbeddingStore(String endpoint, AzureKeyCredential keyCredential, boolean createOrUpdateIndex, int dimensions, String indexName, AzureAiSearchFilterMapper filterMapper) {
        this.initialize(endpoint, keyCredential, null, createOrUpdateIndex, dimensions, null, indexName, filterMapper);
    }

    public AzureAiSearchEmbeddingStore(String endpoint, AzureKeyCredential keyCredential, boolean createOrUpdateIndex, SearchIndex index, String indexName, AzureAiSearchFilterMapper filterMapper) {
        this.initialize(endpoint, keyCredential, null, createOrUpdateIndex, 0, index, indexName, filterMapper);
    }

    public AzureAiSearchEmbeddingStore(String endpoint, TokenCredential tokenCredential, boolean createOrUpdateIndex, int dimensions, String indexName, AzureAiSearchFilterMapper filterMapper) {
        this.initialize(endpoint, null, tokenCredential, createOrUpdateIndex, dimensions, null, indexName, filterMapper);
    }

    public AzureAiSearchEmbeddingStore(String endpoint, TokenCredential tokenCredential, boolean createOrUpdateIndex, SearchIndex index, String indexName, AzureAiSearchFilterMapper filterMapper) {
        this.initialize(endpoint, null, tokenCredential, createOrUpdateIndex, 0, index, indexName, filterMapper);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String endpoint;
        private AzureKeyCredential keyCredential;
        private TokenCredential tokenCredential;
        private boolean createOrUpdateIndex = true;
        private int dimensions;
        private SearchIndex index;
        private String indexName;
        private AzureAiSearchFilterMapper filterMapper;

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

        public Builder createOrUpdateIndex(boolean createOrUpdateIndex) {
            this.createOrUpdateIndex = createOrUpdateIndex;
            return this;
        }

        public Builder dimensions(int dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Builder index(SearchIndex index) {
            this.index = index;
            return this;
        }

        public Builder indexName(String indexName) {
            this.indexName = indexName;
            return this;
        }

        public Builder filterMapper(AzureAiSearchFilterMapper filterMapper) {
            this.filterMapper = filterMapper;
            return this;
        }

        public AzureAiSearchEmbeddingStore build() {
            ValidationUtils.ensureNotNull((Object)this.endpoint, (String)"endpoint");
            ValidationUtils.ensureTrue((this.keyCredential != null || this.tokenCredential != null ? 1 : 0) != 0, (String)"either apiKey or tokenCredential must be set");
            ValidationUtils.ensureTrue((this.dimensions > 0 || this.index != null ? 1 : 0) != 0, (String)"either dimensions or index must be set");
            if (this.keyCredential == null) {
                if (this.index == null) {
                    return new AzureAiSearchEmbeddingStore(this.endpoint, this.tokenCredential, this.createOrUpdateIndex, this.dimensions, this.indexName, this.filterMapper);
                }
                return new AzureAiSearchEmbeddingStore(this.endpoint, this.tokenCredential, this.createOrUpdateIndex, this.index, this.indexName, this.filterMapper);
            }
            if (this.index == null) {
                return new AzureAiSearchEmbeddingStore(this.endpoint, this.keyCredential, this.createOrUpdateIndex, this.dimensions, this.indexName, this.filterMapper);
            }
            return new AzureAiSearchEmbeddingStore(this.endpoint, this.keyCredential, this.createOrUpdateIndex, this.index, this.indexName, this.filterMapper);
        }
    }
}

