/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.mariadb;

import dev.langchain4j.store.embedding.mariadb.MetadataStorageMode;
import java.util.List;

public interface MetadataStorageConfig {
    public MetadataStorageMode storageMode();

    public List<String> columnDefinitions();

    public List<String> indexes();
}

