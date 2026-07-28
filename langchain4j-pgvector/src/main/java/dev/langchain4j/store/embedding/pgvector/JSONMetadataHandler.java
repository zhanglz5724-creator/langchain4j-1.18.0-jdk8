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
package dev.langchain4j.store.embedding.pgvector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.pgvector.JSONFilterMapper;
import dev.langchain4j.store.embedding.pgvector.MetadataColumDefinition;
import dev.langchain4j.store.embedding.pgvector.MetadataHandler;
import dev.langchain4j.store.embedding.pgvector.MetadataStorageConfig;
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
    final MetadataColumDefinition columnDefinition;
    final String columnName;
    final JSONFilterMapper filterMapper;
    final List<String> indexes;

    public JSONMetadataHandler(MetadataStorageConfig config) {
        List definition = (List)ValidationUtils.ensureNotEmpty(config.columnDefinitions(), (String)"Metadata definition");
        if (definition.size() > 1) {
            throw new IllegalArgumentException("Metadata definition should be an unique column definition, example: metadata JSON NULL");
        }
        this.columnDefinition = MetadataColumDefinition.from((String)definition.get(0));
        this.columnName = this.columnDefinition.getName();
        this.filterMapper = new JSONFilterMapper(this.columnName);
        this.indexes = Utils.getOrDefault(config.indexes(), Collections.emptyList());
    }

    @Override
    public String columnDefinitionsString() {
        return this.columnDefinition.getFullDefinition();
    }

    @Override
    public List<String> columnsNames() {
        return Collections.singletonList(this.columnName);
    }

    @Override
    public void createMetadataIndexes(Statement statement, String table) {
        if (!this.indexes.isEmpty()) {
            throw new RuntimeException("Indexes are not allowed for JSON metadata, use JSONB instead");
        }
    }

    @Override
    public String whereClause(Filter filter) {
        return this.filterMapper.map(filter);
    }

    @Override
    public Metadata fromResultSet(ResultSet resultSet) {
        try {
            String metadataJson = (String)Utils.getOrDefault((Object)resultSet.getString(this.columnsNames().get(0)), (Object)"{}");
            return new Metadata((Map)OBJECT_MAPPER.readValue(metadataJson, Map.class));
        }
        catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String insertClause() {
        return String.format("%s = EXCLUDED.%s", this.columnName, this.columnName);
    }

    @Override
    public void setMetadata(PreparedStatement upsertStmt, Integer parameterInitialIndex, Metadata metadata) {
        try {
            upsertStmt.setObject((int)parameterInitialIndex, (Object)OBJECT_MAPPER.writeValueAsString((Object)Utils.toStringValueMap((Map)metadata.toMap())), 1111);
        }
        catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

