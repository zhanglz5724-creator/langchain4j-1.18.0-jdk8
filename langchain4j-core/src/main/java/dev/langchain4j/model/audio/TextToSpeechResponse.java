/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.audio;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.audio.Audio;
import dev.langchain4j.internal.ValidationUtils;

@Experimental
public class TextToSpeechResponse {
    private final Audio audio;

    public TextToSpeechResponse(Audio audio) {
        this.audio = ValidationUtils.ensureNotNull(audio, "audio");
    }

    public Audio audio() {
        return this.audio;
    }

    public static TextToSpeechResponse from(Audio audio) {
        return new TextToSpeechResponse(audio);
    }
}

