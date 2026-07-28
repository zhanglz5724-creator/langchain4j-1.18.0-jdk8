/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.mongodb;

import java.util.HashSet;
import java.util.Set;

public class IndexMapping {
    private final int dimension;
    private final Set<String> metadataFieldNames;

    public IndexMapping(int dimension, Set<String> metadataFieldNames) {
        this.dimension = dimension;
        this.metadataFieldNames = new HashSet<String>(metadataFieldNames);
    }

    public static IndexMapping defaultIndexMapping() {
        return IndexMapping.builder().dimension(1536).metadataFieldNames(new HashSet<String>()).build();
    }

    public int getDimension() {
        return this.dimension;
    }

    public Set<String> getMetadataFieldNames() {
        return this.metadataFieldNames;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int dimension;
        private Set<String> metadataFieldNames;

        public Builder dimension(int dimension) {
            this.dimension = dimension;
            return this;
        }

        public Builder metadataFieldNames(Set<String> metadataFieldNames) {
            this.metadataFieldNames = metadataFieldNames;
            return this;
        }

        public IndexMapping build() {
            return new IndexMapping(this.dimension, this.metadataFieldNames);
        }
    }
}

