/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.audio;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.ValidationUtils;

@Experimental
public class TextToSpeechRequest {
    private final String text;
    private final String voice;

    private TextToSpeechRequest(Builder builder) {
        this.text = ValidationUtils.ensureNotBlank(builder.text, "text");
        this.voice = builder.voice;
    }

    public String text() {
        return this.text;
    }

    public String voice() {
        return this.voice;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(String text) {
        return new Builder().text(text);
    }

    public static class Builder {
        private String text;
        private String voice;

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder voice(String voice) {
            this.voice = voice;
            return this;
        }

        public TextToSpeechRequest build() {
            return new TextToSpeechRequest(this);
        }
    }
}

