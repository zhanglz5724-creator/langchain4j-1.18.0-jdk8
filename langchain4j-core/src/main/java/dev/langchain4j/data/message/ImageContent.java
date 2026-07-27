/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.message;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.internal.ContentUtil;
import dev.langchain4j.internal.ValidationUtils;
import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;

public class ImageContent
implements Content {
    private final Image image;
    private final DetailLevel detailLevel;

    public ImageContent(URI url) {
        this(url, DetailLevel.LOW);
    }

    public ImageContent(String url) {
        this(URI.create(url));
    }

    public ImageContent(URI url, DetailLevel detailLevel) {
        this(Image.builder().url(ValidationUtils.ensureNotNull(url, "url")).build(), detailLevel);
    }

    public ImageContent(String url, DetailLevel detailLevel) {
        this(URI.create(url), detailLevel);
    }

    public ImageContent(String base64Data, String mimeType) {
        this(base64Data, mimeType, DetailLevel.LOW);
    }

    public ImageContent(String base64Data, String mimeType, DetailLevel detailLevel) {
        this(Image.builder().base64Data(ValidationUtils.ensureNotBlank(base64Data, "base64Data")).mimeType(ValidationUtils.ensureNotBlank(mimeType, "mimeType")).build(), detailLevel);
    }

    public ImageContent(Image image) {
        this(image, DetailLevel.LOW);
    }

    public ImageContent(Image image, DetailLevel detailLevel) {
        this.image = ValidationUtils.ensureNotNull(image, "image");
        this.detailLevel = ValidationUtils.ensureNotNull(detailLevel, "detailLevel");
    }

    public Image image() {
        return this.image;
    }

    public DetailLevel detailLevel() {
        return this.detailLevel;
    }

    @Override
    public ContentType type() {
        return ContentType.IMAGE;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ImageContent that = (ImageContent)o;
        return Objects.equals(this.image, that.image) && Objects.equals((Object)this.detailLevel, (Object)that.detailLevel);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.image, this.detailLevel});
    }

    public String toString() {
        return "ImageContent { image = " + this.image + " detailLevel = " + (Object)((Object)this.detailLevel) + " }";
    }

    public static ImageContent from(URI url) {
        return new ImageContent(url);
    }

    public static ImageContent from(String url) {
        return new ImageContent(url);
    }

    public static ImageContent from(URI url, DetailLevel detailLevel) {
        return new ImageContent(url, detailLevel);
    }

    public static ImageContent from(String url, DetailLevel detailLevel) {
        return new ImageContent(url, detailLevel);
    }

    public static ImageContent from(String base64Data, String mimeType) {
        return new ImageContent(base64Data, mimeType);
    }

    public static ImageContent from(String base64Data, String mimeType, DetailLevel detailLevel) {
        return new ImageContent(base64Data, mimeType, detailLevel);
    }

    public static ImageContent from(Path imageFilePath, String mimeType) {
        return ImageContent.from(ContentUtil.extractBase64Content(imageFilePath), mimeType);
    }

    public static ImageContent from(Path imageFilePath, String mimeType, DetailLevel detailLevel) {
        return ImageContent.from(ContentUtil.extractBase64Content(imageFilePath), mimeType, detailLevel);
    }

    public static ImageContent from(Image image) {
        return new ImageContent(image);
    }

    public static ImageContent from(Image image, DetailLevel detailLevel) {
        return new ImageContent(image, detailLevel);
    }

    public static enum DetailLevel {
        LOW,
        MEDIUM,
        HIGH,
        ULTRA_HIGH,
        AUTO;

    }
}

