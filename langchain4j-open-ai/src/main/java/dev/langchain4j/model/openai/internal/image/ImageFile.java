/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.model.openai.internal.image;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Base64;

public class ImageFile {
    private final Image image;

    private ImageFile(Image image) {
        this.image = (Image)ValidationUtils.ensureNotNull((Object)image, (String)"image");
    }

    public String fileName() {
        return "image" + ImageFile.getImageExtension(this.image.mimeType());
    }

    public String mimeType() {
        return this.image.mimeType() != null ? this.image.mimeType() : "image/png";
    }

    public byte[] content() {
        if (this.image.base64Data() != null) {
            try {
                return Base64.getDecoder().decode(this.image.base64Data());
            }
            catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid base64 image data provided", e);
            }
        }
        if (this.image.url() != null) {
            throw new IllegalArgumentException("URL-based image is not supported by OpenAI image editing. Please provide the image as base64 encoded data.");
        }
        throw new IllegalArgumentException("No image data found. Image must contain base64 data");
    }

    public static ImageFile from(Image image) {
        return new ImageFile(image);
    }

    private static String getImageExtension(String mimeType) {
        if (mimeType == null) {
            return ".png";
        }
        switch (mimeType) {
            case "image/png": {
                return ".png";
            }
            case "image/jpeg": 
            case "image/jpg": {
                return ".jpg";
            }
            case "image/webp": {
                return ".webp";
            }
        }
        return ".png";
    }
}

