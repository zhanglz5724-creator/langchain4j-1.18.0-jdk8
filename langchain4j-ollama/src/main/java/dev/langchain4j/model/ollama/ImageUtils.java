/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.model.ollama;

import dev.langchain4j.Internal;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.Utils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Internal
class ImageUtils {
    private static final List<String> SUPPORTED_URL_SCHEMES = Arrays.asList("http", "https", "file");

    ImageUtils() {
    }

    static List<String> base64EncodeImageList(List<ImageContent> contentList) {
        return contentList.stream().map(ImageContent::image).map(ImageUtils::base64Image).collect(Collectors.toList());
    }

    static String base64Image(Image image) {
        if (image.base64Data() != null && !image.base64Data().isEmpty()) {
            return image.base64Data();
        }
        if (SUPPORTED_URL_SCHEMES.contains(image.url().getScheme())) {
            return image.url().getScheme().startsWith("http") ? ImageUtils.httpScheme(image) : ImageUtils.fileScheme(image);
        }
        throw new UnsupportedFeatureException("Ollama integration only supports http/https and file urls. Unsupported url scheme: " + image.url().getScheme());
    }

    private static String httpScheme(Image image) {
        byte[] imageBytes = Utils.readBytes((String)image.url().toString());
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private static String fileScheme(Image image) {
        byte[] fileBytes = ImageUtils.readAllBytes(Paths.get(image.url()));
        return Base64.getEncoder().encodeToString(fileBytes);
    }

    private static byte[] readAllBytes(Path path) {
        try {
            return Files.readAllBytes(path);
        }
        catch (IOException e) {
            throw new RuntimeException(String.format("Can't read file with path '%s'", path), e);
        }
    }
}

