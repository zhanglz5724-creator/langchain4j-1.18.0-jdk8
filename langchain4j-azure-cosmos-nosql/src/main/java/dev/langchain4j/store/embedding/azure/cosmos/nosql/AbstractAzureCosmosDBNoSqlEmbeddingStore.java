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
 *  com.azure.cosmos.CosmosException
 *  com.azure.cosmos.implementation.apachecommons.lang.tuple.ImmutablePair
 *  com.azure.cosmos.implementation.apachecommons.lang.tuple.Pair
 *  com.azure.cosmos.models.CosmosBulkOperations
 *  com.azure.cosmos.models.CosmosContainerProperties
 *  com.azure.cosmos.models.CosmosFullTextIndex
 *  com.azure.cosmos.models.CosmosFullTextPolicy
 *  com.azure.cosmos.models.CosmosItemOperation
 *  com.azure.cosmos.models.CosmosQueryRequestOptions
 *  com.azure.cosmos.models.CosmosVectorEmbeddingPolicy
 *  com.azure.cosmos.models.CosmosVectorIndexSpec
 *  com.azure.cosmos.models.FeedResponse
 *  com.azure.cosmos.models.IndexingPolicy
 *  com.azure.cosmos.models.PartitionKey
 *  com.azure.cosmos.models.PartitionKeyDefinition
 *  com.azure.cosmos.models.PartitionKind
 *  com.azure.cosmos.models.SqlParameter
 *  com.azure.cosmos.models.SqlQuerySpec
 *  com.azure.cosmos.models.ThroughputProperties
 *  com.azure.cosmos.util.CosmosPagedFlux
 *  com.fasterxml.jackson.databind.JsonNode
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  dev.langchain4j.store.embedding.RelevanceScore
 *  dev.langchain4j.store.embedding.filter.Filter
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  reactor.core.publisher.Flux
 */
