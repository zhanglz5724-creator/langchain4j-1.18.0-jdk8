/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.mariadb;

import dev.langchain4j.store.embedding.mariadb.ColumnsMetadataHandler;
import dev.langchain4j.store.embedding.mariadb.JSONMetadataHandler;
import dev.langchain4j.store.embedding.mariadb.MetadataHandler;
import dev.langchain4j.store.embedding.mariadb.MetadataStorageConfig;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.sql.DataSource;

class MetadataHandlerFactory {
    static MetadataHandler get(MetadataStorageConfig config, DataSource dataSource) {
        List<String> sqlKeywords = new ArrayList<String>();
        try (Connection connection = dataSource.getConnection();){
            sqlKeywords = Arrays.stream(connection.getMetaData().getSQLKeywords().split(",")).map(str -> str.toLowerCase(Locale.ROOT)).collect(Collectors.toList());
        }
        catch (SQLException sQLException) {
            // empty catch block
        }
        switch (config.storageMode()) {
            case COMBINED_JSON: {
                return new JSONMetadataHandler(config, sqlKeywords);
            }
            case COLUMN_PER_KEY: {
                return new ColumnsMetadataHandler(config, sqlKeywords);
            }
        }
        throw new RuntimeException(String.format("Type %s not handled.", new Object[]{config.storageMode()}));
    }
}

