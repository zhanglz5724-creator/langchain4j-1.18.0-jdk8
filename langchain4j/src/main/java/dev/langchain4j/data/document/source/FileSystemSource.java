/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.DocumentSource
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.data.document.source;

import dev.langchain4j.data.document.DocumentSource;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.internal.ValidationUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemSource
implements DocumentSource {
    private final Path path;

    public FileSystemSource(Path path) {
        this.path = (Path)ValidationUtils.ensureNotNull((Object)path, (String)"path");
    }

    public InputStream inputStream() throws IOException {
        return Files.newInputStream(this.path, new OpenOption[0]);
    }

    public Metadata metadata() {
        return new Metadata().put("file_name", this.path.getFileName().toString()).put("absolute_directory_path", this.path.toAbsolutePath().getParent().toString());
    }

    public static FileSystemSource from(Path filePath) {
        return new FileSystemSource(filePath);
    }

    public static FileSystemSource from(String filePath) {
        return new FileSystemSource(Paths.get(filePath, new String[0]));
    }

    public static FileSystemSource from(URI fileUri) {
        return new FileSystemSource(Paths.get(fileUri));
    }

    public static FileSystemSource from(File file) {
        return new FileSystemSource(file.toPath());
    }

    public String toString() {
        return "FileSystemSource { path = " + this.path + " }";
    }
}

