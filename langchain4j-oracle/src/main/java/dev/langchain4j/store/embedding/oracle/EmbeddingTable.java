/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.ValidationUtils
 *  oracle.jdbc.OracleType
 */
package dev.langchain4j.store.embedding.oracle;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.oracle.CreateOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import oracle.jdbc.OracleType;

public final class EmbeddingTable {
    private final CreateOption createOption;
    private final String name;
    private final String idColumn;
    private final String embeddingColumn;
    private final String textColumn;
    private final String metadataColumn;

    private EmbeddingTable(Builder builder) {
        this.createOption = builder.createOption;
        this.name = builder.name;
        this.idColumn = builder.idColumn;
        this.embeddingColumn = builder.embeddingColumn;
        this.textColumn = builder.textColumn;
        this.metadataColumn = builder.metadataColumn;
    }

    void create(DataSource dataSource) throws SQLException {
        if (this.createOption == CreateOption.CREATE_NONE) {
            return;
        }
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();){
            if (this.createOption == CreateOption.CREATE_OR_REPLACE) {
                statement.addBatch("DROP TABLE IF EXISTS " + this.name);
            }
            statement.addBatch("CREATE TABLE IF NOT EXISTS " + this.name + "(" + this.idColumn + " VARCHAR(36) NOT NULL, " + this.embeddingColumn + " VECTOR(*, FLOAT32) NOT NULL, " + this.textColumn + " CLOB, " + this.metadataColumn + " JSON, PRIMARY KEY (" + this.idColumn + "))");
            statement.executeBatch();
        }
    }

    String mapMetadataKey(String key, OracleType type) {
        String typeName = type == OracleType.BINARY_FLOAT ? "BINARY_FLOAT" : (type == OracleType.BINARY_DOUBLE ? "BINARY_DOUBLE" : type.getName());
        return "JSON_VALUE(" + this.metadataColumn + ", '$." + key + "' RETURNING " + typeName + " NULL ON ERROR)";
    }

    public String name() {
        return this.name;
    }

    public String idColumn() {
        return this.idColumn;
    }

    public String embeddingColumn() {
        return this.embeddingColumn;
    }

    public String textColumn() {
        return this.textColumn;
    }

    public String metadataColumn() {
        return this.metadataColumn;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private CreateOption createOption = CreateOption.CREATE_NONE;
        private String name;
        private String idColumn = "id";
        private String embeddingColumn = "embedding";
        private String textColumn = "text";
        private String metadataColumn = "metadata";

        private Builder() {
        }

        public Builder createOption(CreateOption createOption) {
            ValidationUtils.ensureNotNull((Object)((Object)createOption), (String)"createOption");
            this.createOption = createOption;
            return this;
        }

        public Builder name(String name) {
            ValidationUtils.ensureNotNull((Object)name, (String)"name");
            this.name = name;
            return this;
        }

        public Builder idColumn(String idColumn) {
            ValidationUtils.ensureNotNull((Object)idColumn, (String)"idColumn");
            this.idColumn = idColumn;
            return this;
        }

        public Builder embeddingColumn(String embeddingColumn) {
            ValidationUtils.ensureNotNull((Object)embeddingColumn, (String)"embeddingColumn");
            this.embeddingColumn = embeddingColumn;
            return this;
        }

        public Builder textColumn(String textColumn) {
            ValidationUtils.ensureNotNull((Object)textColumn, (String)"textColumn");
            this.textColumn = textColumn;
            return this;
        }

        public Builder metadataColumn(String metadataColumn) {
            ValidationUtils.ensureNotNull((Object)metadataColumn, (String)"metadataColumn");
            this.metadataColumn = metadataColumn;
            return this;
        }

        public EmbeddingTable build() {
            ValidationUtils.ensureNotNull((Object)this.name, (String)"name");
            return new EmbeddingTable(this);
        }
    }
}

