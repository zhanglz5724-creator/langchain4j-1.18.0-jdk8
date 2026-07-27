/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.audio;

import dev.langchain4j.Experimental;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.audio.TextToSpeechRequest;
import dev.langchain4j.model.audio.TextToSpeechResponse;

@Experimental
public interface TextToSpeechModel {
    default public TextToSpeechResponse synthesize(String text) {
        return this.synthesize(TextToSpeechRequest.builder().text(text).build());
    }

    public TextToSpeechResponse synthesize(TextToSpeechRequest var1);

    default public ModelProvider provider() {
        return ModelProvider.OTHER;
    }
}

