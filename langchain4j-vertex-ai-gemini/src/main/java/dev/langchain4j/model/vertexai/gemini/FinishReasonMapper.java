/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.cloud.vertexai.api.Candidate$FinishReason
 *  dev.langchain4j.model.output.FinishReason
 */
package dev.langchain4j.model.vertexai.gemini;

import com.google.cloud.vertexai.api.Candidate;
import dev.langchain4j.model.output.FinishReason;

class FinishReasonMapper {
    FinishReasonMapper() {
    }

    static FinishReason map(Candidate.FinishReason finishReason) {
        switch (finishReason) {
            case STOP: {
                return FinishReason.STOP;
            }
            case MAX_TOKENS: {
                return FinishReason.LENGTH;
            }
            case SAFETY: 
            case RECITATION: 
            case BLOCKLIST: 
            case PROHIBITED_CONTENT: 
            case SPII: {
                return FinishReason.CONTENT_FILTER;
            }
        }
        return FinishReason.OTHER;
    }
}

