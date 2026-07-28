/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.pgvector;

import dev.langchain4j.store.embedding.pgvector.JSONMetadataHandler;
import dev.langchain4j.store.embedding.pgvector.MetadataStorageConfig;
import java.sql.SQLException;
import java.sql.Statement;

class JSONBMetadataHandler
extends JSONMetadataHandler {
    final String indexType;

    public JSONBMetadataHandler(MetadataStorageConfig config) {
        super(config);
        if (!this.columnDefinition.getType().equals("jsonb")) {
            throw new RuntimeException("Your column definition type should be JSONB");
        }
        this.indexType = config.indexType();
    }

    @Override
    public void createMetadataIndexes(Statement statement, String table) {
        String indexTypeSql = this.indexType == null ? "" : "USING " + this.indexType;
        for (String str : this.indexes) {
            String index = str.trim();
            String indexName = this.formatIndex(index);
            try {
                String indexSql = String.format("create index if not exists %s_%s on %s %s (%s)", table, indexName, table, indexTypeSql, index);
                statement.executeUpdate(indexSql);
            }
            catch (SQLException e) {
                throw new RuntimeException(String.format("Cannot create index %s: %s", index, e));
            }
        }
    }

    String formatIndex(String index) {
        String indexName = index.contains("->") ? this.columnName + "_" + index.substring(index.indexOf("->") + 2, index.length() - 1).trim().replaceAll("'", "") : index.replaceAll(" ", "_");
        return indexName;
    }
}

