/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  dev.langchain4j.store.embedding.filter.Filter
 *  io.milvus.client.MilvusServiceClient
 *  io.milvus.common.clientenum.ConsistencyLevelEnum
 *  io.milvus.param.ConnectParam
 *  io.milvus.param.ConnectParam$Builder
 *  io.milvus.param.IndexType
 *  io.milvus.param.MetricType
 *  io.milvus.param.dml.InsertParam$Field
 *  io.milvus.param.dml.SearchParam
 *  io.milvus.response.SearchResultsWrapper
 */
package dev.langchain4j.store.embedding.milvus;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.milvus.CollectionOperationsExecutor;
import dev.langchain4j.store.embedding.milvus.CollectionRequestBuilder;
import dev.langchain4j.store.embedding.milvus.FieldDefinition;
import dev.langchain4j.store.embedding.milvus.Generator;
import dev.langchain4j.store.embedding.milvus.Mapper;
import dev.langchain4j.store.embedding.milvus.MilvusMetadataFilterMapper;
import io.milvus.client.MilvusServiceClient;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.param.ConnectParam;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import io.milvus.param.dml.InsertParam;
import io.milvus.param.dml.SearchParam;
import io.milvus.response.SearchResultsWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MilvusEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private static final String DEFAULT_ID_FIELD_NAME = "id";
    private static final String DEFAULT_TEXT_FIELD_NAME = "text";
    private static final String DEFAULT_METADATA_FIELD_NAME = "metadata";
    private static final String DEFAULT_VECTOR_FIELD_NAME = "vector";
    private final MilvusServiceClient milvusClient;
    private final String collectionName;
    private final MetricType metricType;
    private final ConsistencyLevelEnum consistencyLevel;
    private final boolean retrieveEmbeddingsOnSearch;
    private final boolean autoFlushOnInsert;
    private final FieldDefinition fieldDefinition;
    private final Map<String, Object> extraParameters;

    @Deprecated
    public MilvusEmbeddingStore(String host, Integer port, String collectionName, Integer dimension, IndexType indexType, MetricType metricType, String uri, String token, String username, String password, ConsistencyLevelEnum consistencyLevel, Boolean retrieveEmbeddingsOnSearch, Boolean autoFlushOnInsert, String databaseName, String idFieldName, String textFieldName, String metadataFieldName, String vectorFieldName) {
        this(MilvusEmbeddingStore.createMilvusClient(host, port, uri, token, username, password, databaseName), collectionName, dimension, indexType, metricType, consistencyLevel, retrieveEmbeddingsOnSearch, autoFlushOnInsert, idFieldName, textFieldName, metadataFieldName, vectorFieldName);
    }

    @Deprecated
    public MilvusEmbeddingStore(MilvusServiceClient milvusClient, String collectionName, Integer dimension, IndexType indexType, MetricType metricType, ConsistencyLevelEnum consistencyLevel, Boolean retrieveEmbeddingsOnSearch, Boolean autoFlushOnInsert, String idFieldName, String textFieldName, String metadataFieldName, String vectorFieldName) {
        this.milvusClient = (MilvusServiceClient)ValidationUtils.ensureNotNull((Object)milvusClient, (String)"milvusClient");
        this.collectionName = (String)Utils.getOrDefault((Object)collectionName, (Object)"default");
        this.metricType = (MetricType)Utils.getOrDefault((Object)metricType, (Object)MetricType.COSINE);
        this.consistencyLevel = (ConsistencyLevelEnum)Utils.getOrDefault((Object)consistencyLevel, (Object)ConsistencyLevelEnum.EVENTUALLY);
        this.retrieveEmbeddingsOnSearch = (Boolean)Utils.getOrDefault((Object)retrieveEmbeddingsOnSearch, (Object)false);
        this.autoFlushOnInsert = (Boolean)Utils.getOrDefault((Object)autoFlushOnInsert, (Object)false);
        this.fieldDefinition = new FieldDefinition((String)Utils.getOrDefault((Object)idFieldName, (Object)DEFAULT_ID_FIELD_NAME), (String)Utils.getOrDefault((Object)textFieldName, (Object)DEFAULT_TEXT_FIELD_NAME), (String)Utils.getOrDefault((Object)metadataFieldName, (Object)DEFAULT_METADATA_FIELD_NAME), (String)Utils.getOrDefault((Object)vectorFieldName, (Object)DEFAULT_VECTOR_FIELD_NAME));
        this.extraParameters = Collections.emptyMap();
        if (!CollectionOperationsExecutor.hasCollection(this.milvusClient, this.collectionName)) {
            CollectionOperationsExecutor.createCollection(this.milvusClient, this.collectionName, this.fieldDefinition, (Integer)ValidationUtils.ensureNotNull((Object)dimension, (String)"dimension"));
            CollectionOperationsExecutor.createIndex(this.milvusClient, this.collectionName, this.fieldDefinition.getVectorFieldName(), (IndexType)Utils.getOrDefault((Object)indexType, (Object)IndexType.FLAT), this.metricType);
        }
        CollectionOperationsExecutor.loadCollectionInMemory(this.milvusClient, collectionName);
    }

    public MilvusEmbeddingStore(Builder builder) {
        this.milvusClient = builder.milvusClient == null ? MilvusEmbeddingStore.createMilvusClient(builder.host, builder.port, builder.uri, builder.token, builder.username, builder.password, builder.databaseName) : builder.milvusClient;
        this.collectionName = (String)Utils.getOrDefault((Object)builder.collectionName, (Object)"default");
        this.metricType = (MetricType)Utils.getOrDefault((Object)builder.metricType, (Object)MetricType.COSINE);
        this.consistencyLevel = (ConsistencyLevelEnum)Utils.getOrDefault((Object)builder.consistencyLevel, (Object)ConsistencyLevelEnum.EVENTUALLY);
        this.retrieveEmbeddingsOnSearch = (Boolean)Utils.getOrDefault((Object)builder.retrieveEmbeddingsOnSearch, (Object)false);
        this.autoFlushOnInsert = (Boolean)Utils.getOrDefault((Object)builder.autoFlushOnInsert, (Object)false);
        this.fieldDefinition = new FieldDefinition((String)Utils.getOrDefault((Object)builder.idFieldName, (Object)DEFAULT_ID_FIELD_NAME), (String)Utils.getOrDefault((Object)builder.textFieldName, (Object)DEFAULT_TEXT_FIELD_NAME), (String)Utils.getOrDefault((Object)builder.metadataFieldName, (Object)DEFAULT_METADATA_FIELD_NAME), (String)Utils.getOrDefault((Object)builder.vectorFieldName, (Object)DEFAULT_VECTOR_FIELD_NAME));
        this.extraParameters = Utils.getOrDefault((Map)builder.extraParameters, Collections.emptyMap());
        if (!CollectionOperationsExecutor.hasCollection(this.milvusClient, this.collectionName)) {
            CollectionOperationsExecutor.createCollection(this.milvusClient, this.collectionName, this.fieldDefinition, (Integer)ValidationUtils.ensureNotNull((Object)builder.dimension, (String)"dimension"));
            if (this.extraParameters.isEmpty()) {
                CollectionOperationsExecutor.createIndex(this.milvusClient, this.collectionName, this.fieldDefinition.getVectorFieldName(), (IndexType)Utils.getOrDefault((Object)builder.indexType, (Object)IndexType.FLAT), this.metricType);
            } else {
                CollectionOperationsExecutor.createIndex(this.milvusClient, this.collectionName, this.fieldDefinition.getVectorFieldName(), (IndexType)Utils.getOrDefault((Object)builder.indexType, (Object)IndexType.FLAT), this.metricType, builder.extraParameters.toString());
            }
        }
        CollectionOperationsExecutor.loadCollectionInMemory(this.milvusClient, this.collectionName);
    }

    private static MilvusServiceClient createMilvusClient(String host, Integer port, String uri, String token, String username, String password, String databaseName) {
        ConnectParam.Builder connectBuilder = ConnectParam.newBuilder().withHost((String)Utils.getOrDefault((Object)host, (Object)"localhost")).withPort(((Integer)Utils.getOrDefault((Object)port, (Object)19530)).intValue()).withUri(uri).withToken(token).withAuthorization((String)Utils.getOrDefault((Object)username, (Object)""), (String)Utils.getOrDefault((Object)password, (Object)""));
        if (databaseName != null) {
            connectBuilder.withDatabaseName(databaseName);
        }
        return new MilvusServiceClient(connectBuilder.build());
    }

    public static Builder builder() {
        return new Builder();
    }

    public void dropCollection(String collectionName) {
        CollectionOperationsExecutor.dropCollection(this.milvusClient, collectionName);
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
        List<String> ids = Generator.generateRandomIds(embeddings.size());
        this.addAll(ids, embeddings, null);
        return ids;
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest embeddingSearchRequest) {
        SearchParam searchParam = CollectionRequestBuilder.buildSearchRequest(this.collectionName, this.fieldDefinition, embeddingSearchRequest.queryEmbedding().vectorAsList(), embeddingSearchRequest.filter(), embeddingSearchRequest.maxResults(), this.metricType, this.consistencyLevel);
        SearchResultsWrapper resultsWrapper = CollectionOperationsExecutor.search(this.milvusClient, searchParam);
        List<EmbeddingMatch<TextSegment>> matches = Mapper.toEmbeddingMatches(this.milvusClient, resultsWrapper, this.collectionName, this.fieldDefinition, this.consistencyLevel, this.retrieveEmbeddingsOnSearch, this.metricType);
        List result = matches.stream().filter(match -> match.score() >= embeddingSearchRequest.minScore()).collect(Collectors.toList());
        return new EmbeddingSearchResult(result);
    }

    private void addInternal(String id, Embedding embedding, TextSegment textSegment) {
        this.addAll(Collections.singletonList(id), Collections.singletonList(embedding), textSegment == null ? null : Collections.singletonList(textSegment));
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> textSegments) {
        if (Utils.isNullOrEmpty(ids) || Utils.isNullOrEmpty(embeddings)) {
            return;
        }
        ArrayList<InsertParam.Field> fields = new ArrayList<InsertParam.Field>();
        fields.add(new InsertParam.Field(this.fieldDefinition.getIdFieldName(), ids));
        fields.add(new InsertParam.Field(this.fieldDefinition.getTextFieldName(), Mapper.toScalars(textSegments, ids.size())));
        fields.add(new InsertParam.Field(this.fieldDefinition.getMetadataFieldName(), Mapper.toMetadataJsons(textSegments, ids.size())));
        fields.add(new InsertParam.Field(this.fieldDefinition.getVectorFieldName(), Mapper.toVectors(embeddings)));
        CollectionOperationsExecutor.insert(this.milvusClient, this.collectionName, fields);
        if (this.autoFlushOnInsert) {
            CollectionOperationsExecutor.flush(this.milvusClient, this.collectionName);
        }
    }

    public void removeAll(Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        CollectionOperationsExecutor.removeForVector(this.milvusClient, this.collectionName, String.format("%s in %s", this.fieldDefinition.getIdFieldName(), MilvusMetadataFilterMapper.formatValues(ids)));
    }

    public void removeAll(Filter filter) {
        ValidationUtils.ensureNotNull((Object)filter, (String)"filter");
        CollectionOperationsExecutor.removeForVector(this.milvusClient, this.collectionName, MilvusMetadataFilterMapper.map(filter, this.fieldDefinition.getMetadataFieldName()));
    }

    public void removeAll() {
        CollectionOperationsExecutor.removeForVector(this.milvusClient, this.collectionName, String.format("%s != \"\"", this.fieldDefinition.getIdFieldName()));
    }

    public static class Builder {
        private MilvusServiceClient milvusClient;
        private String host;
        private Integer port;
        private String collectionName;
        private Integer dimension;
        private IndexType indexType;
        private MetricType metricType;
        private String uri;
        private String token;
        private String username;
        private String password;
        private ConsistencyLevelEnum consistencyLevel;
        private Boolean retrieveEmbeddingsOnSearch;
        private String databaseName;
        private Boolean autoFlushOnInsert;
        private String idFieldName;
        private String textFieldName;
        private String metadataFieldName;
        private String vectorFieldName;
        private Map<String, Object> extraParameters;

        public Builder milvusClient(MilvusServiceClient milvusClient) {
            this.milvusClient = milvusClient;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(Integer port) {
            this.port = port;
            return this;
        }

        public Builder collectionName(String collectionName) {
            this.collectionName = collectionName;
            return this;
        }

        public Builder dimension(Integer dimension) {
            this.dimension = dimension;
            return this;
        }

        public Builder indexType(IndexType indexType) {
            this.indexType = indexType;
            return this;
        }

        public Builder metricType(MetricType metricType) {
            this.metricType = metricType;
            return this;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder consistencyLevel(ConsistencyLevelEnum consistencyLevel) {
            this.consistencyLevel = consistencyLevel;
            return this;
        }

        public Builder retrieveEmbeddingsOnSearch(Boolean retrieveEmbeddingsOnSearch) {
            this.retrieveEmbeddingsOnSearch = retrieveEmbeddingsOnSearch;
            return this;
        }

        public Builder autoFlushOnInsert(Boolean autoFlushOnInsert) {
            this.autoFlushOnInsert = autoFlushOnInsert;
            return this;
        }

        public Builder databaseName(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }

        public Builder idFieldName(String idFieldName) {
            this.idFieldName = idFieldName;
            return this;
        }

        public Builder textFieldName(String textFieldName) {
            this.textFieldName = textFieldName;
            return this;
        }

        public Builder metadataFieldName(String metadataFieldName) {
            this.metadataFieldName = metadataFieldName;
            return this;
        }

        public Builder vectorFieldName(String vectorFieldName) {
            this.vectorFieldName = vectorFieldName;
            return this;
        }

        public Builder extraParameters(Map<String, Object> extraParameters) {
            this.extraParameters = extraParameters;
            return this;
        }

        public MilvusEmbeddingStore build() {
            return new MilvusEmbeddingStore(this);
        }
    }
}

