/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding;

public class RelevanceScore {
    private RelevanceScore() {
    }

    public static double fromCosineSimilarity(double cosineSimilarity) {
        return (cosineSimilarity + 1.0) / 2.0;
    }
}

