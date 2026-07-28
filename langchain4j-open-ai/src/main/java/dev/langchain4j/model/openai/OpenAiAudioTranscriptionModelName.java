/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 */
package dev.langchain4j.model.openai;

import dev.langchain4j.Experimental;

@Experimental
public enum OpenAiAudioTranscriptionModelName {
    WHISPER_1("whisper-1"),
    GPT_4_O_TRANSCRIBE("gpt-4o-transcribe"),
    GPT_4_O_MINI_TRANSCRIBE("gpt-4o-mini-transcribe"),
    GPT_4_O_TRANSCRIBE_DIARIZE("gpt-4o-transcribe-diarize");

    private final String stringValue;

    private OpenAiAudioTranscriptionModelName(String stringValue) {
        this.stringValue = stringValue;
    }

    public String toString() {
        return this.stringValue;
    }
}

