/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.openai.models.embeddings.EmbeddingModel
 */
package dev.langchain4j.model.openaiofficial;

import com.openai.models.embeddings.EmbeddingModel;
import java.util.HashMap;
import java.util.Map;

enum OpenAiOfficialEmbeddingModelName {
    TEXT_EMBEDDING_3_SMALL(EmbeddingModel.TEXT_EMBEDDING_3_SMALL.value().toString(), 1536),
    TEXT_EMBEDDING_3_LARGE(EmbeddingModel.TEXT_EMBEDDING_3_LARGE.value().toString(), 3072),
    TEXT_EMBEDDING_ADA_002(EmbeddingModel.TEXT_EMBEDDING_ADA_002.value().toString(), 1536);

    private final String stringValue;
    private final Integer dimension;
    private static final Map<String, Integer> KNOWN_DIMENSION;

    private OpenAiOfficialEmbeddingModelName(String stringValue, Integer dimension) {
        this.stringValue = stringValue;
        this.dimension = dimension;
    }

    public String toString() {
        return this.stringValue;
    }

    Integer dimension() {
        return this.dimension;
    }

    static Integer knownDimension(String modelName) {
        return KNOWN_DIMENSION.get(modelName);
    }

    static {
        KNOWN_DIMENSION = new HashMap<String, Integer>(OpenAiOfficialEmbeddingModelName.values().length);
        for (OpenAiOfficialEmbeddingModelName embeddingModelName : OpenAiOfficialEmbeddingModelName.values()) {
            KNOWN_DIMENSION.put(embeddingModelName.toString(), embeddingModelName.dimension());
        }
    }
}

