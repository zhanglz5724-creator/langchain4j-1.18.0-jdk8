/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.datastax.oss.driver.api.core.CqlIdentifier
 *  com.datastax.oss.driver.api.core.CqlSession
 *  com.datastax.oss.driver.api.core.CqlSessionBuilder
 *  com.dtsx.astra.sdk.cassio.AnnQuery
 *  com.dtsx.astra.sdk.cassio.AnnQuery$AnnQueryBuilder
 *  com.dtsx.astra.sdk.cassio.AnnResult
 *  com.dtsx.astra.sdk.cassio.CassIO
 *  com.dtsx.astra.sdk.cassio.CassandraSimilarityMetric
 *  com.dtsx.astra.sdk.cassio.MetadataVectorRecord
 *  com.dtsx.astra.sdk.cassio.MetadataVectorTable
 *  com.dtsx.astra.sdk.utils.AstraEnvironment
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.CosineSimilarity
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  dev.langchain4j.store.embedding.RelevanceScore
 *  org.jspecify.annotations.NonNull
 */
package dev.langchain4j.store.embedding.cassandra;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.dtsx.astra.sdk.cassio.AnnQuery;
import com.dtsx.astra.sdk.cassio.AnnResult;
import com.dtsx.astra.sdk.cassio.CassIO;
import com.dtsx.astra.sdk.cassio.CassandraSimilarityMetric;
import com.dtsx.astra.sdk.cassio.MetadataVectorRecord;
import com.dtsx.astra.sdk.cassio.MetadataVectorTable;
import com.dtsx.astra.sdk.utils.AstraEnvironment;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.CosineSimilarity;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.RelevanceScore;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;

