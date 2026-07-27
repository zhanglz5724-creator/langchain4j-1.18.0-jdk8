/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.audio;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.audio.Audio;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.audio.AudioTranscriptionRequest;
import dev.langchain4j.model.audio.AudioTranscriptionResponse;

@Experimental
public interface AudioTranscriptionModel {
    public AudioTranscriptionResponse transcribe(AudioTranscriptionRequest var1);

    default public String transcribeToText(Audio audio) {
        AudioTranscriptionRequest request = AudioTranscriptionRequest.builder(audio).build();
        AudioTranscriptionResponse response = this.transcribe(request);
        return response.text();
    }

    default public ModelProvider provider() {
        return ModelProvider.OTHER;
    }
}

