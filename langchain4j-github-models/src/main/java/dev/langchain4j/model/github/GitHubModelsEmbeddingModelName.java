/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.github;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public enum GitHubModelsEmbeddingModelName {
    TEXT_EMBEDDING_3_SMALL("text-embedding-3-small", 1536),
    TEXT_EMBEDDING_3_LARGE("text-embedding-3-large", 3072),
    COHERE_EMBED_V3_ENGLISH("cohere-embed-v3-english", 1024),
    COHERE_EMBED_V3_MULTILINGUAL("cohere-embed-v3-multilingual", 1024);

    private final String modelName;
    private final Integer dimension;
    private static final Map<String, Integer> KNOWN_DIMENSION;

    private GitHubModelsEmbeddingModelName(String modelName, Integer dimension) {
        this.modelName = modelName;
        this.dimension = dimension;
    }

    public String toString() {
        return this.modelName;
    }

    static Integer knownDimension(String modelName) {
        return KNOWN_DIMENSION.get(modelName);
    }

    static {
        KNOWN_DIMENSION = new HashMap<String, Integer>(GitHubModelsEmbeddingModelName.values().length);
        for (GitHubModelsEmbeddingModelName embeddingModelName : GitHubModelsEmbeddingModelName.values()) {
            KNOWN_DIMENSION.put(embeddingModelName.toString(), embeddingModelName.dimension);
        }
    }
}

