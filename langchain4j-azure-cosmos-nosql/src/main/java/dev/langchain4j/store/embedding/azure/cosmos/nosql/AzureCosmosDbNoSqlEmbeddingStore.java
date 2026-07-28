/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.core.credential.AzureKeyCredential
 *  com.azure.core.credential.TokenCredential
 *  com.azure.cosmos.models.CosmosFullTextPolicy
 *  com.azure.cosmos.models.CosmosVectorEmbeddingPolicy
 *  com.azure.cosmos.models.IndexingPolicy
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.EmbeddingStore
 */
package dev.langchain4j.store.embedding.azure.cosmos.nosql;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.TokenCredential;
import com.azure.cosmos.models.CosmosFullTextPolicy;
import com.azure.cosmos.models.CosmosVectorEmbeddingPolicy;
import com.azure.cosmos.models.IndexingPolicy;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.rag.content.retriever.azure.cosmos.nosql.AzureCosmosDBNoSqlFilterMapper;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.azure.cosmos.nosql.AbstractAzureCosmosDBNoSqlEmbeddingStore;
import dev.langchain4j.store.embedding.azure.cosmos.nosql.AzureCosmosDBSearchQueryType;

public class AzureCosmosDbNoSqlEmbeddingStore
extends AbstractAzureCosmosDBNoSqlEmbeddingStore
implements EmbeddingStore<TextSegment> {
    public AzureCosmosDbNoSqlEmbeddingStore(String endpoint, AzureKeyCredential keyCredential, String databaseName, String containerName, String partitionKeyPath, IndexingPolicy indexingPolicy, CosmosVectorEmbeddingPolicy cosmosVectorEmbeddingPolicy, CosmosFullTextPolicy cosmosFullTextPolicy, Integer vectorStoreThroughput, AzureCosmosDBSearchQueryType azureCosmosDBSearchQueryType, AzureCosmosDBNoSqlFilterMapper filterMapper) {
        this.initialize(endpoint, keyCredential, null, databaseName, containerName, partitionKeyPath, indexingPolicy, cosmosVectorEmbeddingPolicy, cosmosFullTextPolicy, vectorStoreThroughput, azureCosmosDBSearchQueryType, filterMapper);
    }

    public AzureCosmosDbNoSqlEmbeddingStore(String endpoint, TokenCredential tokenCredential, String databaseName, String containerName, String partitionKeyPath, IndexingPolicy indexingPolicy, CosmosVectorEmbeddingPolicy cosmosVectorEmbeddingPolicy, CosmosFullTextPolicy cosmosFullTextPolicy, Integer vectorStoreThroughput, AzureCosmosDBSearchQueryType azureCosmosDBSearchQueryType, AzureCosmosDBNoSqlFilterMapper filterMappe) {
        this.initialize(endpoint, null, tokenCredential, databaseName, containerName, partitionKeyPath, indexingPolicy, cosmosVectorEmbeddingPolicy, cosmosFullTextPolicy, vectorStoreThroughput, azureCosmosDBSearchQueryType, this.filterMapper);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String endpoint;
        private TokenCredential tokenCredential;
        private AzureKeyCredential keyCredential;
        private String databaseName;
        private String containerName;
        private String partitionKeyPath;
        private IndexingPolicy indexingPolicy;
        private CosmosVectorEmbeddingPolicy cosmosVectorEmbeddingPolicy;
        private CosmosFullTextPolicy cosmosFullTextPolicy;
        private Integer vectorStoreThroughput;
        private AzureCosmosDBSearchQueryType searchQueryType;
        private AzureCosmosDBNoSqlFilterMapper filterMapper;

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

        public Builder vectorStoreThroughput(int vectorStoreThroughput) {
            this.vectorStoreThroughput = vectorStoreThroughput;
            return this;
        }

        public Builder searchQueryType(AzureCosmosDBSearchQueryType searchQueryType) {
            this.searchQueryType = searchQueryType;
            return this;
        }

        public Builder filterMapper(AzureCosmosDBNoSqlFilterMapper filterMapper) {
            this.filterMapper = filterMapper;
            return this;
        }

        public AzureCosmosDbNoSqlEmbeddingStore build() {
            ValidationUtils.ensureNotNull((Object)this.endpoint, (String)"endpoint");
            ValidationUtils.ensureTrue((this.keyCredential != null || this.tokenCredential != null ? 1 : 0) != 0, (String)"either apiKey or tokenCredential must be set");
            if (this.keyCredential != null) {
                return new AzureCosmosDbNoSqlEmbeddingStore(this.endpoint, this.keyCredential, this.databaseName, this.containerName, this.partitionKeyPath, this.indexingPolicy, this.cosmosVectorEmbeddingPolicy, this.cosmosFullTextPolicy, this.vectorStoreThroughput, this.searchQueryType, this.filterMapper);
            }
            return new AzureCosmosDbNoSqlEmbeddingStore(this.endpoint, this.tokenCredential, this.databaseName, this.containerName, this.partitionKeyPath, this.indexingPolicy, this.cosmosVectorEmbeddingPolicy, this.cosmosFullTextPolicy, this.vectorStoreThroughput, this.searchQueryType, this.filterMapper);
        }
    }
}

