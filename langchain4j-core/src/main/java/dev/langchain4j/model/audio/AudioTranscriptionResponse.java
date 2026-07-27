/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.audio;

import dev.langchain4j.Experimental;

@Experimental
public class AudioTranscriptionResponse {
    private final String text;

    public AudioTranscriptionResponse(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }

    public static AudioTranscriptionResponse from(String text) {
        return new AudioTranscriptionResponse(text);
    }
}

