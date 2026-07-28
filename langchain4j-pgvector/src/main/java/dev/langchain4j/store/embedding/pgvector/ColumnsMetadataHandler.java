/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.filter.Filter
 */
package dev.langchain4j.store.embedding.pgvector;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.pgvector.ColumnFilterMapper;
import dev.langchain4j.store.embedding.pgvector.MetadataColumDefinition;
import dev.langchain4j.store.embedding.pgvector.MetadataHandler;
import dev.langchain4j.store.embedding.pgvector.MetadataStorageConfig;
import dev.langchain4j.store.embedding.pgvector.PgVectorFilterMapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class ColumnsMetadataHandler
implements MetadataHandler {
    final List<MetadataColumDefinition> columnsDefinition;
    final List<String> columnsName;
    final PgVectorFilterMapper filterMapper;
    final List<String> indexes;
    final String indexType;

    public ColumnsMetadataHandler(MetadataStorageConfig config) {
        List columnsDefinitionList = (List)ValidationUtils.ensureNotEmpty(config.columnDefinitions(), (String)"Metadata definition");
        this.columnsDefinition = columnsDefinitionList.stream().map(MetadataColumDefinition::from).collect(Collectors.toList());
        this.columnsName = this.columnsDefinition.stream().map(MetadataColumDefinition::getName).collect(Collectors.toList());
        this.filterMapper = new ColumnFilterMapper();
        this.indexes = Utils.getOrDefault(config.indexes(), Collections.emptyList());
        this.indexType = config.indexType();
    }

    @Override
    public String columnDefinitionsString() {
        return this.columnsDefinition.stream().map(MetadataColumDefinition::getFullDefinition).collect(Collectors.joining(","));
    }

    @Override
    public List<String> columnsNames() {
        return this.columnsName;
    }

    @Override
    public void createMetadataIndexes(Statement statement, String table) {
        String indexTypeSql = this.indexType == null ? "" : "USING " + this.indexType;
        this.indexes.stream().map(String::trim).forEach(index -> {
            String indexSql = String.format("create index if not exists %s_%s on %s %s ( %s )", table, index, table, indexTypeSql, index);
            try {
                statement.executeUpdate(indexSql);
            }
            catch (SQLException e) {
                throw new RuntimeException(String.format("Cannot create indexes %s: %s", index, e));
            }
        });
    }

    @Override
    public String insertClause() {
        return this.columnsName.stream().map(c -> String.format("%s = EXCLUDED.%s", c, c)).collect(Collectors.joining(","));
    }

    @Override
    public void setMetadata(PreparedStatement upsertStmt, Integer parameterInitialIndex, Metadata metadata) {
        Map metadataMap = metadata.toMap();
        int i = 0;
        for (String columnName : this.columnsName) {
            try {
                String metadataValue = Objects.toString(metadataMap.get(columnName), null);
                upsertStmt.setObject(parameterInitialIndex + i, (Object)metadataValue, 1111);
                ++i;
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String whereClause(Filter filter) {
        return this.filterMapper.map(filter);
    }

    @Override
    public Metadata fromResultSet(ResultSet resultSet) {
        try {
            HashMap<String, Object> metadataMap = new HashMap<String, Object>();
            for (String c : this.columnsName) {
                if (resultSet.getObject(c) == null) continue;
                metadataMap.put(c, resultSet.getObject(c));
            }
            return new Metadata(metadataMap);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

