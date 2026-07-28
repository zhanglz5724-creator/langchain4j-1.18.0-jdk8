import java.util.Arrays;

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.core.credential.AzureKeyCredential
 *  com.azure.core.credential.TokenCredential
 *  com.azure.cosmos.CosmosAsyncClient
 *  com.azure.cosmos.CosmosAsyncContainer
 *  com.azure.cosmos.CosmosAsyncDatabase
 *  com.azure.cosmos.CosmosClientBuilder
 *  com.azure.cosmos.implementation.guava25.collect.ImmutableList
 *  com.azure.cosmos.models.CosmosContainerProperties
 *  com.azure.cosmos.models.CosmosItemRequestOptions
 *  com.azure.cosmos.models.ExcludedPath
 *  com.azure.cosmos.models.FeedResponse
 *  com.azure.cosmos.models.IncludedPath
 *  com.azure.cosmos.models.IndexingMode
 *  com.azure.cosmos.models.IndexingPolicy
 *  com.azure.cosmos.models.PartitionKey
 *  com.azure.cosmos.models.SqlParameter
 *  com.azure.cosmos.models.SqlQuerySpec
 *  com.azure.cosmos.models.ThroughputProperties
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.memory.chat.ChatMemoryStore
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.memory.azure.cosmos.nosql;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.TokenCredential;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosAsyncDatabase;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.implementation.guava25.collect.ImmutableList;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.ExcludedPath;
import com.azure.cosmos.models.FeedResponse;
import com.azure.cosmos.models.IncludedPath;
import com.azure.cosmos.models.IndexingMode;
import com.azure.cosmos.models.IndexingPolicy;
import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.models.SqlParameter;
import com.azure.cosmos.models.SqlQuerySpec;
import com.azure.cosmos.models.ThroughputProperties;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.azure.cosmos.nosql.AzureCosmosDBNoSqlRuntimeException;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AzureCosmosDBNoSqlMemoryStore
implements ChatMemoryStore {
    protected static final String USER_AGENT = "LangChain4J-CDBNoSql-MemoryStore-Java";
    protected static final String DEFAULT_DATABASE_NAME = "default_db";
    protected static final String DEFAULT_CONTAINER_NAME = "default_container";
    protected static final Integer DEFAULT_THROUGHPUT = 400;
    protected static final String DEFAULT_PARTITION_KEY_PATH = "/id";
    private static final Logger logger = LoggerFactory.getLogger(AzureCosmosDBNoSqlMemoryStore.class);
    private CosmosAsyncClient cosmosClient;
    private String databaseName;
    private String containerName;
    private String partitionKeyPath;
    private Integer vectorStoreThroughput;
    private CosmosAsyncContainer container;

    AzureCosmosDBNoSqlMemoryStore(String endpoint, TokenCredential tokenCredential, String databaseName, String containerName, Integer vectorStoreThroughput) {
        this(endpoint, null, tokenCredential, databaseName, containerName, vectorStoreThroughput);
    }

    AzureCosmosDBNoSqlMemoryStore(String endpoint, AzureKeyCredential keyCredential, String databaseName, String containerName, Integer vectorStoreThroughput) {
        this(endpoint, keyCredential, null, databaseName, containerName, vectorStoreThroughput);
    }

    AzureCosmosDBNoSqlMemoryStore(String endpoint, AzureKeyCredential keyCredential, TokenCredential tokenCredential, String databaseName, String containerName, Integer vectorStoreThroughput) {
        ValidationUtils.ensureNotNull((Object)endpoint, (String)"%s", (Object[])new Object[]{"cosmosClient cannot be null or empty for Azure CosmosDB NoSql Embedding Store."});
        this.cosmosClient = keyCredential != null ? new CosmosClientBuilder().endpoint(endpoint).credential(keyCredential).userAgentSuffix(USER_AGENT).buildAsyncClient() : new CosmosClientBuilder().endpoint(endpoint).credential(tokenCredential).userAgentSuffix(USER_AGENT).buildAsyncClient();
        this.databaseName = (String)Utils.getOrDefault((Object)databaseName, (Object)DEFAULT_DATABASE_NAME);
        this.containerName = (String)Utils.getOrDefault((Object)containerName, (Object)DEFAULT_CONTAINER_NAME);
        this.cosmosClient.createDatabaseIfNotExists(this.databaseName).block();
        this.partitionKeyPath = DEFAULT_PARTITION_KEY_PATH;
        this.vectorStoreThroughput = (Integer)Utils.getOrDefault((Object)vectorStoreThroughput, (Object)DEFAULT_THROUGHPUT);
        CosmosContainerProperties collectionDefinition = new CosmosContainerProperties(this.containerName, this.partitionKeyPath);
        IndexingPolicy indexingPolicy = this.getIndexingPolicy();
        collectionDefinition.setIndexingPolicy(indexingPolicy);
        ThroughputProperties throughputProperties = ThroughputProperties.createManualThroughput((int)this.vectorStoreThroughput);
        CosmosAsyncDatabase cosmosAsyncDatabase = this.cosmosClient.getDatabase(this.databaseName);
        cosmosAsyncDatabase.createContainerIfNotExists(collectionDefinition, throughputProperties).block();
        this.container = cosmosAsyncDatabase.getContainer(this.containerName);
    }

    private IndexingPolicy getIndexingPolicy() {
        IndexingPolicy indexingPolicy = new IndexingPolicy();
        indexingPolicy.setIndexingMode(IndexingMode.CONSISTENT);
        ExcludedPath excludedPath = new ExcludedPath("/*");
        indexingPolicy.setExcludedPaths(Collections.singletonList(excludedPath));
        IncludedPath includedPath1 = new IncludedPath("/metadata/?");
        IncludedPath includedPath2 = new IncludedPath("/content/?");
        indexingPolicy.setIncludedPaths((List)ImmutableArrays.asList((Object)includedPath1, (Object)includedPath2));
        return indexingPolicy;
    }

    public List<ChatMessage> getMessages(Object memoryId) {
        try {
            String query = "SELECT * FROM c WHERE c.id = @id";
            ArrayList<SqlParameter> parameters = new ArrayList<SqlParameter>();
            parameters.add(new SqlParameter("@id", memoryId));
            SqlQuerySpec sqlQuerySpec = new SqlQuerySpec(query, parameters);
            return ((FeedResponse)Objects.requireNonNull(this.container.queryItems(sqlQuerySpec, ChatMessage.class).byPage(1).blockFirst())).getResults();
        }
        catch (Exception e) {
            throw new AzureCosmosDBNoSqlRuntimeException("Exception while fetching documents: {}", e);
        }
    }

    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        try {
            this.deleteMessages(memoryId);
            this.container.upsertItem((Object)messages.get(0), new PartitionKey(memoryId), new CosmosItemRequestOptions()).block();
        }
        catch (Exception e) {
            throw new AzureCosmosDBNoSqlRuntimeException("Exception while updating documents: {}", e);
        }
    }

    public void deleteMessages(Object memoryId) {
        try {
            this.container.deleteItem(memoryId.toString(), new PartitionKey(memoryId)).block();
        }
        catch (Exception e) {
            throw new AzureCosmosDBNoSqlRuntimeException("Exception while deleting documents: {}", e);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String endpoint;
        private AzureKeyCredential keyCredential;
        private TokenCredential tokenCredential;
        private String databaseName;
        private String containerName;
        private Integer vectorStoreThroughput;

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

        public Builder vectorStoreThroughput(int vectorStoreThroughput) {
            this.vectorStoreThroughput = vectorStoreThroughput;
            return this;
        }

        public AzureCosmosDBNoSqlMemoryStore build() {
            ValidationUtils.ensureNotNull((Object)this.endpoint, (String)"endpoint");
            ValidationUtils.ensureTrue((this.keyCredential != null || this.tokenCredential != null ? 1 : 0) != 0, (String)"either apiKey or tokenCredential must be set");
            if (this.keyCredential != null) {
                return new AzureCosmosDBNoSqlMemoryStore(this.endpoint, this.keyCredential, this.databaseName, this.containerName, this.vectorStoreThroughput);
            }
            return new AzureCosmosDBNoSqlMemoryStore(this.endpoint, this.tokenCredential, this.databaseName, this.containerName, this.vectorStoreThroughput);
        }
    }
}

