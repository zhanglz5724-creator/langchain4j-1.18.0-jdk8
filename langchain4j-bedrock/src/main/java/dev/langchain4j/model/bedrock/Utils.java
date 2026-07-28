/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.ValidationUtils
 *  software.amazon.awssdk.services.bedrockruntime.model.ImageFormat
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.Internal;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.ValidationUtils;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import software.amazon.awssdk.services.bedrockruntime.model.ImageFormat;

@Internal
class Utils {
    private static final Map<String, ImageFormat> MIME_TYPE_MAPPING;
    private static final Map<String, ImageFormat> EXTENSION_MAPPING;
    private static final Set<ImageFormat> SUPPORTED_FORMATS;

    Utils() {
    }

    public static String extractExtension(URI uri) {
        int lastDot;
        String path = uri.getPath();
        if (Objects.isNull(path) || path.isEmpty()) {
            return "";
        }
        String cleanPath = path.split("\\?")[0];
        String fileName = cleanPath = cleanPath.split("#")[0];
        int lastSlash = Math.max(cleanPath.lastIndexOf(47), cleanPath.lastIndexOf(92));
        if (lastSlash >= 0) {
            fileName = cleanPath.substring(lastSlash + 1);
        }
        if ((lastDot = fileName.lastIndexOf(46)) > 0) {
            return fileName.substring(lastDot + 1);
        }
        return "";
    }

    public static String extractAndValidateFormat(Image image) {
        String extension;
        ImageFormat format;
        ImageFormat format2;
        ValidationUtils.ensureNotNull((Object)image, (String)"image");
        if (image.mimeType() != null && !image.mimeType().trim().isEmpty() && (format2 = MIME_TYPE_MAPPING.get(image.mimeType().toLowerCase())) != null) {
            return format2.toString();
        }
        if (image.url() != null && (format = EXTENSION_MAPPING.get(extension = Utils.extractExtension(image.url()).toLowerCase())) != null) {
            return format.toString();
        }
        throw new UnsupportedFeatureException(String.format("Unsupported image format, should be one of png | jpeg | gif | webp. Mime type: %s, URI: %s", image.mimeType(), image.url()));
    }

    public static String extractCleanFileName(URI uri) {
        String fileName;
        if (uri == null) {
            return "";
        }
        String path = uri.getPath();
        if (Objects.isNull(path) || path.isEmpty()) {
            return "";
        }
        String cleanPath = path.split("\\?")[0];
        int lastSlash = Math.max((cleanPath = cleanPath.split("#")[0]).lastIndexOf(47), cleanPath.lastIndexOf(92));
        String string = fileName = lastSlash >= 0 ? cleanPath.substring(lastSlash + 1) : cleanPath;
        if (fileName.isEmpty() || fileName.matches("^[.]+$")) {
            return "";
        }
        int lastDot = fileName.lastIndexOf(46);
        if (lastDot > 0) {
            fileName = fileName.substring(0, lastDot);
        }
        return fileName.replaceAll("[^a-zA-Z0-9\\s\\-()\\[\\]]", "-").replaceAll("\\s+", "-").trim();
    }

    static {
        HashMap<String, ImageFormat> map = new HashMap<String, ImageFormat>();
        map.put("image/png", ImageFormat.PNG);
        map.put("image/jpeg", ImageFormat.JPEG);
        map.put("image/jpg", ImageFormat.JPEG);
        map.put("image/gif", ImageFormat.GIF);
        map.put("image/webp", ImageFormat.WEBP);
        MIME_TYPE_MAPPING = Collections.unmodifiableMap(map);
        map = new HashMap();
        map.put("png", ImageFormat.PNG);
        map.put("jpg", ImageFormat.JPEG);
        map.put("jpeg", ImageFormat.JPEG);
        map.put("gif", ImageFormat.GIF);
        map.put("webp", ImageFormat.WEBP);
        EXTENSION_MAPPING = Collections.unmodifiableMap(map);
        HashSet<ImageFormat> set = new HashSet<ImageFormat>();
        set.add(ImageFormat.PNG);
        set.add(ImageFormat.JPEG);
        set.add(ImageFormat.GIF);
        set.add(ImageFormat.WEBP);
        SUPPORTED_FORMATS = Collections.unmodifiableSet(set);
    }
}

