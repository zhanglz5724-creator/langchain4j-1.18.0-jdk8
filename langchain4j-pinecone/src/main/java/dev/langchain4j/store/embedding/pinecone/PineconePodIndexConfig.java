/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.ValidationUtils
 *  io.pinecone.clients.Pinecone
 */
package dev.langchain4j.store.embedding.pinecone;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.pinecone.PineconeIndexConfig;
import io.pinecone.clients.Pinecone;

public class PineconePodIndexConfig
implements PineconeIndexConfig {
    private final Integer dimension;
    private final String environment;
    private final String podType;

    PineconePodIndexConfig(Integer dimension, String environment, String podType) {
        environment = (String)ValidationUtils.ensureNotNull((Object)environment, (String)"environment");
        podType = (String)ValidationUtils.ensureNotNull((Object)podType, (String)"podType");
        ValidationUtils.ensureNotNull((Object)dimension, (String)"dimension");
        this.dimension = dimension;
        this.environment = environment;
        this.podType = podType;
    }

    @Override
    public void createIndex(Pinecone pinecone, String index) {
        ValidationUtils.ensureNotNull((Object)index, (String)"index");
        ValidationUtils.ensureNotNull((Object)pinecone, (String)"pinecone");
        pinecone.createPodsIndex(index, this.dimension, this.environment, this.podType, "cosine");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer dimension;
        private String environment;
        private String podType;

        public Builder dimension(Integer dimension) {
            this.dimension = dimension;
            return this;
        }

        public Builder environment(String environment) {
            this.environment = environment;
            return this;
        }

        public Builder podType(String podType) {
            this.podType = podType;
            return this;
        }

        public PineconePodIndexConfig build() {
            return new PineconePodIndexConfig(this.dimension, this.environment, this.podType);
        }
    }
}

