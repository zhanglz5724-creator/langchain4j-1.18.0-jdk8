/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.message;

import dev.langchain4j.data.audio.Audio;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.internal.ContentUtil;
import dev.langchain4j.internal.ValidationUtils;
import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;

public class AudioContent
implements Content {
    private final Audio audio;

    @Override
    public ContentType type() {
        return ContentType.AUDIO;
    }

    public AudioContent(URI url) {
        this.audio = Audio.builder().url(ValidationUtils.ensureNotNull(url, "url")).build();
    }

    public AudioContent(String url) {
        this(URI.create(url));
    }

    public AudioContent(String base64Data, String mimeType) {
        this.audio = Audio.builder().base64Data(ValidationUtils.ensureNotBlank(base64Data, "base64data")).mimeType(ValidationUtils.ensureNotBlank(mimeType, "mimeType")).build();
    }

    public AudioContent(Audio audio) {
        this.audio = audio;
    }

    public Audio audio() {
        return this.audio;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AudioContent that = (AudioContent)o;
        return Objects.equals(this.audio, that.audio);
    }

    public int hashCode() {
        return Objects.hash(this.audio);
    }

    public String toString() {
        return "AudioContent { audio = " + this.audio + " }";
    }

    public static AudioContent from(URI url) {
        return new AudioContent(url);
    }

    public static AudioContent from(String url) {
        return new AudioContent(url);
    }

    public static AudioContent from(String base64Data, String mimeType) {
        return new AudioContent(base64Data, mimeType);
    }

    public static AudioContent from(Path audioFilePath, String mimeType) {
        return AudioContent.from(ContentUtil.extractBase64Content(audioFilePath), mimeType);
    }

    public static AudioContent from(Audio audio) {
        return new AudioContent(audio);
    }
}

