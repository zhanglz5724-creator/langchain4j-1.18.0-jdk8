/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.filter.Filter
 *  org.mariadb.jdbc.Driver
 */
package dev.langchain4j.store.embedding.mariadb;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.mariadb.ColumnFilterMapper;
import dev.langchain4j.store.embedding.mariadb.MariaDbFilterMapper;
import dev.langchain4j.store.embedding.mariadb.MariaDbValidator;
import dev.langchain4j.store.embedding.mariadb.MetadataColumDefinition;
import dev.langchain4j.store.embedding.mariadb.MetadataHandler;
import dev.langchain4j.store.embedding.mariadb.MetadataStorageConfig;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.mariadb.jdbc.Driver;

class ColumnsMetadataHandler
implements MetadataHandler {
    private final List<MetadataColumDefinition> columnsDefinition;
    private final List<String> columnsName;
    private final List<String> escapedColumnsName;
    private final String insertClause;
    private final MariaDbFilterMapper filterMapper;
    private final List<String> indexes;

    public ColumnsMetadataHandler(MetadataStorageConfig config, List<String> sqlKeywords) {
        List columnsDefinitionList = (List)ValidationUtils.ensureNotEmpty(config.columnDefinitions(), (String)"Metadata definition");
        this.filterMapper = new ColumnFilterMapper();
        this.indexes = Utils.getOrDefault(config.indexes(), Collections.emptyList()).stream().map(key -> {
            if (sqlKeywords.contains(key.toLowerCase(Locale.ROOT))) {
                try {
                    return Driver.enquoteIdentifier((String)key, (boolean)true);
                }
                catch (SQLException sQLException) {
                    // empty catch block
                }
            }
            return key;
        }).collect(Collectors.toList());
        this.columnsDefinition = columnsDefinitionList.stream().map(str -> MetadataColumDefinition.from(str, sqlKeywords)).collect(Collectors.toList());
        this.escapedColumnsName = this.columnsDefinition.stream().map(MetadataColumDefinition::escapedName).collect(Collectors.toList());
        this.insertClause = ", " + this.escapedColumnsName.stream().map(c -> String.format("%s = VALUES(%s)", c, c)).collect(Collectors.joining(","));
        this.columnsName = this.columnsDefinition.stream().map(MetadataColumDefinition::name).collect(Collectors.toList());
    }

    @Override
    public String columnDefinitionsString() {
        return this.columnsDefinition.stream().map(MetadataColumDefinition::fullDefinition).collect(Collectors.joining(","));
    }

    @Override
    public List<String> escapedColumnsName() {
        return this.escapedColumnsName;
    }

    @Override
    public void createMetadataIndexes(Statement statement, String table) {
        StringBuilder sb = new StringBuilder();
        this.indexes.stream().map(String::trim).forEach(index -> sb.append(",").append(MariaDbValidator.validateAndEnquoteIdentifier(index, false)));
        String indexFields = sb.toString().substring(1);
        String indexSql = String.format("create index if not exists %s on %s ( %s )", (table + "_metadata_idx").replaceAll("[ \\`\"'\\\\\\P{Print}]", ""), table, indexFields);
        try {
            statement.executeUpdate(indexSql);
        }
        catch (SQLException e) {
            throw new RuntimeException(String.format("Cannot create indexes on %s: %s", indexFields, e));
        }
    }

    @Override
    public String insertClause() {
        return this.insertClause;
    }

    @Override
    public void setMetadata(PreparedStatement upsertStmt, Integer parameterInitialIndex, Metadata metadata) {
        int i = 0;
        Map meta = metadata.toMap();
        for (String c : this.columnsName) {
            try {
                upsertStmt.setObject(parameterInitialIndex + i, meta.get(c));
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

