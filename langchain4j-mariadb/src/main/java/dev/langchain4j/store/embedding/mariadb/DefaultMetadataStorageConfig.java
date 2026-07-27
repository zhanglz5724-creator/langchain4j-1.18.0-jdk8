package dev.langchain4j.store.embedding.mariadb;

import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.NonNull;

/**
 * Metadata configuration implementation
 */
public class DefaultMetadataStorageConfig implements MetadataStorageConfig {
    private final MetadataStorageMode storageMode;
    private final List<String> columnDefinitions;
    private final List<String> indexes;

    public DefaultMetadataStorageConfig(MetadataStorageMode storageMode, List<String> columnDefinitions, List<String> indexes) {
        this.storageMode = storageMode;
        this.columnDefinitions = columnDefinitions;
        this.indexes = indexes;
    }

    public MetadataStorageMode getStorageMode() {
        return storageMode;
    }

    public List<String> getColumnDefinitions() {
        return columnDefinitions;
    }

    public List<String> getIndexes() {
        return indexes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultMetadataStorageConfig that = (DefaultMetadataStorageConfig) o;
        return java.util.Objects.equals(this.storageMode, that.storageMode) && java.util.Objects.equals(this.columnDefinitions, that.columnDefinitions) && java.util.Objects.equals(this.indexes, that.indexes);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(storageMode, columnDefinitions, indexes);
    }

    @Override
    public String toString() {
        return "DefaultMetadataStorageConfig{"storageMode=" + storageMode + , "columnDefinitions=" + columnDefinitions + , "indexes=" + indexes + "}"";
    }


    /**
     * Default configuration
     *
     * @return Default configuration
     */
    public static MetadataStorageConfig defaultConfig() {
        return builder()
                .storageMode(MetadataStorageMode.COMBINED_JSON)
                .columnDefinitions(Collections.singletonList("metadata JSON NULL"))
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private MetadataStorageMode storageMode;
        private List<String> columnDefinitions;
        private List<String> indexes;

        @NonNull
        public Builder storageMode(@NonNull MetadataStorageMode storageMode) {
            this.storageMode = storageMode;
            return this;
        }

        @NonNull
        public Builder columnDefinitions(@NonNull List<String> columnDefinitions) {
            this.columnDefinitions = columnDefinitions;
            return this;
        }

        @NonNull
        public Builder indexes(@NonNull List<String> indexes) {
            this.indexes = indexes;
            return this;
        }

        @NonNull
        public DefaultMetadataStorageConfig build() {
            return new DefaultMetadataStorageConfig(this.storageMode, this.columnDefinitions, this.indexes);
        }
    }
}
