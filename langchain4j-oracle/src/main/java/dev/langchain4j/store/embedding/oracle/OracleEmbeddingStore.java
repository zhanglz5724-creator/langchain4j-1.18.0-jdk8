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
 *  oracle.jdbc.OracleStatement
 *  oracle.jdbc.OracleType
 *  oracle.sql.json.OracleJsonDecimal
 *  oracle.sql.json.OracleJsonFactory
 *  oracle.sql.json.OracleJsonObject
 *  oracle.sql.json.OracleJsonValue
 *  oracle.sql.json.OracleJsonValue$OracleJsonType
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.oracle;

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
import dev.langchain4j.store.embedding.oracle.CreateOption;
import dev.langchain4j.store.embedding.oracle.EmbeddingTable;
import dev.langchain4j.store.embedding.oracle.IVFIndexBuilder;
import dev.langchain4j.store.embedding.oracle.Index;
import dev.langchain4j.store.embedding.oracle.SQLFilter;
import dev.langchain4j.store.embedding.oracle.SQLFilters;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.OracleType;
import oracle.sql.json.OracleJsonDecimal;
import oracle.sql.json.OracleJsonFactory;
import oracle.sql.json.OracleJsonObject;
import oracle.sql.json.OracleJsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class OracleEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private static final Logger log = LoggerFactory.getLogger(OracleEmbeddingStore.class);
    private final DataSource dataSource;
    private final EmbeddingTable table;
    private final boolean isExactSearch;

    private OracleEmbeddingStore(Builder builder) {
        this.dataSource = builder.dataSource;
        this.table = builder.embeddingTable;
        this.isExactSearch = builder.isExactSearch;
        try {
            this.table.create(this.dataSource);
            OracleEmbeddingStore.createIndex(builder);
        }
        catch (SQLException sqlException) {
            throw OracleEmbeddingStore.uncheckSQLException(sqlException);
        }
    }

    private static void createIndex(Builder builder) throws SQLException {
        if (builder.indexes != null) {
            try {
                for (Index index : builder.indexes) {
                    index.create(builder.dataSource, builder.embeddingTable);
                }
            }
            catch (SQLException sqlException) {
                throw OracleEmbeddingStore.uncheckSQLException(sqlException);
            }
        }
    }

    public String add(Embedding embedding) {
        ValidationUtils.ensureNotNull((Object)embedding, (String)"embedding");
        List<String> id = this.addAll(Collections.singletonList(embedding));
        return id.get(0);
    }

    public List<String> addAll(List<Embedding> embeddings) {
        ValidationUtils.ensureNotNull(embeddings, (String)"embeddings");
        String[] ids = new String[embeddings.size()];
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement insert = connection.prepareStatement("INSERT INTO " + this.table.name() + "(" + this.table.idColumn() + ", " + this.table.embeddingColumn() + ") VALUES (?, ?)");){
            for (int i = 0; i < embeddings.size(); ++i) {
                String id;
                ids[i] = id = Utils.randomUUID();
                Embedding embedding = OracleEmbeddingStore.ensureIndexNotNull(embeddings, i, "embeddings");
                insert.setString(1, id);
                insert.setObject(2, (Object)embedding.vector(), (SQLType)OracleType.VECTOR_FLOAT32);
                insert.addBatch();
            }
            insert.executeBatch();
        }
        catch (SQLException sqlException) {
            throw OracleEmbeddingStore.uncheckSQLException(sqlException);
        }
        return Arrays.asList(ids);
    }

    public String add(Embedding embedding, TextSegment textSegment) {
        ValidationUtils.ensureNotNull((Object)embedding, (String)"embedding");
        ValidationUtils.ensureNotNull((Object)textSegment, (String)"textSegment");
        List id = this.addAll(Collections.singletonList(embedding), Collections.singletonList(textSegment));
        return (String)id.get(0);
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) {
        ValidationUtils.ensureNotNull(embeddings, (String)"embeddings");
        ValidationUtils.ensureNotNull(embedded, (String)"embedded");
        if (embeddings.size() != embedded.size()) {
            throw new IllegalArgumentException("embeddings.size() " + embeddings.size() + " is not equal to embedded.size() " + embedded.size());
        }
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement insert = connection.prepareStatement("INSERT INTO " + this.table.name() + "(" + String.join((CharSequence)", ", this.table.idColumn(), this.table.embeddingColumn(), this.table.textColumn(), this.table.metadataColumn()) + ") VALUES (?, ?, ?, ?)");){
            for (int i = 0; i < embeddings.size(); ++i) {
                Embedding embedding = OracleEmbeddingStore.ensureIndexNotNull(embeddings, i, "embeddings");
                TextSegment textSegment = OracleEmbeddingStore.ensureIndexNotNull(embedded, i, "embedded");
                insert.setString(1, ids.get(i));
                insert.setObject(2, (Object)embedding.vector(), (SQLType)OracleType.VECTOR_FLOAT32);
                insert.setObject(3, textSegment.text());
                insert.setObject(4, OracleEmbeddingStore.getOsonFromMetadata(textSegment.metadata()));
                insert.addBatch();
            }
            insert.executeBatch();
        }
        catch (SQLException sqlException) {
            throw OracleEmbeddingStore.uncheckSQLException(sqlException);
        }
    }

    public void add(String id, Embedding embedding) {
        ValidationUtils.ensureNotNull((Object)id, (String)"id");
        ValidationUtils.ensureNotNull((Object)embedding, (String)"embedding");
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement merge = connection.prepareStatement("MERGE INTO " + this.table.name() + " existing USING (SELECT ? as id, ? as embedding) new ON (new.id = existing." + this.table.idColumn() + ") WHEN MATCHED THEN UPDATE SET existing." + this.table.embeddingColumn() + " = new.embedding WHEN NOT MATCHED THEN INSERT (" + this.table.idColumn() + ", " + this.table.embeddingColumn() + ") VALUES (new.id, new.embedding)");){
            merge.setString(1, id);
            merge.setObject(2, (Object)embedding.vector(), (SQLType)OracleType.VECTOR_FLOAT32);
            merge.execute();
        }
        catch (SQLException sqlException) {
            throw OracleEmbeddingStore.uncheckSQLException(sqlException);
        }
    }

    public void removeAll(Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement delete = connection.prepareStatement("DELETE FROM " + this.table.name() + " WHERE " + this.table.idColumn() + " = ?");){
            for (String id : ids) {
                ValidationUtils.ensureNotNull((Object)id, (String)"id");
                delete.setString(1, id);
                delete.addBatch();
            }
            delete.executeBatch();
        }
        catch (SQLException sqlException) {
            throw OracleEmbeddingStore.uncheckSQLException(sqlException);
        }
    }

    public void removeAll(Filter filter) {
        ValidationUtils.ensureNotNull((Object)filter, (String)"filter");
        SQLFilter sqlFilter = SQLFilters.create(filter, this.table::mapMetadataKey);
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement delete = connection.prepareStatement("DELETE FROM " + this.table.name() + sqlFilter.asWhereClause());){
            sqlFilter.setParameters(delete, 1);
            delete.executeUpdate();
        }
        catch (SQLException sqlException) {
            throw OracleEmbeddingStore.uncheckSQLException(sqlException);
        }
    }

    public void removeAll() {
        try (Connection connection = this.dataSource.getConnection();
             Statement statement = connection.createStatement();){
            statement.execute("TRUNCATE TABLE " + this.table.name());
        }
        catch (SQLException sqlException) {
            throw OracleEmbeddingStore.uncheckSQLException(sqlException);
        }
    }

    /*
     * Enabled aggressive exception aggregation
     */
    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        ValidationUtils.ensureNotNull((Object)request, (String)"request");
        SQLFilter sqlFilter = SQLFilters.create(request.filter(), this.table::mapMetadataKey);
        int maxResults = request.maxResults();
        try (Connection connection = this.dataSource.getConnection();){
            EmbeddingSearchResult embeddingSearchResult;
            block23: {
                PreparedStatement query = connection.prepareStatement("SELECT VECTOR_DISTANCE(" + this.table.embeddingColumn() + ", ?, COSINE) distance, " + String.join((CharSequence)", ", this.table.idColumn(), this.table.embeddingColumn(), this.table.textColumn(), this.table.metadataColumn()) + " FROM " + this.table.name() + sqlFilter.asWhereClause() + " ORDER BY distance FETCH " + (this.isExactSearch ? "" : " APPROXIMATE") + " FIRST " + maxResults + " ROWS ONLY");
                try {
                    query.setObject(1, (Object)request.queryEmbedding().vector(), -107);
                    sqlFilter.setParameters(query, 2);
                    query.setFetchSize(maxResults);
                    OracleStatement oracleStatement = query.unwrap(OracleStatement.class);
                    oracleStatement.defineColumnType(1, 101);
                    oracleStatement.defineColumnType(2, 12);
                    oracleStatement.defineColumnType(3, -107, Integer.MAX_VALUE);
                    oracleStatement.defineColumnType(4, 2005, Integer.MAX_VALUE);
                    oracleStatement.defineColumnType(5, 2016, Integer.MAX_VALUE);
                    oracleStatement.setLobPrefetchSize(Integer.MAX_VALUE);
                    ArrayList<EmbeddingMatch> matches = new ArrayList<EmbeddingMatch>(maxResults);
                    try (ResultSet resultSet = query.executeQuery();){
                        while (resultSet.next()) {
                            double score = 1.0 - resultSet.getDouble("distance") / 2.0;
                            if (score < request.minScore()) {
                                break;
                            }
                            String id = resultSet.getString(this.table.idColumn());
                            float[] embedding = resultSet.getObject(this.table.embeddingColumn(), float[].class);
                            String content = resultSet.getString(this.table.textColumn());
                            OracleJsonObject metadata = resultSet.getObject(this.table.metadataColumn(), OracleJsonObject.class);
                            EmbeddingMatch match = new EmbeddingMatch(Double.valueOf(score), id, new Embedding(embedding), (Object)(content == null ? null : new TextSegment(content, OracleEmbeddingStore.getMetadataFromOson(metadata))));
                            matches.add(match);
                        }
                    }
                    embeddingSearchResult = new EmbeddingSearchResult(matches);
                    if (query == null) break block23;
                }
                catch (Throwable throwable) {
                    if (query != null) {
                        try {
                            query.close();
                        }
                        catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    }
                    throw throwable;
                }
                query.close();
            }
            return embeddingSearchResult;
        }
        catch (SQLException sqlException) {
            throw OracleEmbeddingStore.uncheckSQLException(sqlException);
        }
    }

    private static OracleJsonObject getOsonFromMetadata(Metadata metadata) {
        if (metadata == null) {
            return null;
        }
        OracleJsonFactory factory = new OracleJsonFactory();
        OracleJsonObject object = factory.createObject();
        Map map = metadata.toMap();
        for (Map.Entry entry : map.entrySet()) {
            String key = (String)entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Number) {
                Number number = (Number)value;
                if (number instanceof Integer) {
                    object.put(key, number.intValue());
                    continue;
                }
                if (number instanceof Long) {
                    object.put(key, number.longValue());
                    continue;
                }
                if (number instanceof Float) {
                    object.put((Object)key, (Object)factory.createFloat(number.floatValue()));
                    continue;
                }
                if (number instanceof Double) {
                    object.put(key, number.doubleValue());
                    continue;
                }
                throw OracleEmbeddingStore.unrecognizedMetadata(key, value);
            }
            object.put(key, value.toString());
        }
        return object;
    }

    private static Metadata getMetadataFromOson(OracleJsonObject oson) {
        Metadata metadata = new Metadata();
        if (oson == null) {
            return metadata;
        }
        block10: for (Map.Entry entry : oson.entrySet()) {
            String key = (String)entry.getKey();
            OracleJsonValue value = (OracleJsonValue)entry.getValue();
            OracleJsonValue.OracleJsonType type = value.getOracleJsonType();
            switch (type) {
                case STRING: {
                    metadata.put(key, value.asJsonString().getString());
                    continue block10;
                }
                case DECIMAL: {
                    OracleJsonDecimal decimal = value.asJsonDecimal();
                    switch (decimal.getTargetType()) {
                        case INT: {
                            metadata.put(key, decimal.intValue());
                            continue block10;
                        }
                        case LONG: {
                            metadata.put(key, decimal.longValue());
                            continue block10;
                        }
                    }
                    metadata.put(key, decimal.toString());
                    continue block10;
                }
                case FLOAT: {
                    metadata.put(key, value.asJsonFloat().floatValue());
                    continue block10;
                }
                case DOUBLE: {
                    metadata.put(key, value.asJsonDouble().doubleValue());
                    continue block10;
                }
            }
            metadata.put(key, value.toString());
        }
        return metadata;
    }

    private static RuntimeException uncheckSQLException(SQLException sqlException) {
        return sqlException instanceof BatchUpdateException ? OracleEmbeddingStore.uncheckSQLException((BatchUpdateException)sqlException) : new RuntimeException(sqlException);
    }

    private static RuntimeException uncheckSQLException(BatchUpdateException batchUpdateException) {
        SQLException firstFailure = batchUpdateException.getNextException();
        return new RuntimeException(firstFailure == null ? batchUpdateException : firstFailure);
    }

    private static <T> T ensureIndexNotNull(List<T> list, int index, String name) {
        T value = list.get(index);
        if (value != null) {
            return value;
        }
        throw new IllegalArgumentException("null entry at index " + index + " in " + name);
    }

    private static IllegalArgumentException unrecognizedMetadata(String key, Object value) {
        return new IllegalArgumentException("Unrecognized object type in Metadata with key \"" + key + "\" and value \"" + value + "\" of class " + value.getClass().getSimpleName());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private DataSource dataSource;
        private EmbeddingTable embeddingTable;
        private boolean isExactSearch = false;
        private Index[] indexes;

        private Builder() {
        }

        public Builder dataSource(DataSource dataSource) {
            this.dataSource = (DataSource)ValidationUtils.ensureNotNull((Object)dataSource, (String)"dataSource");
            return this;
        }

        public Builder embeddingTable(String tableName) {
            return this.embeddingTable(tableName, CreateOption.CREATE_NONE);
        }

        public Builder embeddingTable(String tableName, CreateOption createOption) {
            ValidationUtils.ensureNotNull((Object)tableName, (String)"tableName");
            ValidationUtils.ensureNotNull((Object)((Object)createOption), (String)"createOption");
            return this.embeddingTable(EmbeddingTable.builder().name(tableName).createOption(createOption).build());
        }

        public Builder embeddingTable(EmbeddingTable embeddingTable) {
            ValidationUtils.ensureNotNull((Object)embeddingTable, (String)"embeddingTable");
            this.embeddingTable = embeddingTable;
            return this;
        }

        public Builder vectorIndex(CreateOption createOption) {
            return this.index(((IVFIndexBuilder)Index.ivfIndexBuilder().createOption(createOption)).build());
        }

        public Builder index(Index ... indexes) {
            this.indexes = indexes;
            return this;
        }

        public Builder exactSearch(boolean isExactSearch) {
            this.isExactSearch = isExactSearch;
            return this;
        }

        public OracleEmbeddingStore build() {
            ValidationUtils.ensureNotNull((Object)this.dataSource, (String)"dataSource");
            ValidationUtils.ensureNotNull((Object)this.embeddingTable, (String)"embeddingTable");
            return new OracleEmbeddingStore(this);
        }
    }
}

