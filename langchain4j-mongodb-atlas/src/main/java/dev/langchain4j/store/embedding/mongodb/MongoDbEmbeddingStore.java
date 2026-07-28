/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mongodb.MongoClientSettings
 *  com.mongodb.MongoCommandException
 *  com.mongodb.MongoDriverInformation
 *  com.mongodb.client.AggregateIterable
 *  com.mongodb.client.MongoClient
 *  com.mongodb.client.MongoCollection
 *  com.mongodb.client.MongoDatabase
 *  com.mongodb.client.model.CreateCollectionOptions
 *  com.mongodb.client.model.Filters
 *  com.mongodb.client.result.InsertManyResult
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  dev.langchain4j.store.embedding.filter.Filter
 *  org.bson.Document
 *  org.bson.codecs.configuration.CodecProvider
 *  org.bson.codecs.configuration.CodecRegistries
 *  org.bson.codecs.configuration.CodecRegistry
 *  org.bson.codecs.pojo.PojoCodecProvider
 *  org.bson.conversions.Bson
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoDriverInformation;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertManyResult;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.mongodb.IndexMapping;
import dev.langchain4j.store.embedding.mongodb.MappingUtils;
import dev.langchain4j.store.embedding.mongodb.MongoDbDocument;
import dev.langchain4j.store.embedding.mongodb.MongoDbMatchedDocument;
import dev.langchain4j.store.embedding.mongodb.MongoDbMetadataFilterMapper;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoDbEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private static Method APPEND_METADATA;
    private static final int SECONDS_TO_WAIT_FOR_INDEX = 20;
    private static final Logger log;
    private final MongoCollection<MongoDbDocument> collection;
    private final MongoDatabase database;
    private final String indexName;
    private final long maxResultRatio;
    private final Bson globalPrefilter;

    public MongoDbEmbeddingStore(MongoClient mongoClient, String databaseName, String collectionName, String indexName, Long maxResultRatio, CreateCollectionOptions createCollectionOptions, Bson filter, IndexMapping indexMapping, Boolean createIndex) {
        mongoClient = (MongoClient)ValidationUtils.ensureNotNull((Object)mongoClient, (String)"mongoClient");
        databaseName = (String)ValidationUtils.ensureNotNull((Object)databaseName, (String)"databaseName");
        collectionName = (String)ValidationUtils.ensureNotNull((Object)collectionName, (String)"collectionName");
        createIndex = (Boolean)Utils.getOrDefault((Object)createIndex, (Object)false);
        this.indexName = (String)ValidationUtils.ensureNotNull((Object)indexName, (String)"indexName");
        this.maxResultRatio = (Long)Utils.getOrDefault((Object)maxResultRatio, (Object)10L);
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders((CodecProvider[])new CodecProvider[]{PojoCodecProvider.builder().register(new Class[]{MongoDbDocument.class, MongoDbMatchedDocument.class}).build()});
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries((CodecRegistry[])new CodecRegistry[]{MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry});
        MongoDbEmbeddingStore.appendMongoClientMetadata(mongoClient);
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        if (!this.isCollectionExist(database, collectionName)) {
            this.createCollection(database, collectionName, (CreateCollectionOptions)Utils.getOrDefault((Object)createCollectionOptions, (Object)new CreateCollectionOptions()));
        }
        this.collection = database.getCollection(collectionName, MongoDbDocument.class).withCodecRegistry(codecRegistry);
        this.database = database;
        this.globalPrefilter = filter;
        if (!this.indexExists(this.indexName)) {
            if (createIndex.booleanValue()) {
                this.createIndex(this.indexName, (IndexMapping)Utils.getOrDefault((Object)indexMapping, (Object)IndexMapping.defaultIndexMapping()));
            } else {
                throw new RuntimeException(String.format("Search Index '%s' not found and must be created via createIndex(true), or manually as a vector search index (not a regular index), via the createSearchIndexes command", this.indexName));
            }
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

    public void removeAll() {
        this.collection.deleteMany(Filters.empty());
    }

    public void removeAll(Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        this.collection.deleteMany(Filters.in((String)"_id", ids));
    }

    public void removeAll(Filter filter) {
        ValidationUtils.ensureNotNull((Object)filter, (String)"filter");
        this.collection.deleteMany(MongoDbMetadataFilterMapper.map(filter));
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        List queryVector = request.queryEmbedding().vectorAsList().stream().map(Float::doubleValue).collect(Collectors.toList());
        long numCandidates = (long)request.maxResults() * this.maxResultRatio;
        Bson postFilter = null;
        if (request.minScore() > 0.0) {
            postFilter = Filters.gte((String)"score", (Object)request.minScore());
        }
        if (request.filter() != null) {
            Bson newFilter = MongoDbMetadataFilterMapper.map(request.filter());
            postFilter = postFilter == null ? newFilter : Filters.and((Bson[])new Bson[]{postFilter, newFilter});
        }
        ArrayList<Document> pipeline = new ArrayList<Document>();
        Document vectorSearchStage = new Document("$vectorSearch", (Object)new Document().append("index", (Object)this.indexName).append("path", (Object)"embedding").append("queryVector", queryVector).append("numCandidates", (Object)numCandidates).append("limit", (Object)request.maxResults()));
        if (this.globalPrefilter != null) {
            ((Document)vectorSearchStage.get((Object)"$vectorSearch", Document.class)).append("filter", (Object)this.globalPrefilter);
        }
        pipeline.add(vectorSearchStage);
        pipeline.add(new Document("$project", (Object)new Document().append("embedding", (Object)1).append("metadata", (Object)1).append("text", (Object)1).append("score", (Object)new Document("$meta", (Object)"vectorSearchScore"))));
        if (postFilter != null) {
            pipeline.add(new Document("$match", (Object)postFilter));
        }
        try {
            AggregateIterable results = this.collection.aggregate(pipeline, MongoDbMatchedDocument.class);
            List result = StreamSupport.stream(results.spliterator(), false).map(MappingUtils::toEmbeddingMatch).collect(Collectors.toList());
            return new EmbeddingSearchResult(result);
        }
        catch (MongoCommandException e) {
            log.error("Error in MongoDBEmbeddingStore.search", (Throwable)e);
            throw new RuntimeException(e);
        }
    }

    private void addInternal(String id, Embedding embedding, TextSegment embedded) {
        this.addAll(Collections.singletonList(id), Collections.singletonList(embedding), embedded == null ? null : Collections.singletonList(embedded));
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) {
        if (Utils.isNullOrEmpty(ids) || Utils.isNullOrEmpty(embeddings)) {
            log.info("do not add empty embeddings to MongoDB Atlas");
            return;
        }
        ValidationUtils.ensureTrue((ids.size() == embeddings.size() ? 1 : 0) != 0, (String)"ids size is not equal to embeddings size");
        ValidationUtils.ensureTrue((embedded == null || embeddings.size() == embedded.size() ? 1 : 0) != 0, (String)"embeddings size is not equal to embedded size");
        ArrayList<MongoDbDocument> documents = new ArrayList<MongoDbDocument>(ids.size());
        for (int i = 0; i < ids.size(); ++i) {
            String id = ids.get(i);
            MongoDbDocument document = MappingUtils.toMongoDbDocument(id, embeddings.get(i), embedded == null ? null : embedded.get(i));
            documents.add(document);
        }
        InsertManyResult result = this.collection.insertMany(documents);
        if (!result.wasAcknowledged()) {
            String errMsg = String.format("[MongoDbEmbeddingStore] Add document failed, Document=%s", documents);
            log.error(errMsg);
            throw new RuntimeException(errMsg);
        }
    }

    private boolean isCollectionExist(MongoDatabase database, String collectionName) {
        return StreamSupport.stream(database.listCollectionNames().spliterator(), false).anyMatch(collectionName::equals);
    }

    private void createCollection(MongoDatabase database, String collectionName, CreateCollectionOptions createCollectionOptions) {
        database.createCollection(collectionName, createCollectionOptions);
    }

    private boolean indexExists(String indexName) {
        Document indexRecord = MongoDbEmbeddingStore.indexRecord(this.collection, indexName);
        return indexRecord != null && !indexRecord.getString((Object)"status").equals("DOES_NOT_EXIST");
    }

    private static Document indexRecord(MongoCollection<MongoDbDocument> collection, String indexName) {
        return StreamSupport.stream(collection.listSearchIndexes().spliterator(), false).filter(index -> indexName.equals(index.getString((Object)"name"))).findAny().orElse(null);
    }

    private void createIndex(String indexName, IndexMapping indexMapping) {
        Document indexDefinition = new Document("name", (Object)indexName).append("type", (Object)"vectorSearch").append("definition", (Object)MappingUtils.fromIndexMapping(indexMapping));
        Document cmd = new Document("createSearchIndexes", (Object)this.collection.getNamespace().getCollectionName()).append("indexes", Collections.singletonList(indexDefinition));
        this.database.runCommand((Bson)cmd);
        MongoDbEmbeddingStore.waitForIndex(this.collection, indexName);
    }

    static void waitForIndex(MongoCollection<MongoDbDocument> collection, String indexName) {
        long startTime = System.nanoTime();
        long timeoutNanos = TimeUnit.SECONDS.toNanos(20L);
        while (System.nanoTime() - startTime < timeoutNanos) {
            Document indexRecord = MongoDbEmbeddingStore.indexRecord(collection, indexName);
            if (indexRecord != null) {
                if ("FAILED".equals(indexRecord.getString((Object)"status"))) {
                    throw new RuntimeException("Search index has failed status.");
                }
                if (indexRecord.getBoolean((Object)"queryable").booleanValue()) {
                    return;
                }
            }
            try {
                Thread.sleep(100L);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        log.warn("Index {} was not created or did not exit INITIAL_SYNC within {} seconds", (Object)indexName, (Object)20);
    }

    private static void appendMongoClientMetadata(MongoClient mongoClient) {
        if (APPEND_METADATA != null) {
            try {
                APPEND_METADATA.invoke(mongoClient, MongoDriverInformation.builder().driverName("langchain4j-mongodb").build());
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                log.warn("langchain4j-mongodb metadata could not be added to MongoClient");
            }
        }
    }

    static {
        try {
            APPEND_METADATA = MongoClient.class.getMethod("appendMetadata", MongoDriverInformation.class);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
        log = LoggerFactory.getLogger(MongoDbEmbeddingStore.class);
    }

    public static class Builder {
        private MongoClient mongoClient;
        private String databaseName;
        private String collectionName;
        private String indexName;
        private Long maxResultRatio;
        private CreateCollectionOptions createCollectionOptions;
        private Bson filter;
        private IndexMapping indexMapping;
        private Boolean createIndex;

        public Builder fromClient(MongoClient mongoClient) {
            this.mongoClient = mongoClient;
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

        public Builder maxResultRatio(Long maxResultRatio) {
            this.maxResultRatio = maxResultRatio;
            return this;
        }

        public Builder createCollectionOptions(CreateCollectionOptions createCollectionOptions) {
            this.createCollectionOptions = createCollectionOptions;
            return this;
        }

        public Builder filter(Bson filter) {
            this.filter = filter;
            return this;
        }

        public Builder indexMapping(IndexMapping indexMapping) {
            this.indexMapping = indexMapping;
            return this;
        }

        public Builder createIndex(Boolean createIndex) {
            this.createIndex = createIndex;
            return this;
        }

        public MongoDbEmbeddingStore build() {
            return new MongoDbEmbeddingStore(this.mongoClient, this.databaseName, this.collectionName, this.indexName, this.maxResultRatio, this.createCollectionOptions, this.filter, this.indexMapping, this.createIndex);
        }
    }
}

