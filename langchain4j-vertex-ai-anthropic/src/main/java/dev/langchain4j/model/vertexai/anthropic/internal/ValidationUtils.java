/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.vertexai.anthropic.internal;

public final class ValidationUtils {
    public static Integer validateMaxTokens(Integer maxTokens) {
        if (maxTokens == null) {
            return 4096;
        }
        if (maxTokens < 1 || maxTokens > 200000) {
            throw new IllegalArgumentException("maxTokens must be between 1 and 200000");
        }
        return maxTokens;
    }

    public static Double validateTemperature(Double temperature) {
        if (temperature == null) {
            return null;
        }
        if (temperature < 0.0 || temperature > 1.0) {
            throw new IllegalArgumentException("temperature must be between 0.0 and 1.0");
        }
        return temperature;
    }

    public static Double validateTopP(Double topP) {
        if (topP == null) {
            return null;
        }
        if (topP < 0.0 || topP > 1.0) {
            throw new IllegalArgumentException("topP must be between 0.0 and 1.0");
        }
        return topP;
    }

    public static Integer validateTopK(Integer topK) {
        if (topK == null) {
            return null;
        }
        if (topK < 1) {
            throw new IllegalArgumentException("topK must be greater than 0");
        }
        return topK;
    }
}

