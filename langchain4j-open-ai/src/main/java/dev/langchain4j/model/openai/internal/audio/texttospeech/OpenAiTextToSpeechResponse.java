/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.openai.internal.audio.texttospeech;

public class OpenAiTextToSpeechResponse {
    private final byte[] audio;
    private final String contentType;

    public OpenAiTextToSpeechResponse(byte[] audio, String contentType) {
        this.audio = audio;
        this.contentType = contentType;
    }

    public byte[] audio() {
        return this.audio;
    }

    public String contentType() {
        return this.contentType;
    }

    public static OpenAiTextToSpeechResponse from(byte[] audio, String contentType) {
        return new OpenAiTextToSpeechResponse(audio, contentType);
    }
}

