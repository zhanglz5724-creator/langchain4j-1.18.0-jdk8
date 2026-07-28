/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.output.FinishReason
 */
package dev.langchain4j.model.googleai;

import dev.langchain4j.model.googleai.GeminiGenerateContentResponse;
import dev.langchain4j.model.output.FinishReason;

class FinishReasonMapper {
    FinishReasonMapper() {
    }

    static FinishReason fromGFinishReasonToFinishReason(GeminiGenerateContentResponse.GeminiCandidate.GeminiFinishReason geminiFinishReason) {
        switch (geminiFinishReason) {
            case STOP: {
                return FinishReason.STOP;
            }
            case BLOCKLIST: 
            case PROHIBITED_CONTENT: 
            case RECITATION: 
            case IMAGE_RECITATION: 
            case SPII: 
            case SAFETY: 
            case LANGUAGE: {
                return FinishReason.CONTENT_FILTER;
            }
            case MAX_TOKENS: {
                return FinishReason.LENGTH;
            }
            case MALFORMED_FUNCTION_CALL: 
            case FINISH_REASON_UNSPECIFIED: 
            case OTHER: {
                return FinishReason.OTHER;
            }
        }
        throw new IllegalArgumentException("Unknown GeminiFinishReason: " + (Object)((Object)geminiFinishReason));
    }
}

