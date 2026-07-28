/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.pgvector.PGvector
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  dev.langchain4j.store.embedding.filter.Filter
 *  org.postgresql.ds.PGSimpleDataSource
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.pgvector;

import com.pgvector.PGvector;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.pgvector.DefaultMetadataStorageConfig;
import dev.langchain4j.store.embedding.pgvector.MetadataHandler;
import dev.langchain4j.store.embedding.pgvector.MetadataHandlerFactory;
import dev.langchain4j.store.embedding.pgvector.MetadataStorageConfig;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PgVectorEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private static final Logger log = LoggerFactory.getLogger(PgVectorEmbeddingStore.class);
    private static final String DEFAULT_TEXT_SEARCH_CONFIG = "simple";
    private static final int DEFAULT_RRF_K = 60;
    protected final DataSource datasource;
    protected final String table;
    private final boolean skipCreateVectorExtension;
    final MetadataHandler metadataHandler;
    private final SearchMode searchMode;
    private final String textSearchConfig;
    private final int rrfK;

    protected PgVectorEmbeddingStore(DataSource datasource, String table, Integer dimension, Boolean useIndex, Integer indexListSize, Boolean createTable, Boolean dropTableFirst, MetadataStorageConfig metadataStorageConfig, SearchMode searchMode, String textSearchConfig, Integer rrfK) {
        this(new DatasourceBuilder().datasource(datasource).table(table).dimension(dimension).useIndex(useIndex).indexListSize(indexListSize).createTable(createTable).dropTableFirst(dropTableFirst).skipCreateVectorExtension(null).metadataStorageConfig(metadataStorageConfig).searchMode(searchMode).textSearchConfig(textSearchConfig).rrfK(rrfK));
    }

    protected PgVectorEmbeddingStore(DataSource datasource, String table, Integer dimension, Boolean useIndex, Integer indexListSize, Boolean createTable, Boolean dropTableFirst, MetadataStorageConfig metadataStorageConfig) {
        this(datasource, table, dimension, useIndex, indexListSize, createTable, dropTableFirst, metadataStorageConfig, null, null, null);
    }

    protected PgVectorEmbeddingStore(String host, Integer port, String user, String password, String database, String table, Integer dimension, Boolean useIndex, Integer indexListSize, Boolean createTable, Boolean dropTableFirst, MetadataStorageConfig metadataStorageConfig) {
        this(PgVectorEmbeddingStore.createDataSource(host, port, user, password, database), table, dimension, useIndex, indexListSize, createTable, dropTableFirst, metadataStorageConfig);
    }

    protected PgVectorEmbeddingStore(PgVectorEmbeddingStoreBuilder builder) {
        this(new DatasourceBuilder().datasource(PgVectorEmbeddingStore.createDataSource(builder.host, builder.port, builder.user, builder.password, builder.database)).table(builder.table).dimension(builder.dimension).useIndex(builder.useIndex).indexListSize(builder.indexListSize).createTable(builder.createTable).dropTableFirst(builder.dropTableFirst).skipCreateVectorExtension(builder.skipCreateVectorExtension).metadataStorageConfig(builder.metadataStorageConfig).searchMode(builder.searchMode).textSearchConfig(builder.textSearchConfig).rrfK(builder.rrfK));
    }

    protected PgVectorEmbeddingStore(DatasourceBuilder builder) {
        this.datasource = (DataSource)ValidationUtils.ensureNotNull((Object)builder.datasource, (String)"datasource");
        this.table = ValidationUtils.ensureNotBlank((String)builder.table, (String)"table");
        MetadataStorageConfig config = (MetadataStorageConfig)Utils.getOrDefault((Object)builder.metadataStorageConfig, (Object)DefaultMetadataStorageConfig.defaultConfig());
        this.metadataHandler = MetadataHandlerFactory.get(config);
        boolean useIndex = (Boolean)Utils.getOrDefault((Object)builder.useIndex, (Object)false);
        boolean createTable = (Boolean)Utils.getOrDefault((Object)builder.createTable, (Object)true);
        boolean dropTableFirst = (Boolean)Utils.getOrDefault((Object)builder.dropTableFirst, (Object)false);
        this.skipCreateVectorExtension = (Boolean)Utils.getOrDefault((Object)builder.skipCreateVectorExtension, (Object)false);
        this.searchMode = (SearchMode)((Object)Utils.getOrDefault((Object)((Object)builder.searchMode), (Object)((Object)SearchMode.VECTOR)));
        this.textSearchConfig = (String)Utils.getOrDefault((Object)builder.textSearchConfig, (Object)DEFAULT_TEXT_SEARCH_CONFIG);
        this.rrfK = ValidationUtils.ensureGreaterThanZero((Integer)((Integer)Utils.getOrDefault((Object)builder.rrfK, (Object)60)), (String)"rrfK");
        if (useIndex || createTable || dropTableFirst) {
            this.initTable(dropTableFirst, createTable, useIndex, builder.dimension, builder.indexListSize);
        }
    }

    public PgVectorEmbeddingStore() {
        this.datasource = null;
        this.table = null;
        this.metadataHandler = null;
        this.skipCreateVectorExtension = false;
        this.searchMode = SearchMode.VECTOR;
        this.textSearchConfig = DEFAULT_TEXT_SEARCH_CONFIG;
        this.rrfK = 60;
    }

    private static DataSource createDataSource(String host, Integer port, String user, String password, String database) {
        host = ValidationUtils.ensureNotBlank((String)host, (String)"host");
        port = ValidationUtils.ensureGreaterThanZero((Integer)port, (String)"port");
        user = ValidationUtils.ensureNotBlank((String)user, (String)"user");
        password = ValidationUtils.ensureNotBlank((String)password, (String)"password");
        database = ValidationUtils.ensureNotBlank((String)database, (String)"database");
        PGSimpleDataSource source = new PGSimpleDataSource();
        source.setServerNames(new String[]{host});
        source.setPortNumbers(new int[]{port});
        source.setDatabaseName(database);
        source.setUser(user);
        source.setPassword(password);
        return source;
    }

    public static DatasourceBuilder datasourceBuilder() {
        return new DatasourceBuilder();
    }

    public static PgVectorEmbeddingStoreBuilder builder() {
        return new PgVectorEmbeddingStoreBuilder();
    }

    protected void initTable(Boolean dropTableFirst, Boolean createTable, Boolean useIndex, Integer dimension, Integer indexListSize) {
        String query = "init";
        try (Connection connection = this.getConnection();
             Statement statement = connection.createStatement();){
            if (dropTableFirst.booleanValue()) {
                statement.executeUpdate(String.format("DROP TABLE IF EXISTS %s", this.table));
            }
            if (createTable.booleanValue()) {
                query = String.format("CREATE TABLE IF NOT EXISTS %s (embedding_id UUID PRIMARY KEY, embedding vector(%s), text TEXT NULL, %s )", this.table, ValidationUtils.ensureGreaterThanZero((Integer)dimension, (String)"dimension"), this.metadataHandler.columnDefinitionsString());
                statement.executeUpdate(query);
                this.metadataHandler.createMetadataIndexes(statement, this.table);
            }
            String cleanTableName = this.computeCleanTableName();
            if (this.searchMode == SearchMode.HYBRID) {
                String ftsIndexName = cleanTableName + "_text_fts_gin_index";
                query = String.format("CREATE INDEX IF NOT EXISTS %s ON %s USING gin (to_tsvector('%s', coalesce(text, '')))", ftsIndexName, this.table, this.textSearchConfig);
                statement.executeUpdate(query);
            }
            if (useIndex.booleanValue()) {
                String indexName = cleanTableName + "_ivfflat_index";
                query = String.format("CREATE INDEX IF NOT EXISTS %s ON %s USING ivfflat (embedding vector_cosine_ops) WITH (lists = %s)", indexName, this.table, ValidationUtils.ensureGreaterThanZero((Integer)indexListSize, (String)"indexListSize"));
                statement.executeUpdate(query);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(String.format("Failed to execute '%s'", query), e);
        }
    }

    private String computeCleanTableName() {
        int lastDotIndex = this.table.lastIndexOf(46);
        return lastDotIndex >= 0 ? this.table.substring(lastDotIndex + 1) : this.table;
    }

    public String add(Embedding embedding) {
        String id = Utils.randomUUID();
        this.addInternal(id, embedding, null);
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

    public void removeAll(Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        String sql = String.format("DELETE FROM %s WHERE embedding_id = ANY (?)", this.table);
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            Array array = connection.createArrayOf("uuid", ids.stream().map(UUID::fromString).toArray());
            statement.setArray(1, array);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeAll(Filter filter) {
        ValidationUtils.ensureNotNull((Object)filter, (String)"filter");
        String whereClause = this.metadataHandler.whereClause(filter);
        String sql = String.format("DELETE FROM %s WHERE %s", this.table, whereClause);
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeAll() {
        try (Connection connection = this.getConnection();
             Statement statement = connection.createStatement();){
            statement.executeUpdate(String.format("TRUNCATE TABLE %s", this.table));
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        SearchMode mode = (SearchMode)((Object)Utils.getOrDefault((Object)((Object)this.searchMode), (Object)((Object)SearchMode.VECTOR)));
        switch (mode) {
            case VECTOR: {
                return this.embeddingOnlySearch(request);
            }
            case HYBRID: {
                return this.hybridSearch(request);
            }
        }
        throw new IllegalStateException("Unknown search mode: " + (Object)((Object)mode));
    }

    private EmbeddingSearchResult<TextSegment> embeddingOnlySearch(EmbeddingSearchRequest request) {
        Embedding referenceEmbedding = request.queryEmbedding();
        int maxResults = request.maxResults();
        double minScore = request.minScore();
        Filter filter = request.filter();
        ArrayList<EmbeddingMatch> result = new ArrayList<EmbeddingMatch>();
        try (Connection connection = this.getConnection();){
            String referenceVector = Arrays.toString(referenceEmbedding.vector());
            String whereClause = filter == null ? "" : this.metadataHandler.whereClause(filter);
            whereClause = whereClause.isEmpty() ? "" : "AND " + whereClause;
            String query = String.format("SELECT (2 - (embedding <=> '%s')) / 2 AS score, embedding_id, embedding, text, %s FROM %s WHERE round(cast(float8 (embedding <=> '%s') as numeric), 8) <= round(2 - 2 * %s, 8) %s ORDER BY embedding <=> '%s' LIMIT %s;", referenceVector, String.join((CharSequence)",", this.metadataHandler.columnsNames()), this.table, referenceVector, minScore, whereClause, referenceVector, maxResults);
            try (PreparedStatement selectStmt = connection.prepareStatement(query);
                 ResultSet resultSet = selectStmt.executeQuery();){
                while (resultSet.next()) {
                    double score = resultSet.getDouble("score");
                    String embeddingId = resultSet.getString("embedding_id");
                    PGvector vector = (PGvector)resultSet.getObject("embedding");
                    Embedding embedding = new Embedding(vector.toArray());
                    String text = resultSet.getString("text");
                    TextSegment textSegment = null;
                    if (Utils.isNotNullOrBlank((String)text)) {
                        Metadata metadata = this.metadataHandler.fromResultSet(resultSet);
                        textSegment = TextSegment.from((String)text, (Metadata)metadata);
                    }
                    result.add(new EmbeddingMatch(Double.valueOf(score), embeddingId, embedding, (Object)textSegment));
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new EmbeddingSearchResult(result);
    }

    private EmbeddingSearchResult<TextSegment> hybridSearch(EmbeddingSearchRequest request) {
        Embedding referenceEmbedding = request.queryEmbedding();
        String keywordQuery = request.query();
        if (Utils.isNullOrBlank((String)keywordQuery)) {
            throw new RuntimeException("For HYBRID search mode, the query must be provided in the EmbeddingSearchRequest");
        }
        int maxResults = request.maxResults();
        double minScore = request.minScore();
        Filter filter = request.filter();
        ArrayList<EmbeddingMatch> result = new ArrayList<EmbeddingMatch>();
        try (Connection connection = this.getConnection();){
            String referenceVector = Arrays.toString(referenceEmbedding.vector());
            String filterCondition = filter == null ? "" : this.metadataHandler.whereClause(filter);
            String vectorWhere = filterCondition.isEmpty() ? "" : "WHERE " + filterCondition;
            String keywordWhere = filterCondition.isEmpty() ? "" : " AND " + filterCondition;
            List<String> metadataCols = this.metadataHandler.columnsNames();
            String rawMetadataCols = metadataCols.isEmpty() ? "" : ", " + String.join((CharSequence)", ", metadataCols);
            String coalescedMetadataCols = "";
            if (!metadataCols.isEmpty()) {
                coalescedMetadataCols = ", " + metadataCols.stream().map(col -> String.format("COALESCE(v.%1$s, k.%1$s) AS %1$s", col)).collect(Collectors.joining(", "));
            }
            String sql = String.format(" WITH vector_search AS (\n   SELECT\n     embedding_id, embedding, text %1$s,\n     RANK() OVER (ORDER BY embedding <=> '%2$s') AS rnk\n   FROM %3$s\n   %4$s\n   ORDER BY embedding <=> '%2$s'\n   LIMIT %5$d\n ), keyword_search AS (\n   SELECT\n     embedding_id, embedding, text %1$s,\n     RANK() OVER (ORDER BY ts_rank(to_tsvector('%6$s', coalesce(text, '')), plainto_tsquery('%6$s', ?)) DESC) AS rnk\n   FROM %3$s\n   WHERE to_tsvector('%6$s', coalesce(text, '')) @@ plainto_tsquery('%6$s', ?)\n     %7$s\n   ORDER BY ts_rank(to_tsvector('%6$s', coalesce(text, '')), plainto_tsquery('%6$s', ?)) DESC\n   LIMIT %5$d\n )\n SELECT * FROM (\n   SELECT\n     COALESCE(v.embedding_id, k.embedding_id) AS embedding_id,\n     COALESCE(v.embedding, k.embedding) AS embedding,\n     COALESCE(v.text, k.text) AS text\n     %8$s,\n     COALESCE(1.0 / (%9$d + v.rnk), 0.0) + COALESCE(1.0 / (%9$d + k.rnk), 0.0) AS score\n   FROM vector_search v\n   FULL OUTER JOIN keyword_search k ON v.embedding_id = k.embedding_id\n ) ranked\n WHERE ranked.score >= ?\n ORDER BY ranked.score DESC\n LIMIT %10$d;", rawMetadataCols, referenceVector, this.table, vectorWhere, Math.max(maxResults, this.rrfK), this.textSearchConfig, keywordWhere, coalescedMetadataCols, this.rrfK, maxResults);
            try (PreparedStatement stmt = connection.prepareStatement(sql);){
                stmt.setString(1, keywordQuery);
                stmt.setString(2, keywordQuery);
                stmt.setString(3, keywordQuery);
                stmt.setDouble(4, minScore);
                try (ResultSet rs = stmt.executeQuery();){
                    while (rs.next()) {
                        double score = rs.getDouble("score");
                        String embeddingId = rs.getString("embedding_id");
                        PGvector vector = (PGvector)rs.getObject("embedding");
                        Embedding embedding = new Embedding(vector.toArray());
                        String text = rs.getString("text");
                        TextSegment textSegment = null;
                        if (Utils.isNotNullOrBlank((String)text)) {
                            Metadata metadata = this.metadataHandler.fromResultSet(rs);
                            textSegment = TextSegment.from((String)text, (Metadata)metadata);
                        }
                        result.add(new EmbeddingMatch(Double.valueOf(score), embeddingId, embedding, (Object)textSegment));
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new EmbeddingSearchResult(result);
    }

    private void addInternal(String id, Embedding embedding, TextSegment embedded) {
        this.addAll(Collections.singletonList(id), Collections.singletonList(embedding), embedded == null ? null : Collections.singletonList(embedded));
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) {
        if (Utils.isNullOrEmpty(ids) || Utils.isNullOrEmpty(embeddings)) {
            log.info("Empty embeddings - no ops");
            return;
        }
        ValidationUtils.ensureTrue((ids.size() == embeddings.size() ? 1 : 0) != 0, (String)"ids size is not equal to embeddings size");
        ValidationUtils.ensureTrue((embedded == null || embeddings.size() == embedded.size() ? 1 : 0) != 0, (String)"embeddings size is not equal to embedded size");
        try (Connection connection = this.getConnection();){
            String query = String.format("INSERT INTO %s (embedding_id, embedding, text, %s) VALUES (?, ?, ?, %s)ON CONFLICT (embedding_id) DO UPDATE SET embedding = EXCLUDED.embedding,text = EXCLUDED.text,%s;", this.table, String.join((CharSequence)",", this.metadataHandler.columnsNames()), String.join((CharSequence)",", Collections.nCopies(this.metadataHandler.columnsNames().size(), "?")), this.metadataHandler.insertClause());
            try (PreparedStatement upsertStmt = connection.prepareStatement(query);){
                for (int i = 0; i < ids.size(); ++i) {
                    upsertStmt.setObject(1, UUID.fromString(ids.get(i)));
                    upsertStmt.setObject(2, new PGvector(embeddings.get(i).vector()));
                    if (embedded != null && embedded.get(i) != null) {
                        upsertStmt.setObject(3, embedded.get(i).text());
                        this.metadataHandler.setMetadata(upsertStmt, 4, embedded.get(i).metadata());
                    } else {
                        upsertStmt.setNull(3, 12);
                        IntStream.range(4, 4 + this.metadataHandler.columnsNames().size()).forEach(j -> {
                            try {
                                upsertStmt.setNull(j, 1111);
                            }
                            catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                    upsertStmt.addBatch();
                }
                upsertStmt.executeBatch();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Connection getConnection() throws SQLException {
        Connection connection = this.datasource.getConnection();
        if (!this.skipCreateVectorExtension) {
            try (Statement statement = connection.createStatement();){
                statement.executeUpdate("CREATE EXTENSION IF NOT EXISTS vector");
            }
        }
        PGvector.addVectorType((Connection)connection);
        return connection;
    }

    public static class PgVectorEmbeddingStoreBuilder {
        private String host;
        private Integer port;
        private String user;
        private String password;
        private String database;
        private String table;
        private Integer dimension;
        private Boolean useIndex;
        private Integer indexListSize;
        private Boolean createTable;
        private Boolean dropTableFirst;
        private Boolean skipCreateVectorExtension;
        private MetadataStorageConfig metadataStorageConfig;
        private SearchMode searchMode;
        private String textSearchConfig;
        private Integer rrfK;

        PgVectorEmbeddingStoreBuilder() {
        }

        public PgVectorEmbeddingStoreBuilder host(String host) {
            this.host = host;
            return this;
        }

        public PgVectorEmbeddingStoreBuilder port(Integer port) {
            this.port = port;
            return this;
        }

        public PgVectorEmbeddingStoreBuilder user(String user) {
            this.user = user;
            return this;
        }

        public PgVectorEmbeddingStoreBuilder password(String password) {
            this.password = password;
            return this;
        }

        public PgVectorEmbeddingStoreBuilder database(String database) {
            this.database = database;
            return this;
        }

        public PgVectorEmbeddingStoreBuilder table(String table) {
            this.table = table;
            return this;
        }

        public PgVectorEmbeddingStoreBuilder dimension(Integer dimension) {
            this.dimension = dimension;
            return this;
        }

        public PgVectorEmbeddingStoreBuilder useIndex(Boolean useIndex) {
            this.useIndex = useIndex;
            return this;
        }

        public PgVectorEmbeddingStoreBuilder indexListSize(Integer indexListSize) {
            this.indexListSize = indexListSize;
            return this;
        }

        public PgVectorEmbeddingStoreBuilder createTable(Boolean createTable) {
            this.createTable = createTable;
            return this;
        }

        public PgVectorEmbeddingStoreBuilder skipCreateVectorExtension(Boolean skipCreateVectorExtension) {
            this.skipCreateVectorExtension = skipCreateVectorExtension;
            return this;
        }

        public PgVectorEmbeddingStoreBuilder dropTableFirst(Boolean dropTableFirst) {
            this.dropTableFirst = dropTableFirst;
            return this;
        }

        public PgVectorEmbeddingStoreBuilder metadataStorageConfig(MetadataStorageConfig metadataStorageConfig) {
            this.metadataStorageConfig = metadataStorageConfig;
            return this;
        }

        public PgVectorEmbeddingStoreBuilder searchMode(SearchMode searchMode) {
            this.searchMode = searchMode;
            return this;
        }

        public PgVectorEmbeddingStoreBuilder textSearchConfig(String textSearchConfig) {
            this.textSearchConfig = textSearchConfig;
            return this;
        }

        public PgVectorEmbeddingStoreBuilder rrfK(Integer rrfK) {
            this.rrfK = rrfK;
            return this;
        }

        public PgVectorEmbeddingStore build() {
            return new PgVectorEmbeddingStore(this);
        }

        public String toString() {
            return "PgVectorEmbeddingStore.PgVectorEmbeddingStoreBuilder(host=" + this.host + ", port=" + this.port + ", user=" + this.user + ", password=" + (this.password == null ? null : "********") + ", database=" + this.database + ", table=" + this.table + ", dimension=" + this.dimension + ", useIndex=" + this.useIndex + ", indexListSize=" + this.indexListSize + ", createTable=" + this.createTable + ", dropTableFirst=" + this.dropTableFirst + ", skipCreateVectorExtension=" + this.skipCreateVectorExtension + ", metadataStorageConfig=" + this.metadataStorageConfig + ", searchMode=" + (Object)((Object)this.searchMode) + ", textSearchConfig=" + this.textSearchConfig + ", rrfK=" + this.rrfK + ")";
        }
    }

    public static class DatasourceBuilder {
        private DataSource datasource;
        private String table;
        private Integer dimension;
        private Boolean useIndex;
        private Integer indexListSize;
        private Boolean createTable;
        private Boolean dropTableFirst;
        private Boolean skipCreateVectorExtension;
        private MetadataStorageConfig metadataStorageConfig;
        private SearchMode searchMode;
        private String textSearchConfig;
        private Integer rrfK;

        DatasourceBuilder() {
        }

        public DatasourceBuilder datasource(DataSource datasource) {
            this.datasource = datasource;
            return this;
        }

        public DatasourceBuilder table(String table) {
            this.table = table;
            return this;
        }

        public DatasourceBuilder dimension(Integer dimension) {
            this.dimension = dimension;
            return this;
        }

        public DatasourceBuilder useIndex(Boolean useIndex) {
            this.useIndex = useIndex;
            return this;
        }

        public DatasourceBuilder indexListSize(Integer indexListSize) {
            this.indexListSize = indexListSize;
            return this;
        }

        public DatasourceBuilder createTable(Boolean createTable) {
            this.createTable = createTable;
            return this;
        }

        public DatasourceBuilder dropTableFirst(Boolean dropTableFirst) {
            this.dropTableFirst = dropTableFirst;
            return this;
        }

        public DatasourceBuilder skipCreateVectorExtension(Boolean skipCreateVectorExtension) {
            this.skipCreateVectorExtension = skipCreateVectorExtension;
            return this;
        }

        public DatasourceBuilder metadataStorageConfig(MetadataStorageConfig metadataStorageConfig) {
            this.metadataStorageConfig = metadataStorageConfig;
            return this;
        }

        public DatasourceBuilder searchMode(SearchMode searchMode) {
            this.searchMode = searchMode;
            return this;
        }

        public DatasourceBuilder textSearchConfig(String textSearchConfig) {
            this.textSearchConfig = textSearchConfig;
            return this;
        }

        public DatasourceBuilder rrfK(Integer rrfK) {
            this.rrfK = rrfK;
            return this;
        }

        public PgVectorEmbeddingStore build() {
            return new PgVectorEmbeddingStore(this);
        }

        public String toString() {
            return "PgVectorEmbeddingStore.DatasourceBuilder(datasource=" + this.datasource + ", table=" + this.table + ", dimension=" + this.dimension + ", useIndex=" + this.useIndex + ", indexListSize=" + this.indexListSize + ", createTable=" + this.createTable + ", dropTableFirst=" + this.dropTableFirst + ", skipCreateVectorExtension=" + this.skipCreateVectorExtension + ", metadataStorageConfig=" + this.metadataStorageConfig + ", searchMode=" + (Object)((Object)this.searchMode) + ", textSearchConfig=" + this.textSearchConfig + ", rrfK=" + this.rrfK + ")";
        }
    }

    public static enum SearchMode {
        VECTOR,
        HYBRID;

    }
}

