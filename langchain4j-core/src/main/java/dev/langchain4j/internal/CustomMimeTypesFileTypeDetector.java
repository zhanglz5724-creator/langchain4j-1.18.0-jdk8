/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.internal;

import dev.langchain4j.Internal;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.spi.FileTypeDetector;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Internal
public class CustomMimeTypesFileTypeDetector
extends FileTypeDetector {
    private static final Map<String, String> defaultMappings = new HashMap<String, String>();
    private final Map<String, String> mappings;

    public CustomMimeTypesFileTypeDetector() {
        this(defaultMappings);
    }

    public CustomMimeTypesFileTypeDetector(Map<String, String> customMapping) {
        this.mappings = Collections.unmodifiableMap(customMapping);
    }

    @Override
    public String probeContentType(Path path) {
        String extension = CustomMimeTypesFileTypeDetector.extension(path).toLowerCase();
        if (this.mappings.containsKey(extension)) {
            return this.mappings.get(extension);
        }
        try {
            return Files.probeContentType(path);
        }
        catch (IOException e) {
            return null;
        }
    }

    public String probeContentType(String path) {
        return this.probeContentType(Paths.get(path, new String[0]));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String probeContentType(URI uri) {
        Path path = Paths.get(uri.getPath(), new String[0]);
        String mimeTypeFromPath = this.probeContentType(path);
        if (mimeTypeFromPath != null) {
            return mimeTypeFromPath;
        }
        String mimeTypeGuessedFromUrlCon = URLConnection.guessContentTypeFromName(path.getFileName().toString());
        if (mimeTypeGuessedFromUrlCon != null) {
            return mimeTypeGuessedFromUrlCon;
        }
        try (BufferedInputStream in = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ));){
            String string = URLConnection.guessContentTypeFromStream(in);
            return string;
        }
        catch (IOException e) {
            return null;
        }
    }

    static String extension(Path path) {
        String fileName = path.getFileName().toFile().toString();
        int lastDotPosition = fileName.lastIndexOf(46);
        return lastDotPosition > 0 ? fileName.substring(lastDotPosition + 1) : "";
    }

    static {
        defaultMappings.put("pdf", "application/pdf");
        defaultMappings.put("yml", "text/yaml");
        defaultMappings.put("yaml", "text/yaml");
        defaultMappings.put("json", "application/json");
        defaultMappings.put("js", "text/javascript");
        defaultMappings.put("mjs", "text/javascript");
        defaultMappings.put("ts", "text/x.typescript");
        defaultMappings.put("txt", "text/plain");
        defaultMappings.put("xml", "application/xml");
        defaultMappings.put("svg", "image/svg+xml");
        defaultMappings.put("xhtml", "application/xhtml+xml");
        defaultMappings.put("html", "text/html");
        defaultMappings.put("htm", "text/html");
        defaultMappings.put("css", "text/css");
        defaultMappings.put("csv", "text/csv");
        defaultMappings.put("tsv", "text/tsv");
        defaultMappings.put("md", "text/x-markdown");
        defaultMappings.put("avif", "image/avif");
        defaultMappings.put("bmp", "image/bmp");
        defaultMappings.put("gif", "image/gif");
        defaultMappings.put("jpe", "image/jpeg");
        defaultMappings.put("jpeg", "image/jpeg");
        defaultMappings.put("jpg", "image/jpeg");
        defaultMappings.put("png", "image/png");
        defaultMappings.put("tif", "image/tiff");
        defaultMappings.put("tiff", "image/tiff");
        defaultMappings.put("webp", "image/webp");
        defaultMappings.put("mp3", "audio/mp3");
        defaultMappings.put("wav", "audio/wav");
        defaultMappings.put("aac", "audio/aac");
        defaultMappings.put("flac", "audio/flac");
        defaultMappings.put("mpa", "audio/m4a");
        defaultMappings.put("mpga", "audio/mpga");
        defaultMappings.put("opus", "audio/opus");
        defaultMappings.put("pcm", "audio/pcm");
        defaultMappings.put("mp4", "video/mp4");
        defaultMappings.put("mpeg", "video/mpeg");
        defaultMappings.put("mpg", "video/mpg");
        defaultMappings.put("mpegps", "video/mpegps");
        defaultMappings.put("mov", "video/mov");
        defaultMappings.put("avi", "video/avi");
        defaultMappings.put("flv", "video/x-flv");
        defaultMappings.put("webm", "video/webm");
        defaultMappings.put("wmv", "video/wmv");
        defaultMappings.put("3gpp", "video/3gpp");
    }
}

