/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.openai.internal.audio.transcription;

import dev.langchain4j.model.openai.internal.audio.transcription.AudioFile;

public class OpenAiAudioTranscriptionRequest {
    private final AudioFile file;
    private final String model;
    private final String language;
    private final String prompt;
    private final Double temperature;

    public OpenAiAudioTranscriptionRequest(Builder builder) {
        this.file = builder.file;
        this.model = builder.model;
        this.language = builder.language;
        this.prompt = builder.prompt;
        this.temperature = builder.temperature;
    }

    public AudioFile file() {
        return this.file;
    }

    public String model() {
        return this.model;
    }

    public String language() {
        return this.language;
    }

    public String prompt() {
        return this.prompt;
    }

    public Double temperature() {
        return this.temperature;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private AudioFile file;
        private String model;
        private String language;
        private String prompt;
        private Double temperature;

        public Builder file(AudioFile file) {
            this.file = file;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public OpenAiAudioTranscriptionRequest build() {
            return new OpenAiAudioTranscriptionRequest(this);
        }
    }
}

