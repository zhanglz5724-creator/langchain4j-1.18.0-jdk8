/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.pgvector;

import dev.langchain4j.store.embedding.pgvector.MetadataStorageMode;
import java.util.List;

public interface MetadataStorageConfig {
    public MetadataStorageMode storageMode();

    public List<String> columnDefinitions();

    public List<String> indexes();

    public String indexType();
}

