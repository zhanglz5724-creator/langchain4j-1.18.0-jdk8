/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.ValidationUtils;

public class CosineSimilarity {
    public static final float EPSILON = 1.0E-8f;

    private CosineSimilarity() {
    }

    public static double between(Embedding embeddingA, Embedding embeddingB) {
        ValidationUtils.ensureNotNull(embeddingA, "embeddingA");
        ValidationUtils.ensureNotNull(embeddingB, "embeddingB");
        float[] vectorA = embeddingA.vector();
        float[] vectorB = embeddingB.vector();
        if (vectorA.length != vectorB.length) {
            throw Exceptions.illegalArgument("Length of vector a (%s) must be equal to the length of vector b (%s)", vectorA.length, vectorB.length);
        }
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; ++i) {
            dotProduct += (double)(vectorA[i] * vectorB[i]);
            normA += (double)(vectorA[i] * vectorA[i]);
            normB += (double)(vectorB[i] * vectorB[i]);
        }
        return dotProduct / Math.max(Math.sqrt(normA) * Math.sqrt(normB), (double)1.0E-8f);
    }

    public static double fromRelevanceScore(double relevanceScore) {
        return relevanceScore * 2.0 - 1.0;
    }
}

