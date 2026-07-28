/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
 *  org.mariadb.jdbc.MariaDbDataSource
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.mariadb;

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
import dev.langchain4j.store.embedding.mariadb.DefaultMetadataStorageConfig;
import dev.langchain4j.store.embedding.mariadb.MariaDBDistanceType;
import dev.langchain4j.store.embedding.mariadb.MariaDbValidator;
import dev.langchain4j.store.embedding.mariadb.MetadataHandler;
import dev.langchain4j.store.embedding.mariadb.MetadataHandlerFactory;
import dev.langchain4j.store.embedding.mariadb.MetadataStorageConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.sql.DataSource;
import org.mariadb.jdbc.MariaDbDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MariaDbEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private static final Logger log = LoggerFactory.getLogger(MariaDbEmbeddingStore.class);
    private final DataSource datasource;
    private final String table;
    private final MariaDBDistanceType distanceType;
    private final String idFieldName;
    private final String embeddingFieldName;
    private final String contentFieldName;
    public static final String DEFAULT_TABLE_NAME = "vector_store";
    public static final String DEFAULT_COLUMN_EMBEDDING = "embedding";
    public static final String DEFAULT_COLUMN_ID = "id";
    public static final String DEFAULT_COLUMN_CONTENT = "content";
    final MetadataHandler metadataHandler;

    private MariaDbEmbeddingStore(DataSource datasource, Builder builder) {
        this.datasource = (DataSource)ValidationUtils.ensureNotNull((Object)datasource, (String)"datasource");
        this.table = this.validateAndEnquoteIdentifier(builder.table, DEFAULT_TABLE_NAME);
        this.contentFieldName = this.validateAndEnquoteIdentifier(builder.contentFieldName, DEFAULT_COLUMN_CONTENT);
        this.embeddingFieldName = this.validateAndEnquoteIdentifier(builder.embeddingFieldName, DEFAULT_COLUMN_EMBEDDING);
        this.idFieldName = this.validateAndEnquoteIdentifier(builder.idFieldName, DEFAULT_COLUMN_ID);
        MetadataStorageConfig config = (MetadataStorageConfig)Utils.getOrDefault((Object)builder.metadataStorageConfig, (Object)DefaultMetadataStorageConfig.defaultConfig());
        this.metadataHandler = MetadataHandlerFactory.get(config, this.datasource);
        this.distanceType = builder.distanceType == null ? MariaDBDistanceType.COSINE : builder.distanceType;
        int dimension = (Integer)ValidationUtils.ensureNotNull((Object)builder.dimension, (String)"dimension");
        this.initTable(builder.dropTableFirst, builder.createTable, dimension);
    }

    private String validateAndEnquoteIdentifier(String value, String defaultValue) {
        return Utils.isNullOrEmpty((String)value) ? defaultValue : MariaDbValidator.validateAndEnquoteIdentifier(value, false);
    }

    protected void initTable(boolean dropTableFirst, boolean createTable, int dimension) {
        String query = "init";
        try (Connection connection = this.datasource.getConnection();
             Statement statement = connection.createStatement();){
            if (dropTableFirst) {
                statement.executeUpdate("DROP TABLE IF EXISTS " + this.table);
            }
            if (createTable) {
                query = String.format("CREATE TABLE IF NOT EXISTS %s (%s UUID NOT NULL DEFAULT uuid() PRIMARY KEY, %s VECTOR(%s) NOT NULL, %s TEXT NULL, %s, VECTOR INDEX %s_idx (%s) ) ENGINE=InnoDB COLLATE uca1400_ai_cs", this.table, this.idFieldName, this.embeddingFieldName, ValidationUtils.ensureGreaterThanZero((Integer)dimension, (String)"dimension"), this.contentFieldName, this.metadataHandler.columnDefinitionsString(), (this.table + "_" + this.embeddingFieldName).replaceAll("[ \\`\"'\\\\\\P{Print}]", ""), this.embeddingFieldName);
                statement.executeUpdate(query);
                this.metadataHandler.createMetadataIndexes(statement, this.table);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(String.format("Failed to execute '%s'", query), e);
        }
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
        this.addAllInternal(ids, embeddings, null);
        return ids;
    }

    public List<String> addAll(List<Embedding> embeddings, List<TextSegment> embedded) {
        List<String> ids = embeddings.stream().map(ignored -> Utils.randomUUID()).collect(Collectors.toList());
        this.addAllInternal(ids, embeddings, embedded);
        return ids;
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) {
        this.addAllInternal(ids, embeddings, embedded);
    }

    public void removeAll(Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        try (Connection connection = this.datasource.getConnection();
             Statement statement = connection.createStatement();){
            String commaSeparated = ids.stream().map(UUID::fromString).map(uuid -> "'" + uuid + "'").collect(Collectors.joining(","));
            String sql = String.format("DELETE FROM %s WHERE %s IN (%s)", this.table, this.idFieldName, commaSeparated);
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeAll(Filter filter) {
        ValidationUtils.ensureNotNull((Object)filter, (String)"filter");
        String whereClause = this.metadataHandler.whereClause(filter);
        String sql = String.format("DELETE FROM %s WHERE %s", this.table, whereClause);
        try (Connection connection = this.datasource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeAll() {
        try (Connection connection = this.datasource.getConnection();
             Statement statement = connection.createStatement();){
            statement.executeUpdate(String.format("TRUNCATE TABLE %s", this.table));
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        Embedding referenceEmbedding = request.queryEmbedding();
        int maxResults = request.maxResults();
        double minScore = request.minScore();
        Filter filter = request.filter();
        ArrayList<EmbeddingMatch> result = new ArrayList<EmbeddingMatch>();
        try (Connection connection = this.datasource.getConnection();){
            String metadataFilterClause = filter != null ? this.metadataHandler.whereClause(filter) : null;
            String filterClause = "";
            if (metadataFilterClause != null && !metadataFilterClause.isEmpty()) {
                filterClause = "and " + metadataFilterClause + " ";
            }
            String distanceTypeName = this.distanceType.name().toLowerCase(Locale.ROOT);
            String sql = String.format("SELECT * FROM (select %s, %s, %s, (2 - vec_distance_%s(%s, ?)) / 2 as score, %s from %s) as t where score >= ? %sorder by score desc LIMIT %s", this.idFieldName, this.embeddingFieldName, this.contentFieldName, distanceTypeName, this.embeddingFieldName, String.join((CharSequence)",", this.metadataHandler.escapedColumnsName()), this.table, filterClause, maxResults);
            try (PreparedStatement selectStmt = connection.prepareStatement(sql);){
                selectStmt.setObject(1, referenceEmbedding.vector());
                selectStmt.setDouble(2, minScore);
                try (ResultSet resultSet = selectStmt.executeQuery();){
                    while (resultSet.next()) {
                        String embeddingId = resultSet.getString(1);
                        Embedding embedding = new Embedding(resultSet.getObject(2, float[].class));
                        String text = resultSet.getString(3);
                        double score = resultSet.getDouble(4);
                        TextSegment textSegment = null;
                        if (Utils.isNotNullOrBlank((String)text)) {
                            Metadata metadata = this.metadataHandler.fromResultSet(resultSet);
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
        this.addAllInternal(Collections.singletonList(id), Collections.singletonList(embedding), embedded == null ? null : Collections.singletonList(embedded));
    }

    private void addAllInternal(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) {
        if (Utils.isNullOrEmpty(ids) || Utils.isNullOrEmpty(embeddings)) {
            log.info("Empty embeddings - no ops");
            return;
        }
        ValidationUtils.ensureTrue((ids.size() == embeddings.size() ? 1 : 0) != 0, (String)"ids size is not equal to embeddings size");
        ValidationUtils.ensureTrue((embedded == null || embeddings.size() == embedded.size() ? 1 : 0) != 0, (String)"embeddings size is not equal to embedded size");
        try (Connection connection = this.datasource.getConnection();){
            String query = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, %s) ON DUPLICATE KEY UPDATE %s = VALUES(%s), %s = VALUES(%s)%s", this.table, this.idFieldName, this.embeddingFieldName, this.contentFieldName, String.join((CharSequence)",", this.metadataHandler.escapedColumnsName()), String.join((CharSequence)",", Collections.nCopies(this.metadataHandler.escapedColumnsName().size(), "?")), this.embeddingFieldName, this.embeddingFieldName, this.contentFieldName, this.contentFieldName, this.metadataHandler.insertClause());
            try (PreparedStatement upsertStmt = connection.prepareStatement(query);){
                for (int i = 0; i < ids.size(); ++i) {
                    upsertStmt.setString(1, ids.get(i));
                    upsertStmt.setObject(2, embeddings.get(i).vector());
                    if (embedded != null && embedded.get(i) != null) {
                        upsertStmt.setString(3, embedded.get(i).text());
                        this.metadataHandler.setMetadata(upsertStmt, 4, embedded.get(i).metadata());
                    } else {
                        upsertStmt.setNull(3, 12);
                        IntStream.range(4, 4 + this.metadataHandler.escapedColumnsName().size()).forEach(j -> {
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String table;
        private MariaDBDistanceType distanceType;
        private String idFieldName;
        private String embeddingFieldName;
        private String contentFieldName;
        private MetadataStorageConfig metadataStorageConfig;
        private boolean dropTableFirst;
        private boolean createTable = false;
        private Integer dimension;
        private DataSource datasource;
        private String url;
        private String user;
        private String password;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder datasource(DataSource datasource) {
            this.datasource = datasource;
            return this;
        }

        public Builder table(String table) {
            this.table = table;
            return this;
        }

        public Builder distanceType(MariaDBDistanceType distanceType) {
            this.distanceType = distanceType;
            return this;
        }

        public Builder idFieldName(String idFieldName) {
            this.idFieldName = idFieldName;
            return this;
        }

        public Builder embeddingFieldName(String embeddingFieldName) {
            this.embeddingFieldName = embeddingFieldName;
            return this;
        }

        public Builder contentFieldName(String contentFieldName) {
            this.contentFieldName = contentFieldName;
            return this;
        }

        public Builder metadataStorageConfig(MetadataStorageConfig metadataStorageConfig) {
            this.metadataStorageConfig = metadataStorageConfig;
            return this;
        }

        public Builder dropTableFirst(boolean dropTableFirst) {
            this.dropTableFirst = dropTableFirst;
            return this;
        }

        public Builder createTable(boolean createTable) {
            this.createTable = createTable;
            return this;
        }

        public Builder dimension(Integer dimension) {
            this.dimension = dimension;
            return this;
        }

        public MariaDbEmbeddingStore build() {
            if (this.datasource == null) {
                if (this.url == null) {
                    throw new IllegalArgumentException("set datasource or url ");
                }
                MariaDbDataSource ds = new MariaDbDataSource();
                try {
                    ds.setUrl(this.url);
                    ds.setUser(this.user);
                    ds.setPassword(this.password);
                }
                catch (SQLException e) {
                    throw new IllegalArgumentException(String.format("Wrong url configuring builder: '%s'", this.url), e);
                }
                this.datasource = ds;
            }
            return new MariaDbEmbeddingStore(this.datasource, this);
        }
    }
}

