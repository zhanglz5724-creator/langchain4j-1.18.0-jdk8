/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mongodb.ConnectionString
 *  com.mongodb.MongoClientSettings
 *  com.mongodb.MongoCommandException
 *  com.mongodb.client.AggregateIterable
 *  com.mongodb.client.MongoClient
 *  com.mongodb.client.MongoClients
 *  com.mongodb.client.MongoCollection
 *  com.mongodb.client.MongoDatabase
 *  com.mongodb.client.model.CreateCollectionOptions
 *  com.mongodb.client.result.InsertManyResult
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  dev.langchain4j.store.embedding.RelevanceScore
 *  org.bson.BsonArray
 *  org.bson.BsonDocument
 *  org.bson.BsonValue
 *  org.bson.Document
 *  org.bson.codecs.configuration.CodecProvider
 *  org.bson.codecs.configuration.CodecRegistries
 *  org.bson.codecs.configuration.CodecRegistry
 *  org.bson.codecs.pojo.PojoCodecProvider
 *  org.bson.conversions.Bson
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.azure.cosmos.mongo.vcore;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCommandException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.result.InsertManyResult;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.RelevanceScore;
import dev.langchain4j.store.embedding.azure.cosmos.mongo.vcore.AzureCosmosDbMongoVCoreDocument;
import dev.langchain4j.store.embedding.azure.cosmos.mongo.vcore.AzureCosmosDbMongoVCoreMatchedDocument;
import dev.langchain4j.store.embedding.azure.cosmos.mongo.vcore.MappingUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AzureCosmosDbMongoVCoreEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private static final Logger log = LoggerFactory.getLogger(AzureCosmosDbMongoVCoreEmbeddingStore.class);
    private final MongoCollection<AzureCosmosDbMongoVCoreDocument> collection;
    private final String indexName;
    private final VectorIndexType kind;
    private final Integer numLists;
    private final Integer dimensions;
    private final Integer m;
    private final Integer efConstruction;
    private final Integer efSearch;

    public AzureCosmosDbMongoVCoreEmbeddingStore(MongoClient mongoClient, String connectionString, String databaseName, String collectionName, String indexName, String applicationName, CreateCollectionOptions createCollectionOptions, Boolean createIndex, String kind, Integer numLists, Integer dimensions, Integer m, Integer efConstruction, Integer efSearch) {
        MongoDatabase database;
        if (mongoClient == null && Utils.isNullOrEmpty((String)connectionString)) {
            throw new IllegalArgumentException("You need to pass either the mongoClient or the connectionString required for connecting to Azure CosmosDB Mongo vCore");
        }
        if (Utils.isNullOrEmpty((String)databaseName) || Utils.isNullOrEmpty((String)collectionName)) {
            throw new IllegalArgumentException("databaseName and collectionName needs to be provided.");
        }
        createIndex = (Boolean)Utils.getOrDefault((Object)createIndex, (Object)false);
        this.indexName = (String)Utils.getOrDefault((Object)indexName, (Object)"defaultIndexAzureCosmos");
        applicationName = (String)Utils.getOrDefault((Object)applicationName, (Object)"LangChain4j");
        this.kind = VectorIndexType.fromString(kind);
        this.numLists = (Integer)Utils.getOrDefault((Object)numLists, (Object)1);
        this.dimensions = (Integer)Utils.getOrDefault((Object)dimensions, (Object)1536);
        this.m = (Integer)Utils.getOrDefault((Object)m, (Object)16);
        this.efConstruction = (Integer)Utils.getOrDefault((Object)efConstruction, (Object)64);
        this.efSearch = (Integer)Utils.getOrDefault((Object)efSearch, (Object)40);
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders((CodecProvider[])new CodecProvider[]{PojoCodecProvider.builder().register(new Class[]{AzureCosmosDbMongoVCoreDocument.class, BsonDocument.class}).build()});
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries((CodecRegistry[])new CodecRegistry[]{MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry});
        if (mongoClient == null) {
            mongoClient = MongoClients.create((MongoClientSettings)MongoClientSettings.builder().applyConnectionString(new ConnectionString(connectionString)).applicationName(applicationName).build());
        }
        if (!this.isCollectionExist(database = mongoClient.getDatabase(databaseName), collectionName)) {
            this.createCollection(database, collectionName, (CreateCollectionOptions)Utils.getOrDefault((Object)createCollectionOptions, (Object)new CreateCollectionOptions()));
        }
        this.collection = database.getCollection(collectionName, AzureCosmosDbMongoVCoreDocument.class).withCodecRegistry(codecRegistry);
        if (Boolean.TRUE.equals(createIndex) && !this.isIndexExist(this.indexName)) {
            this.createIndex(this.indexName, collectionName, database);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String add(Embedding embedding) {
        String id = Utils.randomUUID();
        this.add(id, embedding);
        return id;
    }

    public void add(String id, Embedding embedding) {
        this.addInternal(id, embedding, null);
    }

    public String add(Embedding embedding, TextSegment textSegment) {
        String id = Utils.randomUUID();
        this.addInternal(id, embedding, textSegment);
        return id;
    }

    public List<String> addAll(List<Embedding> embeddings) {
        List<String> ids = embeddings.stream().map(ignored -> Utils.randomUUID()).collect(Collectors.toList());
        this.addAll(ids, embeddings, null);
        return ids;
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        if (request.filter() != null) {
            throw new UnsupportedOperationException("EmbeddingSearchRequest.Filter is not supported yet.");
        }
        List<EmbeddingMatch<TextSegment>> matches = this.findRelevant(request.queryEmbedding(), request.maxResults(), request.minScore());
        return new EmbeddingSearchResult(matches);
    }

    public List<EmbeddingMatch<TextSegment>> findRelevant(Embedding referenceEmbedding, int maxResults, double minScore) {
        List<Object> pipeline = new ArrayList();
        switch (this.kind) {
            case VECTOR_IVF: {
                pipeline = this.getPipelineDefinitionVectorIVF(referenceEmbedding, maxResults);
                break;
            }
            case VECTOR_HNSW: {
                pipeline = this.getPipelineDefinitionVectorHNSW(referenceEmbedding, maxResults);
            }
        }
        try {
            AggregateIterable results = this.collection.aggregate(pipeline, BsonDocument.class);
            return StreamSupport.stream(results.spliterator(), false).filter(doc -> RelevanceScore.fromCosineSimilarity((double)doc.getDouble((Object)"similarityScore").getValue()) >= minScore).map(doc -> MappingUtils.toEmbeddingMatch(this.mapBsonToAzureCosmosDbMongoVCoreMatchedDocument(doc.getDocument((Object)"document"), doc.getDouble((Object)"similarityScore").getValue()))).collect(Collectors.toList());
        }
        catch (MongoCommandException e) {
            throw new RuntimeException("Error in AzureCosmosDbMongoVCoreEmbeddingStore.findRelevant", e);
        }
    }

    private List<Bson> getPipelineDefinitionVectorIVF(Embedding queryVector, int maxResults) {
        ArrayList<Bson> pipeline = new ArrayList<Bson>();
        Document searchStage = new Document("$search", (Object)new Document("cosmosSearch", (Object)new Document("vector", (Object)queryVector.vectorAsList()).append("path", (Object)"embedding").append("k", (Object)maxResults)).append("returnStoredSource", (Object)true));
        pipeline.add((Bson)searchStage);
        Document projectStage = new Document("$project", (Object)new Document("similarityScore", (Object)new Document("$meta", (Object)"searchScore")).append("document", (Object)"$$ROOT"));
        pipeline.add((Bson)projectStage);
        return pipeline;
    }

    private List<Bson> getPipelineDefinitionVectorHNSW(Embedding queryVector, int maxResults) {
        ArrayList<Bson> pipeline = new ArrayList<Bson>();
        Document searchStage = new Document("$search", (Object)new Document("cosmosSearch", (Object)new Document("vector", (Object)queryVector.vectorAsList()).append("path", (Object)"embedding").append("k", (Object)maxResults).append("efSearch", (Object)this.efSearch)));
        pipeline.add((Bson)searchStage);
        Document projectStage = new Document("$project", (Object)new Document("similarityScore", (Object)new Document("$meta", (Object)"searchScore")).append("document", (Object)"$$ROOT"));
        pipeline.add((Bson)projectStage);
        return pipeline;
    }

    private AzureCosmosDbMongoVCoreMatchedDocument mapBsonToAzureCosmosDbMongoVCoreMatchedDocument(BsonDocument bsonDocument, Double score) {
        AzureCosmosDbMongoVCoreMatchedDocument document = new AzureCosmosDbMongoVCoreMatchedDocument();
        document.setId(bsonDocument.getString((Object)"_id").getValue());
        ArrayList<Float> embedding = new ArrayList<Float>();
        BsonArray embeddingArray = bsonDocument.getArray((Object)"embedding");
        for (BsonValue value : embeddingArray) {
            embedding.add(Float.valueOf((float)value.asDouble().getValue()));
        }
        document.setEmbedding(embedding);
        if (bsonDocument.containsKey((Object)"text")) {
            document.setText(bsonDocument.getString((Object)"text").getValue());
        }
        if (bsonDocument.containsKey((Object)"metadata")) {
            HashMap<String, String> metadata = new HashMap<String, String>();
            BsonDocument metadataDocument = bsonDocument.getDocument((Object)"metadata");
            for (String key : metadataDocument.keySet()) {
                metadata.put(key, metadataDocument.getString((Object)key).getValue());
            }
            document.setMetadata(metadata);
        }
        document.setScore(RelevanceScore.fromCosineSimilarity((double)score));
        return document;
    }

    private void addInternal(String id, Embedding embedding, TextSegment embedded) {
        this.addAll(Collections.singletonList(id), Collections.singletonList(embedding), embedded == null ? null : Collections.singletonList(embedded));
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) {
        if (Utils.isNullOrEmpty(ids) || Utils.isNullOrEmpty(embeddings)) {
            log.info("do not add empty embeddings to Azure CosmosDB  Mongo vCore");
            return;
        }
        ValidationUtils.ensureTrue((ids.size() == embeddings.size() ? 1 : 0) != 0, (String)"ids size is not equal to embeddings size");
        ValidationUtils.ensureTrue((embedded == null || embeddings.size() == embedded.size() ? 1 : 0) != 0, (String)"embeddings size is not equal to embedded size");
        ArrayList<AzureCosmosDbMongoVCoreDocument> documents = new ArrayList<AzureCosmosDbMongoVCoreDocument>(ids.size());
        for (int i = 0; i < ids.size(); ++i) {
            AzureCosmosDbMongoVCoreDocument document = MappingUtils.toMongoDbDocument(ids.get(i), embeddings.get(i), embedded == null ? null : embedded.get(i));
            documents.add(document);
        }
        InsertManyResult result = this.collection.insertMany(documents);
        if (!result.wasAcknowledged()) {
            String errMsg = String.format("[AzureCosmosDbMongoVCoreEmbeddingStore] Add document failed, Document=%s", documents);
            throw new RuntimeException(errMsg);
        }
    }

    static Iterable<String> listCollectionNames(MongoDatabase database) {
        try {
            Method m = MongoDatabase.class.getMethod("listCollectionNames", new Class[0]);
            Object result = m.invoke(database, new Object[0]);
            if (result instanceof Iterable) {
                return (Iterable)result;
            }
            throw new IllegalStateException("MongoDatabase.listCollectionNames() returned non-Iterable: " + result);
        }
        catch (NoSuchMethodException e) {
            throw new IllegalStateException("MongoDatabase.listCollectionNames() not found", e);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to invoke MongoDatabase.listCollectionNames()", e);
        }
    }

    static boolean collectionExists(MongoDatabase database, String collectionName) {
        return StreamSupport.stream(AzureCosmosDbMongoVCoreEmbeddingStore.listCollectionNames(database).spliterator(), false).anyMatch(collectionName::equals);
    }

    private boolean isCollectionExist(MongoDatabase database, String collectionName) {
        return AzureCosmosDbMongoVCoreEmbeddingStore.collectionExists(database, collectionName);
    }

    private void createCollection(MongoDatabase database, String collectionName, CreateCollectionOptions createCollectionOptions) {
        database.createCollection(collectionName, createCollectionOptions);
    }

    private boolean isIndexExist(String indexName) {
        return StreamSupport.stream(this.collection.listIndexes().spliterator(), false).anyMatch(index -> indexName.equals(index.getString((Object)"name")));
    }

    private void createIndex(String indexName, String collectionName, MongoDatabase database) {
        Document commandDocument = new Document();
        switch (this.kind) {
            case VECTOR_IVF: {
                commandDocument = this.getIndexDefinitionVectorIVF(indexName, collectionName);
                break;
            }
            case VECTOR_HNSW: {
                commandDocument = this.getIndexDefinitionVectorHNSW(indexName, collectionName);
            }
        }
        database.runCommand((Bson)commandDocument);
    }

    private BsonDocument getIndexDefinitionVectorIVF(String indexName, String collectionName) {
        Document indexDefinition = new Document().append("name", (Object)indexName).append("key", (Object)new Document("embedding", (Object)"cosmosSearch")).append("cosmosSearchOptions", (Object)new Document().append("kind", (Object)this.kind.getValue()).append("numLists", (Object)this.numLists).append("similarity", (Object)SimilarityMetric.COS).append("dimensions", (Object)this.dimensions));
        BsonDocument bsonIndexDefinition = indexDefinition.toBsonDocument();
        BsonArray bsonArray = new BsonArray();
        bsonArray.add((BsonValue)bsonIndexDefinition);
        return new Document().append("createIndexes", (Object)collectionName).append("indexes", (Object)bsonArray).toBsonDocument();
    }

    private BsonDocument getIndexDefinitionVectorHNSW(String indexName, String collectionName) {
        Document indexDefinition = new Document().append("name", (Object)indexName).append("key", (Object)new Document("embedding", (Object)"cosmosSearch")).append("cosmosSearchOptions", (Object)new Document().append("kind", (Object)this.kind.getValue()).append("m", (Object)this.m).append("efConstruction", (Object)this.efConstruction).append("similarity", (Object)SimilarityMetric.COS).append("dimensions", (Object)this.dimensions));
        BsonDocument bsonIndexDefinition = indexDefinition.toBsonDocument();
        BsonArray bsonArray = new BsonArray();
        bsonArray.add((BsonValue)bsonIndexDefinition);
        return new Document().append("createIndexes", (Object)collectionName).append("indexes", (Object)bsonArray).toBsonDocument();
    }

    public static enum VectorIndexType {
        VECTOR_IVF("vector-ivf"),
        VECTOR_HNSW("vector-hnsw");

        private final String value;

        private VectorIndexType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static VectorIndexType fromString(String kindString) {
            return Arrays.stream(VectorIndexType.values()).filter(k -> k.getValue().equals(kindString)).findFirst().orElseThrow(() -> new IllegalArgumentException("This vector index type is not supported: " + kindString));
        }
    }

    public static enum SimilarityMetric {
        COS("COS");

        private final String value;

        private SimilarityMetric(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static SimilarityMetric fromString(String similarityString) {
            return Arrays.stream(SimilarityMetric.values()).filter(k -> k.getValue().equals(similarityString)).findFirst().orElseThrow(() -> new IllegalArgumentException("This similarity metric is not supported: " + similarityString));
        }
    }

    public static class Builder {
        private MongoClient mongoClient;
        private String connectionString;
        private String databaseName;
        private String collectionName;
        private String indexName;
        private String applicationName;
        private CreateCollectionOptions createCollectionOptions;
        private Boolean createIndex;
        private String kind;
        private Integer numLists;
        private Integer dimensions;
        private Integer m;
        private Integer efConstruction;
        private Integer efSearch;

        public Builder mongoClient(MongoClient mongoClient) {
            this.mongoClient = mongoClient;
            return this;
        }

        public Builder connectionString(String connectionString) {
            this.connectionString = connectionString;
            return this;
        }

        public Builder databaseName(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }

        public Builder collectionName(String collectionName) {
            this.collectionName = collectionName;
            return this;
        }

        public Builder indexName(String indexName) {
            this.indexName = indexName;
            return this;
        }

        public Builder applicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public Builder createCollectionOptions(CreateCollectionOptions createCollectionOptions) {
            this.createCollectionOptions = createCollectionOptions;
            return this;
        }

        public Builder createIndex(Boolean createIndex) {
            this.createIndex = createIndex;
            return this;
        }

        public Builder kind(String kind) {
            this.kind = kind;
            return this;
        }

        public Builder numLists(Integer numLists) {
            this.numLists = numLists;
            return this;
        }

        public Builder dimensions(Integer dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Builder m(Integer m) {
            this.m = m;
            return this;
        }

        public Builder efConstruction(Integer efConstruction) {
            this.efConstruction = efConstruction;
            return this;
        }

        public Builder efSearch(Integer efSearch) {
            this.efSearch = efSearch;
            return this;
        }

        public AzureCosmosDbMongoVCoreEmbeddingStore build() {
            return new AzureCosmosDbMongoVCoreEmbeddingStore(this.mongoClient, this.connectionString, this.databaseName, this.collectionName, this.indexName, this.applicationName, this.createCollectionOptions, this.createIndex, this.kind, this.numLists, this.dimensions, this.m, this.efConstruction, this.efSearch);
        }
    }
}

