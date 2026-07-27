/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.message;

import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.video.Video;
import dev.langchain4j.internal.ContentUtil;
import dev.langchain4j.internal.ValidationUtils;
import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;

public class VideoContent
implements Content {
    private final Video video;

    @Override
    public ContentType type() {
        return ContentType.VIDEO;
    }

    public VideoContent(URI url) {
        this.video = Video.builder().url(ValidationUtils.ensureNotNull(url, "url")).build();
    }

    public VideoContent(String url) {
        this(URI.create(url));
    }

    public VideoContent(String base64Data, String mimeType) {
        this.video = Video.builder().base64Data(ValidationUtils.ensureNotBlank(base64Data, "base64data")).mimeType(ValidationUtils.ensureNotBlank(mimeType, "mimeType")).build();
    }

    public VideoContent(Video video) {
        this.video = video;
    }

    public Video video() {
        return this.video;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        VideoContent that = (VideoContent)o;
        return Objects.equals(this.video, that.video);
    }

    public int hashCode() {
        return Objects.hash(this.video);
    }

    public String toString() {
        return "VideoContent { video = " + this.video + " }";
    }

    public static VideoContent from(URI url) {
        return new VideoContent(url);
    }

    public static VideoContent from(String url) {
        return new VideoContent(url);
    }

    public static VideoContent from(String base64Data, String mimeType) {
        return new VideoContent(base64Data, mimeType);
    }

    public static VideoContent from(Path videoFilePath, String mimeType) {
        return VideoContent.from(ContentUtil.extractBase64Content(videoFilePath), mimeType);
    }

    public static VideoContent from(Video video) {
        return new VideoContent(video);
    }
}

