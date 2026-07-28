/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.pgvector;

import dev.langchain4j.store.embedding.pgvector.ColumnsMetadataHandler;
import dev.langchain4j.store.embedding.pgvector.JSONBMetadataHandler;
import dev.langchain4j.store.embedding.pgvector.JSONMetadataHandler;
import dev.langchain4j.store.embedding.pgvector.MetadataHandler;
import dev.langchain4j.store.embedding.pgvector.MetadataStorageConfig;

class MetadataHandlerFactory {
    static MetadataHandler get(MetadataStorageConfig config) {
        switch (config.storageMode()) {
            case COMBINED_JSON: {
                return new JSONMetadataHandler(config);
            }
            case COMBINED_JSONB: {
                return new JSONBMetadataHandler(config);
            }
            case COLUMN_PER_KEY: {
                return new ColumnsMetadataHandler(config);
            }
        }
        throw new RuntimeException(String.format("Type %s not handled.", new Object[]{config.storageMode()}));
    }
}

