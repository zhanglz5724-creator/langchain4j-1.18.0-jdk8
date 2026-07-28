/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.store.embedding.oracle;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.oracle.CreateOption;
import dev.langchain4j.store.embedding.oracle.EmbeddingTable;
import dev.langchain4j.store.embedding.oracle.IVFIndexBuilder;
import dev.langchain4j.store.embedding.oracle.IndexBuilder;
import dev.langchain4j.store.embedding.oracle.JSONIndexBuilder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

public class Index {
    private IndexBuilder builder;
    private String tableName;

    Index(IndexBuilder builder) {
        this.builder = builder;
    }

    public static IVFIndexBuilder ivfIndexBuilder() {
        return new IVFIndexBuilder();
    }

    public static JSONIndexBuilder jsonIndexBuilder() {
        return new JSONIndexBuilder();
    }

    public String name() {
        return this.builder.indexName;
    }

    public String tableName() {
        return this.tableName;
    }

    void create(DataSource dataSource, EmbeddingTable embeddingTable) throws SQLException {
        ValidationUtils.ensureNotNull((Object)dataSource, (String)"dataSource");
        ValidationUtils.ensureNotNull((Object)embeddingTable, (String)"embeddingTable");
        this.tableName = embeddingTable.name();
        if (this.builder.createOption == CreateOption.CREATE_NONE) {
            return;
        }
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();){
            if (this.builder.createOption == CreateOption.CREATE_OR_REPLACE) {
                statement.addBatch(this.builder.getDropIndexStatement(embeddingTable));
            }
            statement.addBatch(this.builder.getCreateIndexStatement(embeddingTable));
            statement.executeBatch();
        }
    }
}

