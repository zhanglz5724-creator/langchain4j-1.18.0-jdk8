/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.SerializationFeature
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.filter.Filter
 */
package dev.langchain4j.store.embedding.mariadb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.mariadb.JSONFilterMapper;
import dev.langchain4j.store.embedding.mariadb.MetadataColumDefinition;
import dev.langchain4j.store.embedding.mariadb.MetadataHandler;
import dev.langchain4j.store.embedding.mariadb.MetadataStorageConfig;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class JSONMetadataHandler
implements MetadataHandler {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    public static final String DEFAULT_COLUMN_METADATA = "metadata";
    private final MetadataColumDefinition columnDefinition;
    private final String escapedColumnsName;
    private final JSONFilterMapper filterMapper;
    private final List<String> indexes;

    public JSONMetadataHandler(MetadataStorageConfig config, List<String> sqlKeywords) {
        List definition = (List)ValidationUtils.ensureNotEmpty(config.columnDefinitions(), (String)"Metadata definition");
        if (definition.size() > 1) {
            throw new IllegalArgumentException("Metadata definition should be an unique column definition, example: metadata JSON NULL");
        }
        this.columnDefinition = MetadataColumDefinition.from((String)definition.get(0), sqlKeywords);
        this.escapedColumnsName = this.columnDefinition.escapedName() == null ? DEFAULT_COLUMN_METADATA : this.columnDefinition.escapedName();
        this.filterMapper = new JSONFilterMapper(this.escapedColumnsName);
        this.indexes = Utils.getOrDefault(config.indexes(), Collections.emptyList());
    }

    @Override
    public String columnDefinitionsString() {
        return this.columnDefinition.fullDefinition();
    }

    @Override
    public List<String> escapedColumnsName() {
        return Collections.singletonList(this.escapedColumnsName);
    }

    @Override
    public void createMetadataIndexes(Statement statement, String table) {
        if (!this.indexes.isEmpty()) {
            throw new RuntimeException("Indexes are actually not allowed for JSON metadata");
        }
    }

    @Override
    public String whereClause(Filter filter) {
        return this.filterMapper.map(filter);
    }

    @Override
    public Metadata fromResultSet(ResultSet resultSet) {
        try {
            String metadataJson = (String)Utils.getOrDefault((Object)resultSet.getString(5), (Object)"{}");
            return new Metadata((Map)OBJECT_MAPPER.readValue(metadataJson, Map.class));
        }
        catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String insertClause() {
        return ", " + String.format("%s = VALUES(%s)", this.escapedColumnsName, this.escapedColumnsName);
    }

    @Override
    public void setMetadata(PreparedStatement upsertStmt, Integer parameterInitialIndex, Metadata metadata) {
        try {
            String jsonValue = OBJECT_MAPPER.writeValueAsString((Object)metadata.toMap());
            upsertStmt.setString(parameterInitialIndex, jsonValue);
        }
        catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

