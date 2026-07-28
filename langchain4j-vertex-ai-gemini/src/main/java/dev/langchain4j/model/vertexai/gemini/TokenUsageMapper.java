/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.cloud.vertexai.api.GenerateContentResponse$UsageMetadata
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.vertexai.gemini;

import com.google.cloud.vertexai.api.GenerateContentResponse;
import dev.langchain4j.model.output.TokenUsage;

class TokenUsageMapper {
    TokenUsageMapper() {
    }

    static TokenUsage map(GenerateContentResponse.UsageMetadata usageMetadata) {
        return new TokenUsage(Integer.valueOf(usageMetadata.getPromptTokenCount()), Integer.valueOf(usageMetadata.getCandidatesTokenCount()), Integer.valueOf(usageMetadata.getTotalTokenCount()));
    }
}

