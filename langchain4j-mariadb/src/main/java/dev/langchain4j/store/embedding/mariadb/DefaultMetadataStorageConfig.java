/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NonNull
 */
package dev.langchain4j.store.embedding.mariadb;

import dev.langchain4j.store.embedding.mariadb.MetadataStorageConfig;
import dev.langchain4j.store.embedding.mariadb.MetadataStorageMode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public class DefaultMetadataStorageConfig
implements MetadataStorageConfig {
    private final MetadataStorageMode storageMode;
    private final List<String> columnDefinitions;
    private final List<String> indexes;

    public DefaultMetadataStorageConfig(MetadataStorageMode storageMode, List<String> columnDefinitions, List<String> indexes) {
        this.storageMode = storageMode;
        this.columnDefinitions = columnDefinitions;
        this.indexes = indexes;
    }

    @Override
    public MetadataStorageMode storageMode() {
        return this.storageMode;
    }

    @Override
    public List<String> columnDefinitions() {
        return this.columnDefinitions;
    }

    @Override
    public List<String> indexes() {
        return this.indexes;
    }

    public static MetadataStorageConfig defaultConfig() {
        return DefaultMetadataStorageConfig.builder().storageMode(MetadataStorageMode.COMBINED_JSON).columnDefinitions(Collections.singletonList("metadata JSON NULL")).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultMetadataStorageConfig)) {
            return false;
        }
        DefaultMetadataStorageConfig that = (DefaultMetadataStorageConfig)o;
        return Objects.equals((Object)this.storageMode, (Object)that.storageMode) && Objects.equals(this.columnDefinitions, that.columnDefinitions) && Objects.equals(this.indexes, that.indexes);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.storageMode, this.columnDefinitions, this.indexes});
    }

    public String toString() {
        return "DefaultMetadataStorageConfig[storageMode=" + (Object)((Object)this.storageMode) + ", columnDefinitions=" + this.columnDefinitions + ", indexes=" + this.indexes + "]";
    }

    public static final class Builder {
        private MetadataStorageMode storageMode;
        private List<String> columnDefinitions;
        private List<String> indexes;

        public @NonNull Builder storageMode(@NonNull MetadataStorageMode storageMode) {
            this.storageMode = storageMode;
            return this;
        }

        public @NonNull Builder columnDefinitions(@NonNull List<String> columnDefinitions) {
            this.columnDefinitions = columnDefinitions;
            return this;
        }

        public @NonNull Builder indexes(@NonNull List<String> indexes) {
            this.indexes = indexes;
            return this;
        }

        public @NonNull DefaultMetadataStorageConfig build() {
            return new DefaultMetadataStorageConfig(this.storageMode, this.columnDefinitions, this.indexes);
        }
    }
}

