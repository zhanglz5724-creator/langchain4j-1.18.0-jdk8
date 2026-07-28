/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  io.pinecone.clients.Pinecone
 *  org.openapitools.db_control.client.model.DeletionProtection
 */
package dev.langchain4j.store.embedding.pinecone;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.pinecone.PineconeIndexConfig;
import io.pinecone.clients.Pinecone;
import org.openapitools.db_control.client.model.DeletionProtection;

public class PineconeServerlessIndexConfig
implements PineconeIndexConfig {
    private final Integer dimension;
    private final String cloud;
    private final String region;
    private final DeletionProtection deletionProtection;

    PineconeServerlessIndexConfig(Integer dimension, String cloud, String region, DeletionProtection deletionProtection) {
        this.dimension = (Integer)ValidationUtils.ensureNotNull((Object)dimension, (String)"dimension");
        this.cloud = ValidationUtils.ensureNotBlank((String)cloud, (String)"cloud");
        this.region = ValidationUtils.ensureNotBlank((String)region, (String)"region");
        this.deletionProtection = (DeletionProtection)Utils.getOrDefault((Object)deletionProtection, (Object)DeletionProtection.ENABLED);
    }

    @Deprecated
    PineconeServerlessIndexConfig(Integer dimension, String cloud, String region) {
        this(dimension, cloud, region, null);
    }

    @Override
    public void createIndex(Pinecone pinecone, String index) {
        ValidationUtils.ensureNotNull((Object)pinecone, (String)"pinecone");
        ValidationUtils.ensureNotNull((Object)index, (String)"index");
        pinecone.createServerlessIndex(index, "cosine", this.dimension.intValue(), this.cloud, this.region, this.deletionProtection);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer dimension;
        private String cloud;
        private String region;
        private DeletionProtection deletionProtection;

        public Builder dimension(Integer dimension) {
            this.dimension = dimension;
            return this;
        }

        public Builder cloud(String cloud) {
            this.cloud = cloud;
            return this;
        }

        public Builder region(String region) {
            this.region = region;
            return this;
        }

        public Builder deletionProtection(DeletionProtection deletionProtection) {
            this.deletionProtection = deletionProtection;
            return this;
        }

        public PineconeServerlessIndexConfig build() {
            return new PineconeServerlessIndexConfig(this.dimension, this.cloud, this.region, this.deletionProtection);
        }
    }
}

