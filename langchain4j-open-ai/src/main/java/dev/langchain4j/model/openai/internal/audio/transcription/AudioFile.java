/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.audio.Audio
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.model.openai.internal.audio.transcription;

import dev.langchain4j.data.audio.Audio;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Base64;

public class AudioFile {
    private final Audio audio;

    private AudioFile(Audio audio) {
        this.audio = (Audio)ValidationUtils.ensureNotNull((Object)audio, (String)"audio");
    }

    public String fileName() {
        return "audio_file" + this.getAudioExtension(this.audio.mimeType());
    }

    public String mimeType() {
        return this.audio.mimeType();
    }

    public byte[] content() {
        if (this.audio.binaryData() != null) {
            return this.audio.binaryData();
        }
        if (this.audio.base64Data() != null) {
            try {
                return Base64.getDecoder().decode(this.audio.base64Data());
            }
            catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid base64 audio data provided", e);
            }
        }
        if (this.audio.url() != null) {
            throw new IllegalArgumentException("URL-based audio is not supported by OpenAI transcription. Please provide audio as binary data or base64 encoded data.");
        }
        throw new IllegalArgumentException("No audio data found. Audio must contain either binary data, base64 data");
    }

    public static AudioFile from(Audio audio) {
        return new AudioFile(audio);
    }

    private String getAudioExtension(String mimeType) {
        if (mimeType == null) {
            return "";
        }
        switch (mimeType) {
            case "audio/flac": {
                return ".flac";
            }
            case "audio/mpeg": 
            case "audio/mpeg3": {
                return ".mp3";
            }
            case "audio/mp4": 
            case "video/mp4": {
                return ".mp4";
            }
            case "audio/mpga": {
                return ".mpga";
            }
            case "audio/m4a": {
                return ".m4a";
            }
            case "audio/ogg": {
                return ".ogg";
            }
            case "audio/x-wav": 
            case "audio/wave": 
            case "audio/wav": {
                return ".wav";
            }
            case "audio/webm": 
            case "video/webm": {
                return ".webm";
            }
            case "audio/x-mpegurl": 
            case "audio/mpegurl": {
                return ".m3u";
            }
        }
        return "";
    }
}