public class CassandraEmbeddingStore
implements EmbeddingStore<TextSegment> {
    protected MetadataVectorTable embeddingTable;
    protected CqlSession cassandraSession;

    public CassandraEmbeddingStore(CqlSession session, String tableName, int dimension) {
        this(session, tableName, dimension, CassandraSimilarityMetric.COSINE);
    }

    public CassandraEmbeddingStore(CqlSession session, String tableName, int dimension, CassandraSimilarityMetric metric) {
        this.cassandraSession = session;
        this.embeddingTable = new MetadataVectorTable(session, ((CqlIdentifier)session.getKeyspace().get()).asInternal(), tableName, dimension, metric);
        this.embeddingTable.create();
    }

    public void delete() {
        this.embeddingTable.delete();
    }

    public void clear() {
        this.embeddingTable.clear();
    }

    public CqlSession getCassandraSession() {
        return this.cassandraSession;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static BuilderAstra builderAstra() {
        return new BuilderAstra();
    }

    public String add(@NonNull Embedding embedding) {
        Objects.requireNonNull(embedding, "embedding must not be null");
        return this.add(embedding, null);
    }

    public String add(@NonNull Embedding embedding, TextSegment textSegment) {
        Objects.requireNonNull(embedding, "embedding must not be null");
        return this.addInternal(Utils.randomUUID(), embedding, textSegment);
    }

    private String addInternal(@NonNull String id, @NonNull Embedding embedding, TextSegment textSegment) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(embedding, "embedding must not be null");
        MetadataVectorRecord record = new MetadataVectorRecord(id, embedding.vectorAsList());
        if (textSegment != null) {
            record.setBody(textSegment.text());
            record.setMetadata(Utils.toStringValueMap((Map)textSegment.metadata().toMap()));
        }
        this.embeddingTable.put(record);
        return record.getRowId();
    }

    public void add(@NonNull String rowId, @NonNull Embedding embedding) {
        Objects.requireNonNull(rowId, "rowId must not be null");
        Objects.requireNonNull(embedding, "embedding must not be null");
        this.embeddingTable.put(new MetadataVectorRecord(rowId, embedding.vectorAsList()));
    }

    public List<String> addAll(List<Embedding> embeddingList) {
        return embeddingList.stream().map(Embedding::vectorAsList).map(MetadataVectorRecord::new).peek(arg_0 -> ((MetadataVectorTable)this.embeddingTable).putAsync(arg_0)).map(MetadataVectorRecord::getRowId).collect(Collectors.toList());
    }

    public void addAll(List<String> ids, List<Embedding> embeddingList, List<TextSegment> textSegmentList) {
        if (ids == null || embeddingList == null || textSegmentList == null) {
            throw new IllegalArgumentException("ids, embeddingList, and textSegmentList must not be null");
        }
        if (ids.size() != embeddingList.size() || ids.size() != textSegmentList.size()) {
            throw new IllegalArgumentException("ids, embeddingList, and textSegmentList must all have the same size");
        }
        for (int i = 0; i < embeddingList.size(); ++i) {
            this.addInternal(ids.get(i), embeddingList.get(i), textSegmentList.get(i));
        }
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        if (request.filter() != null) {
            throw new UnsupportedOperationException("EmbeddingSearchRequest.Filter is not supported yet.");
        }
        List<EmbeddingMatch<TextSegment>> matches = this.findRelevant(request.queryEmbedding(), request.maxResults(), request.minScore());
        return new EmbeddingSearchResult(matches);
    }

    public List<EmbeddingMatch<TextSegment>> findRelevant(Embedding embedding, int maxResults, double minScore) {
        return this.embeddingTable.similaritySearch(AnnQuery.builder().embeddings(embedding.vectorAsList()).recordCount(ValidationUtils.ensureGreaterThanZero((Integer)maxResults, (String)"maxResults")).threshold(CosineSimilarity.fromRelevanceScore((double)ValidationUtils.ensureBetween((Double)minScore, (double)0.0, (double)1.0, (String)"minScore"))).metric(CassandraSimilarityMetric.COSINE).build()).stream().map(CassandraEmbeddingStore::mapSearchResult).collect(Collectors.toList());
    }

    private static EmbeddingMatch<TextSegment> mapSearchResult(AnnResult<MetadataVectorRecord> record) {
        TextSegment embedded = null;
        String body = ((MetadataVectorRecord)record.getEmbedded()).getBody();
        if (body != null && !body.isEmpty() && ((MetadataVectorRecord)record.getEmbedded()).getMetadata() != null) {
            embedded = TextSegment.from((String)((MetadataVectorRecord)record.getEmbedded()).getBody(), (Metadata)new Metadata(((MetadataVectorRecord)record.getEmbedded()).getMetadata()));
        }
        return new EmbeddingMatch(Double.valueOf(RelevanceScore.fromCosineSimilarity((double)record.getSimilarity())), ((MetadataVectorRecord)record.getEmbedded()).getRowId(), Embedding.from((List)((MetadataVectorRecord)record.getEmbedded()).getVector()), embedded);
    }

    public List<EmbeddingMatch<TextSegment>> findRelevant(Embedding embedding, int maxResults, double minScore, Metadata metadata) {
        AnnQuery.AnnQueryBuilder builder = AnnQuery.builder().embeddings(embedding.vectorAsList()).metric(CassandraSimilarityMetric.COSINE).recordCount(ValidationUtils.ensureGreaterThanZero((Integer)maxResults, (String)"maxResults")).threshold(CosineSimilarity.fromRelevanceScore((double)ValidationUtils.ensureBetween((Double)minScore, (double)0.0, (double)1.0, (String)"minScore")));
        if (metadata != null) {
            builder.metaData(Utils.toStringValueMap((Map)metadata.toMap()));
        }
        return this.embeddingTable.similaritySearch(builder.build()).stream().map(CassandraEmbeddingStore::mapSearchResult).collect(Collectors.toList());
    }

    public static class Builder {
        public static Integer DEFAULT_PORT = 9042;
        private List<String> contactPoints;
        private String localDataCenter;
        private Integer port = DEFAULT_PORT;
        private String userName;
        private String password;
        protected String keyspace;
        protected String table;
        protected Integer dimension;
        protected CassandraSimilarityMetric metric = CassandraSimilarityMetric.COSINE;

        public Builder contactPoints(List<String> contactPoints) {
            this.contactPoints = contactPoints;
            return this;
        }

        public Builder localDataCenter(String localDataCenter) {
            this.localDataCenter = localDataCenter;
            return this;
        }

        public Builder port(Integer port) {
            this.port = port;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder keyspace(String keyspace) {
            this.keyspace = keyspace;
            return this;
        }

        public Builder table(String table) {
            this.table = table;
            return this;
        }

        public Builder dimension(Integer dimension) {
            this.dimension = dimension;
            return this;
        }

        public Builder metric(CassandraSimilarityMetric metric) {
            this.metric = metric;
            return this;
        }

        public CassandraEmbeddingStore build() {
            CqlSessionBuilder builder = (CqlSessionBuilder)((CqlSessionBuilder)CqlSession.builder().withKeyspace(this.keyspace)).withLocalDatacenter(this.localDataCenter);
            if (this.userName != null && this.password != null) {
                builder.withAuthCredentials(this.userName, this.password);
            }
            this.contactPoints.forEach(cp -> builder.addContactPoint(new InetSocketAddress((String)cp, (int)this.port)));
            return new CassandraEmbeddingStore((CqlSession)builder.build(), this.table, this.dimension, this.metric);
        }
    }

    public static class BuilderAstra {
        private String token;
        private UUID dbId;
        private String tableName;
        private int dimension;
        private String keyspaceName = "default_keyspace";
        private String dbRegion = "us-east1";
        private CassandraSimilarityMetric metric = CassandraSimilarityMetric.COSINE;
        private AstraEnvironment env = AstraEnvironment.PROD;

        public BuilderAstra token(String token) {
            this.token = token;
            return this;
        }

        public BuilderAstra env(AstraEnvironment env) {
            this.env = env;
            return this;
        }

        public BuilderAstra databaseId(UUID dbId) {
            this.dbId = dbId;
            return this;
        }

        public BuilderAstra databaseRegion(String dbRegion) {
            this.dbRegion = dbRegion;
            return this;
        }

        public BuilderAstra keyspace(String keyspaceName) {
            this.keyspaceName = keyspaceName;
            return this;
        }

        public BuilderAstra table(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public BuilderAstra dimension(int dimension) {
            this.dimension = dimension;
            return this;
        }

        public BuilderAstra metric(CassandraSimilarityMetric metric) {
            this.metric = metric;
            return this;
        }

        public CassandraEmbeddingStore build() {
            CqlSession cqlSession = CassIO.init((String)this.token, (UUID)this.dbId, (String)this.dbRegion, (String)this.keyspaceName, (AstraEnvironment)this.env);
            return new CassandraEmbeddingStore(cqlSession, this.tableName, this.dimension, this.metric);
        }
    }
}

