/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.audio;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.audio.Audio;

@Experimental
public class AudioTranscriptionRequest {
    private final Audio audio;
    private final String prompt;
    private final String language;
    private final Double temperature;

    private AudioTranscriptionRequest(Builder builder) {
        this.audio = builder.audio;
        this.prompt = builder.prompt;
        this.language = builder.language;
        this.temperature = builder.temperature;
    }

    public Audio audio() {
        return this.audio;
    }

    public String prompt() {
        return this.prompt;
    }

    public String language() {
        return this.language;
    }

    public Double temperature() {
        return this.temperature;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Audio audio) {
        return new Builder().audio(audio);
    }

    public static class Builder {
        private Audio audio;
        private String prompt;
        private String language;
        private Double temperature;

        public Builder audio(Audio audio) {
            this.audio = audio;
            return this;
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public AudioTranscriptionRequest build() {
            if (this.audio == null) {
                throw new IllegalStateException("Audio must be provided");
            }
            return new AudioTranscriptionRequest(this);
        }
    }
}