package dev.langchain4j.store.embedding.azure.cosmos.nosql;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.TokenCredential;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosAsyncDatabase;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosException;
import com.azure.cosmos.implementation.apachecommons.lang.tuple.ImmutablePair;
import com.azure.cosmos.implementation.apachecommons.lang.tuple.Pair;
import com.azure.cosmos.models.CosmosBulkOperations;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosFullTextIndex;
import com.azure.cosmos.models.CosmosFullTextPolicy;
import com.azure.cosmos.models.CosmosItemOperation;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.CosmosVectorEmbeddingPolicy;
import com.azure.cosmos.models.CosmosVectorIndexSpec;
import com.azure.cosmos.models.FeedResponse;
import com.azure.cosmos.models.IndexingPolicy;
import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.models.PartitionKeyDefinition;
import com.azure.cosmos.models.PartitionKind;
import com.azure.cosmos.models.SqlParameter;
import com.azure.cosmos.models.SqlQuerySpec;
import com.azure.cosmos.models.ThroughputProperties;
import com.azure.cosmos.util.CosmosPagedFlux;
import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.rag.content.retriever.azure.cosmos.nosql.AzureCosmosDBNoSqlFilterMapper;
import dev.langchain4j.rag.content.retriever.azure.cosmos.nosql.DefaultAzureCosmosDBNoSqlFilterMapper;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.RelevanceScore;
import dev.langchain4j.store.embedding.azure.cosmos.nosql.AzureCosmosDBNoSqlRuntimeException;
import dev.langchain4j.store.embedding.azure.cosmos.nosql.AzureCosmosDBSearchQueryType;
import dev.langchain4j.store.embedding.azure.cosmos.nosql.AzureCosmosDbNoSqlMatchedDocument;
import dev.langchain4j.store.embedding.azure.cosmos.nosql.MappingUtils;
import dev.langchain4j.store.embedding.filter.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class AbstractAzureCosmosDBNoSqlEmbeddingStore
implements EmbeddingStore<TextSegment> {
    protected static final String USER_AGENT = "LangChain4J-CDBNoSql-VectorStore-Java";
    protected static final String DEFAULT_DATABASE_NAME = "default_db";
    protected static final String DEFAULT_CONTAINER_NAME = "default_container";
    protected static final Integer DEFAULT_THROUGHPUT = 400;
    protected static final String DEFAULT_PARTITION_KEY_PATH = "/id";
    protected static final AzureCosmosDBSearchQueryType DEFAULT_SEARCH_QUERY_TYPE = AzureCosmosDBSearchQueryType.VECTOR;
    protected static final Integer DEFAULT_MAX_RESULTS = 1000;
    private static final Logger logger = LoggerFactory.getLogger(AbstractAzureCosmosDBNoSqlEmbeddingStore.class);
    protected AzureCosmosDBNoSqlFilterMapper filterMapper;
    private CosmosAsyncClient cosmosClient;
    private String databaseName;
    private String containerName;
    private String partitionKeyPath;
    private Integer vectorStoreThroughput;
    private IndexingPolicy indexingPolicy;
    private CosmosVectorEmbeddingPolicy cosmosVectorEmbeddingPolicy;
    private CosmosFullTextPolicy cosmosFullTextPolicy;
    private AzureCosmosDBSearchQueryType searchQueryType;
    private CosmosAsyncContainer container;

    protected void initialize(String endpoint, AzureKeyCredential keyCredential, TokenCredential tokenCredential, String databaseName, String containerName, String partitionKeyPath, IndexingPolicy indexingPolicy, CosmosVectorEmbeddingPolicy cosmosVectorEmbeddingPolicy, CosmosFullTextPolicy cosmosFullTextPolicy, Integer vectorStoreThroughput, AzureCosmosDBSearchQueryType searchQueryType, AzureCosmosDBNoSqlFilterMapper filterMapper) {
        ValidationUtils.ensureNotNull((Object)endpoint, (String)"%s", (Object[])new Object[]{"endpoint cannot be null or empty for Azure CosmosDB NoSql Embedding Store."});
        this.filterMapper = filterMapper == null ? new DefaultAzureCosmosDBNoSqlFilterMapper() : filterMapper;
        try {
            this.cosmosClient = keyCredential != null ? new CosmosClientBuilder().endpoint(endpoint).credential(keyCredential).userAgentSuffix(USER_AGENT).buildAsyncClient() : new CosmosClientBuilder().endpoint(endpoint).credential(tokenCredential).userAgentSuffix(USER_AGENT).buildAsyncClient();
        }
        catch (Exception e) {
            throw new RuntimeException("Error creating cosmosClient: {}", e);
        }
        this.databaseName = (String)Utils.getOrDefault((Object)databaseName, (Object)DEFAULT_DATABASE_NAME);
        this.containerName = (String)Utils.getOrDefault((Object)containerName, (Object)DEFAULT_CONTAINER_NAME);
        this.cosmosClient.createDatabaseIfNotExists(this.databaseName).block();
        this.partitionKeyPath = (String)Utils.getOrDefault((Object)partitionKeyPath, (Object)DEFAULT_PARTITION_KEY_PATH);
        this.vectorStoreThroughput = (Integer)Utils.getOrDefault((Object)vectorStoreThroughput, (Object)DEFAULT_THROUGHPUT);
        this.indexingPolicy = indexingPolicy;
        PartitionKeyDefinition subPartitionKeyDefinition = new PartitionKeyDefinition();
        ArrayList pathsFromCommaSeparatedList = new ArrayList();
        String[] subPartitionKeyPaths = this.partitionKeyPath.split(",");
        Collections.addAll(pathsFromCommaSeparatedList, subPartitionKeyPaths);
        if (subPartitionKeyPaths.length > 1) {
            subPartitionKeyDefinition.setPaths(pathsFromCommaSeparatedList);
            subPartitionKeyDefinition.setKind(PartitionKind.MULTI_HASH);
        } else {
            subPartitionKeyDefinition.setPaths(Collections.singletonList(this.partitionKeyPath));
            subPartitionKeyDefinition.setKind(PartitionKind.HASH);
        }
        this.searchQueryType = (AzureCosmosDBSearchQueryType)((Object)Utils.getOrDefault((Object)((Object)searchQueryType), (Object)((Object)DEFAULT_SEARCH_QUERY_TYPE)));
        switch (this.searchQueryType) {
            case VECTOR: {
                if (cosmosVectorEmbeddingPolicy != null && !indexingPolicy.getVectorIndexes().isEmpty()) break;
                throw new AzureCosmosDBNoSqlRuntimeException("cosmosVectorEmbeddingPolicy is required for VECTOR search");
            }
            case FULL_TEXT_SEARCH: 
            case FULL_TEXT_RANKING: {
                if (cosmosFullTextPolicy != null && !indexingPolicy.getCosmosFullTextIndexes().isEmpty()) break;
                throw new AzureCosmosDBNoSqlRuntimeException("cosmosFullTextPolicy is required for FULL_TEXT_* search");
            }
            case HYBRID: {
                ArrayList<String> missing = new ArrayList<String>();
                if (cosmosVectorEmbeddingPolicy == null) {
                    missing.add("cosmosVectorEmbeddingPolicy");
                }
                if (cosmosFullTextPolicy == null) {
                    missing.add("cosmosFullTextPolicy");
                }
                if (indexingPolicy.getVectorIndexes().isEmpty()) {
                    missing.add("vectorIndexes");
                }
                if (indexingPolicy.getCosmosFullTextIndexes().isEmpty()) {
                    missing.add("fullTextIndexes");
                }
                if (missing.isEmpty()) break;
                throw new AzureCosmosDBNoSqlRuntimeException("Missing for HYBRID: " + String.join((CharSequence)", ", missing));
            }
        }
        this.cosmosVectorEmbeddingPolicy = cosmosVectorEmbeddingPolicy;
        this.cosmosFullTextPolicy = cosmosFullTextPolicy;
        CosmosContainerProperties collectionDefinition = new CosmosContainerProperties(this.containerName, subPartitionKeyDefinition);
        collectionDefinition.setIndexingPolicy(this.indexingPolicy);
        switch (this.searchQueryType) {
            case VECTOR: {
                collectionDefinition.setVectorEmbeddingPolicy(this.cosmosVectorEmbeddingPolicy);
                break;
            }
            case FULL_TEXT_SEARCH: 
            case FULL_TEXT_RANKING: {
                collectionDefinition.setFullTextPolicy(this.cosmosFullTextPolicy);
                break;
            }
            case HYBRID: {
                collectionDefinition.setVectorEmbeddingPolicy(this.cosmosVectorEmbeddingPolicy);
                collectionDefinition.setFullTextPolicy(this.cosmosFullTextPolicy);
            }
        }
        ThroughputProperties throughputProperties = ThroughputProperties.createManualThroughput((int)this.vectorStoreThroughput);
        CosmosAsyncDatabase cosmosAsyncDatabase = this.cosmosClient.getDatabase(this.databaseName);
        cosmosAsyncDatabase.createContainerIfNotExists(collectionDefinition, throughputProperties).block();
        this.container = cosmosAsyncDatabase.getContainer(this.containerName);
    }

    public String add(Embedding embedding) {
        String id = Utils.randomUUID();
        this.add(id, embedding);
        return id;
    }

    public void add(String id, Embedding embedding) {
        this.addAll(Collections.singletonList(id), Collections.singletonList(embedding), null);
    }

    public String add(Embedding embedding, TextSegment textSegment) {
        String id = Utils.randomUUID();
        this.addAll(Collections.singletonList(id), Collections.singletonList(embedding), Collections.singletonList(textSegment));
        return id;
    }

    public String add(TextSegment textSegment) {
        String id = Utils.randomUUID();
        this.addAll(Collections.singletonList(id), null, Collections.singletonList(textSegment));
        return id;
    }

    public List<String> addAll(List<Embedding> embeddings) {
        List<String> ids = embeddings.stream().map(ignored -> Utils.randomUUID()).collect(Collectors.toList());
        this.addAll(ids, embeddings, null);
        return ids;
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) {
        if (this.searchQueryType.equals((Object)AzureCosmosDBSearchQueryType.FULL_TEXT_SEARCH) || this.searchQueryType.equals((Object)AzureCosmosDBSearchQueryType.FULL_TEXT_RANKING)) {
            if (Utils.isNullOrEmpty(embedded)) {
                logger.debug("do not add empty embeddings or content to Azure CosmosDB NoSQL");
                return;
            }
        } else if (Utils.isNullOrEmpty(embeddings)) {
            logger.debug("do not add empty embeddings to Azure CosmosDB NoSQL");
            return;
        }
        List itemOperationsWithIds = ids.stream().map(id -> {
            String partitionKeyValue;
            if (DEFAULT_PARTITION_KEY_PATH.equals(this.partitionKeyPath)) {
                partitionKeyValue = id;
            } else if (this.partitionKeyPath.startsWith("/metadata/") && Utils.isNullOrEmpty((Collection)embedded)) {
                Object value;
                String metadataKey = this.partitionKeyPath.substring("/metadata/".length());
                Object object = value = ((TextSegment)embedded.get(ids.indexOf(id))).metadata() != null ? ((TextSegment)embedded.get(ids.indexOf(id))).metadata().toMap().get(metadataKey) : null;
                if (value == null) {
                    throw new IllegalArgumentException("Partition key '" + metadataKey + "' not found in document metadata.");
                }
                partitionKeyValue = value.toString();
            } else {
                throw new IllegalArgumentException("Unsupported partition key path: " + this.partitionKeyPath);
            }
            CosmosItemOperation operation = Utils.isNullOrEmpty((Collection)embeddings) ? CosmosBulkOperations.getCreateItemOperation((Object)MappingUtils.toNoSqlDbDocument(id, null, (TextSegment)embedded.get(ids.indexOf(id))), (PartitionKey)new PartitionKey((Object)partitionKeyValue)) : (Utils.isNullOrEmpty((Collection)embedded) ? CosmosBulkOperations.getCreateItemOperation((Object)MappingUtils.toNoSqlDbDocument(id, (Embedding)embeddings.get(ids.indexOf(id)), null), (PartitionKey)new PartitionKey((Object)partitionKeyValue)) : CosmosBulkOperations.getCreateItemOperation((Object)MappingUtils.toNoSqlDbDocument(id, (Embedding)embeddings.get(ids.indexOf(id)), (TextSegment)embedded.get(ids.indexOf(id))), (PartitionKey)new PartitionKey((Object)partitionKeyValue)));
            return new ImmutablePair(id, (Object)operation);
        }).collect(Collectors.toList());
        try {
            List itemOperations = itemOperationsWithIds.stream().map(Pair::getValue).collect(Collectors.toList());
            this.container.executeBulkOperations(Flux.fromIterable(itemOperations)).doOnNext(response -> {
                if (response != null && response.getResponse() != null) {
                    int statusCode = response.getResponse().getStatusCode();
                    if (statusCode == 409) {
                        String documentId = itemOperationsWithIds.stream().filter(pair -> ((CosmosItemOperation)pair.getValue()).equals(response.getOperation())).findFirst().map(Pair::getKey).orElse("Unknown ID");
                        String errorMessage = String.format("Duplicate document id: %s", documentId);
                        logger.debug(errorMessage);
                        throw new RuntimeException(errorMessage);
                    }
                    logger.debug("Document added with status: {}", (Object)statusCode);
                } else {
                    logger.warn("Received a null response or null status code for a document operation.");
                }
            }).doOnError(error -> logger.debug("Error adding document: {}", (Object)error.getMessage())).doOnComplete(() -> logger.debug("Bulk operation completed successfully.")).blockLast();
        }
        catch (Exception e) {
            logger.debug("Exception occurred during bulk add operation: {}", (Object)e.getMessage(), (Object)e);
            throw e;
        }
    }

    public void remove(String id) {
        ValidationUtils.ensureNotBlank((String)id, (String)"id");
        this.removeAll(Collections.singletonList(id));
    }

    public void removeAll(Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        try {
            List itemOperations = ids.stream().map(id -> {
                String partitionKeyValue;
                if (DEFAULT_PARTITION_KEY_PATH.equals(this.partitionKeyPath)) {
                    partitionKeyValue = id;
                } else if (this.partitionKeyPath.startsWith("/metadata/")) {
                    String metadataKey = this.partitionKeyPath.substring("/metadata/".length());
                    String query = String.format("SELECT * FROM c WHERE c.id = '%s'", id);
                    CosmosPagedFlux queryFlux = this.container.queryItems(query, new CosmosQueryRequestOptions(), JsonNode.class);
                    List documents = ((FeedResponse)Objects.requireNonNull(queryFlux.byPage(1).blockFirst())).getResults();
                    if (documents == null || documents.isEmpty()) {
                        throw new IllegalArgumentException("No document found for id: " + id);
                    }
                    JsonNode document = (JsonNode)documents.get(0);
                    JsonNode metadataNode = document.get("metadata");
                    if (metadataNode == null || metadataNode.get(metadataKey) == null) {
                        throw new IllegalArgumentException("Partition key '" + metadataKey + "' not found in metadata for document with id: " + id);
                    }
                    partitionKeyValue = metadataNode.get(metadataKey).asText();
                } else {
                    throw new IllegalArgumentException("Unsupported partition key path: " + this.partitionKeyPath);
                }
                return CosmosBulkOperations.getDeleteItemOperation((String)id, (PartitionKey)new PartitionKey((Object)partitionKeyValue));
            }).collect(Collectors.toList());
            this.container.executeBulkOperations(Flux.fromIterable(itemOperations)).doOnNext(response -> logger.debug("Document deleted with status: {}", (Object)response.getResponse().getStatusCode())).doOnError(error -> logger.debug("Error deleting document: {}", (Object)error.getMessage())).blockLast();
        }
        catch (Exception e) {
            logger.debug("Exception while deleting documents: {}", (Object)e.getMessage(), (Object)e);
            throw e;
        }
    }

    public void deleteContainer() {
        try {
            this.container.delete().block();
            System.out.println("Deleted container: " + this.containerName);
        }
        catch (CosmosException e) {
            if (e.getStatusCode() == 404) {
                System.out.println("Container does not exist: " + this.containerName);
            }
            System.out.println("Failed to delete container: " + e.getMessage());
            throw e;
        }
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        if (request.maxResults() > DEFAULT_MAX_RESULTS) {
            throw new IllegalArgumentException("maxResults must be 1000 or less.");
        }
        String embeddingField = ((CosmosVectorIndexSpec)this.indexingPolicy.getVectorIndexes().get(0)).getPath().substring(((CosmosVectorIndexSpec)this.indexingPolicy.getVectorIndexes().get(0)).getPath().lastIndexOf("/") + 1);
        List referenceEmbeddingString = request.queryEmbedding().vectorAsList();
        StringBuilder queryBuilder = new StringBuilder(String.format("SELECT TOP @topK c.id as id, c.text as text, c.embedding as embedding, c.metadata as metadata, VectorDistance(c.%s, @embedding) as score FROM c", embeddingField));
        if (request.filter() != null) {
            queryBuilder.append(" AND").append(this.filterMapper.map(request.filter()));
        }
        queryBuilder.append(String.format(" ORDER BY VectorDistance(c.%s, @embedding)", embeddingField));
        String query = queryBuilder.toString();
        ArrayList<SqlParameter> parameters = new ArrayList<SqlParameter>();
        parameters.add(new SqlParameter("@embedding", (Object)referenceEmbeddingString));
        parameters.add(new SqlParameter("@topK", (Object)request.maxResults()));
        SqlQuerySpec sqlQuerySpec = new SqlQuerySpec(query, parameters);
        CosmosQueryRequestOptions options = AbstractAzureCosmosDBNoSqlEmbeddingStore.queryRequestOptions();
        logger.debug("Executing similarity search query: {}", (Object)query);
        return this.runQuery(sqlQuerySpec, options, request.minScore(), this.searchQueryType);
    }

    public EmbeddingSearchResult<TextSegment> findRelevantWithFullTextSearch(String content, Integer maxResults, double minScore, Filter filter) {
        if (maxResults > DEFAULT_MAX_RESULTS) {
            throw new IllegalArgumentException("maxResults must be 1000 or less.");
        }
        if (filter == null) {
            throw new IllegalArgumentException("Filter cannot be null.");
        }
        String query = "SELECT TOP @topK * FROM c WHERE " + this.filterMapper.map(filter);
        ArrayList<SqlParameter> parameters = new ArrayList<SqlParameter>();
        parameters.add(new SqlParameter("@topK", (Object)maxResults));
        SqlQuerySpec sqlQuerySpec = new SqlQuerySpec(query, parameters);
        CosmosQueryRequestOptions options = AbstractAzureCosmosDBNoSqlEmbeddingStore.queryRequestOptions();
        logger.debug("Executing full text search query: {}", (Object)query);
        return this.runQuery(sqlQuerySpec, options, minScore, this.searchQueryType);
    }

    public EmbeddingSearchResult<TextSegment> findRelevantWithFullTextRanking(String content, Integer maxResults, double minScore, Filter filter) {
        if (maxResults > DEFAULT_MAX_RESULTS) {
            throw new IllegalArgumentException("maxResults must be 1000 or less.");
        }
        StringBuilder queryBuilder = new StringBuilder("SELECT TOP @topK * FROM c");
        ArrayList<SqlParameter> parameters = new ArrayList<SqlParameter>();
        parameters.add(new SqlParameter("@topK", (Object)maxResults));
        if (filter != null) {
            queryBuilder.append(" WHERE (").append(this.filterMapper.map(filter)).append(") AND ");
        } else {
            queryBuilder.append(" WHERE ");
        }
        String searchWords = AbstractAzureCosmosDBNoSqlEmbeddingStore.toFullTextScoreArguments(content);
        queryBuilder.append(AbstractAzureCosmosDBNoSqlEmbeddingStore.fullTextRankPredicate("text", searchWords));
        queryBuilder.append(" ORDER BY RANK FullTextScore(c.text, ").append(searchWords).append(")");
        String query = queryBuilder.toString();
        SqlQuerySpec sqlQuerySpec = new SqlQuerySpec(query, parameters);
        CosmosQueryRequestOptions options = AbstractAzureCosmosDBNoSqlEmbeddingStore.queryRequestOptions();
        logger.debug("Executing full text search ranking query: {}", (Object)query);
        return this.runQuery(sqlQuerySpec, options, minScore, this.searchQueryType);
    }

    public EmbeddingSearchResult<TextSegment> findRelevantWithHybridSearch(Embedding referenceEmbedding, String content, Integer maxResults, double minScore, Filter filter) {
        if (maxResults > DEFAULT_MAX_RESULTS) {
            throw new IllegalArgumentException("maxResults must be 1000 or less.");
        }
        String embeddingField = ((CosmosVectorIndexSpec)this.indexingPolicy.getVectorIndexes().get(0)).getPath().substring(((CosmosVectorIndexSpec)this.indexingPolicy.getVectorIndexes().get(0)).getPath().lastIndexOf("/") + 1);
        String textField = ((CosmosFullTextIndex)this.indexingPolicy.getCosmosFullTextIndexes().get(0)).getPath().substring(((CosmosFullTextIndex)this.indexingPolicy.getCosmosFullTextIndexes().get(0)).getPath().lastIndexOf("/") + 1);
        String referenceEmbeddingVector = AbstractAzureCosmosDBNoSqlEmbeddingStore.toVectorLiteral(referenceEmbedding.vectorAsList());
        StringBuilder queryBuilder = new StringBuilder(String.format("SELECT TOP @topK c.id as id, c.text as text, c.embedding as embedding, c.metadata as metadata, VectorDistance(c.%s, %s) as score FROM c", embeddingField, referenceEmbeddingVector));
        ArrayList<SqlParameter> parameters = new ArrayList<SqlParameter>();
        parameters.add(new SqlParameter("@topK", (Object)maxResults));
        String searchWords = AbstractAzureCosmosDBNoSqlEmbeddingStore.toFullTextScoreArguments(content);
        if (filter != null) {
            queryBuilder.append(" WHERE (").append(this.filterMapper.map(filter)).append(") AND ");
        } else {
            queryBuilder.append(" WHERE ");
        }
        queryBuilder.append(AbstractAzureCosmosDBNoSqlEmbeddingStore.fullTextRankPredicate(textField, searchWords));
        queryBuilder.append(String.format(" ORDER BY RANK RRF(VectorDistance(c.%s, %s), FullTextScore(c.%s, %s))", embeddingField, referenceEmbeddingVector, textField, searchWords));
        String query = queryBuilder.toString();
        SqlQuerySpec sqlQuerySpec = new SqlQuerySpec(query, parameters);
        CosmosQueryRequestOptions options = AbstractAzureCosmosDBNoSqlEmbeddingStore.queryRequestOptions();
        logger.debug("Executing hybrid search query: {}", (Object)query);
        return this.runQuery(sqlQuerySpec, options, minScore, this.searchQueryType);
    }

    private static String toFullTextScoreArguments(String content) {
        ValidationUtils.ensureNotBlank((String)content, (String)"content");
        ArrayList searchWords = new ArrayList();
        Arrays.stream(content.trim().split("\\s+")).forEach(searchWord -> {
            String escapedSearchWord = searchWord.replace("\\", "\\\\").replace("\"", "\\\"");
            searchWords.add("\"" + escapedSearchWord + "\"");
        });
        return String.join((CharSequence)", ", searchWords);
    }

    private static String toVectorLiteral(List<Float> vector) {
        return vector.stream().map(String::valueOf).collect(Collectors.toList()).toString();
    }

    private static String fullTextRankPredicate(String textField, String searchWords) {
        return String.format("(FullTextContainsAny(c.%s, %s) OR IS_DEFINED(c.id))", textField, searchWords);
    }

    private static CosmosQueryRequestOptions queryRequestOptions() {
        return new CosmosQueryRequestOptions().setQueryMetricsEnabled(false).setMaxDegreeOfParallelism(1);
    }

    public void close() {
        if (this.cosmosClient != null) {
            this.cosmosClient.close();
        }
    }

    private EmbeddingSearchResult<TextSegment> runQuery(SqlQuerySpec sqlQuerySpec, CosmosQueryRequestOptions options, Double minScore, AzureCosmosDBSearchQueryType azureCosmosDBSearchQueryType) {
        List results = (List)this.container.queryItems(sqlQuerySpec, options, AzureCosmosDbNoSqlMatchedDocument.class).byPage().flatMap(page -> Flux.fromIterable((Iterable)page.getResults())).collectList().block();
        assert (results != null);
        List<EmbeddingMatch<TextSegment>> matches = this.getEmbeddingMatches(results, minScore, azureCosmosDBSearchQueryType);
        return new EmbeddingSearchResult(matches);
    }

    private List<EmbeddingMatch<TextSegment>> getEmbeddingMatches(List<AzureCosmosDbNoSqlMatchedDocument> results, Double minScore, AzureCosmosDBSearchQueryType azureCosmosDBSearchQueryType) {
        if (azureCosmosDBSearchQueryType.equals((Object)AzureCosmosDBSearchQueryType.FULL_TEXT_SEARCH) || azureCosmosDBSearchQueryType.equals((Object)AzureCosmosDBSearchQueryType.FULL_TEXT_RANKING)) {
            return results.stream().map(MappingUtils::toEmbeddingMatch).collect(Collectors.toList());
        }
        ArrayList<EmbeddingMatch<TextSegment>> matches = new ArrayList<EmbeddingMatch<TextSegment>>();
        for (AzureCosmosDbNoSqlMatchedDocument result : results) {
            Double score = RelevanceScore.fromCosineSimilarity((double)result.getScore());
            if (score < minScore) continue;
            result.setScore(score);
            EmbeddingMatch<TextSegment> embeddingMatch = MappingUtils.toEmbeddingMatch(result);
            matches.add(embeddingMatch);
        }
        return matches;
    }
}

