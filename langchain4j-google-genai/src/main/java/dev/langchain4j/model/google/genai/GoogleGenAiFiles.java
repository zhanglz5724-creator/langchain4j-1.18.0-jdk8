/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.genai.Client
 *  com.google.genai.types.DeleteFileConfig
 *  com.google.genai.types.File
 *  com.google.genai.types.GetFileConfig
 *  com.google.genai.types.ListFilesConfig
 *  com.google.genai.types.UploadFileConfig
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.model.google.genai;

import com.google.genai.Client;
import com.google.genai.types.DeleteFileConfig;
import com.google.genai.types.File;
import com.google.genai.types.GetFileConfig;
import com.google.genai.types.ListFilesConfig;
import com.google.genai.types.UploadFileConfig;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.google.genai.GoogleGenAiClientFactory;
import dev.langchain4j.model.google.genai.GoogleGenAiContentMapper;
import dev.langchain4j.model.google.genai.GoogleGenAiExceptionMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class GoogleGenAiFiles {
    private final Client client;

    private GoogleGenAiFiles(Builder builder) {
        this.client = builder.client != null ? builder.client : GoogleGenAiClientFactory.createClient(builder.apiKey, null, null, null, null, builder.customHeaders, builder.apiEndpoint);
    }

    public static Builder builder() {
        return new Builder();
    }

    public File uploadFile(Path filePath, String displayName) {
        ValidationUtils.ensureNotNull((Object)filePath, (String)"filePath");
        try {
            UploadFileConfig config = UploadFileConfig.builder().displayName(displayName != null ? displayName : filePath.getFileName().toString()).mimeType(this.detectMimeType(filePath)).build();
            return (File)GoogleGenAiExceptionMapper.INSTANCE.withExceptionMapper(() -> this.client.files.upload(filePath.toFile(), config));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public File uploadFile(byte[] fileBytes, String mimeType, String name) {
        ValidationUtils.ensureNotNull((Object)fileBytes, (String)"fileBytes");
        ValidationUtils.ensureNotNull((Object)mimeType, (String)"mimeType");
        ValidationUtils.ensureNotNull((Object)name, (String)"name");
        UploadFileConfig config = UploadFileConfig.builder().displayName(name).mimeType(mimeType).build();
        return (File)GoogleGenAiExceptionMapper.INSTANCE.withExceptionMapper(() -> this.client.files.upload(fileBytes, config));
    }

    public File getMetadata(String name) {
        ValidationUtils.ensureNotBlank((String)name, (String)"name");
        return (File)GoogleGenAiExceptionMapper.INSTANCE.withExceptionMapper(() -> this.client.files.get(name, GetFileConfig.builder().build()));
    }

    public List<File> listFiles() {
        return (List)GoogleGenAiExceptionMapper.INSTANCE.withExceptionMapper(() -> {
            ArrayList allFiles = new ArrayList();
            this.client.files.list(ListFilesConfig.builder().build()).forEach(allFiles::add);
            return allFiles;
        });
    }

    public void deleteFile(String name) {
        ValidationUtils.ensureNotBlank((String)name, (String)"name");
        GoogleGenAiExceptionMapper.INSTANCE.withExceptionMapper(() -> {
            this.client.files.delete(name, DeleteFileConfig.builder().build());
            return null;
        });
    }

    private String detectMimeType(Path filePath) throws IOException {
        String mimeType = Files.probeContentType(filePath);
        if (mimeType == null) {
            try {
                mimeType = GoogleGenAiContentMapper.detectMimeType(filePath.toUri());
            }
            catch (IllegalArgumentException e) {
                mimeType = "application/octet-stream";
            }
        }
        return mimeType;
    }

    public static class Builder {
        private String apiKey;
        private String apiEndpoint;
        private Map<String, String> customHeaders;
        private Client client;

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder apiEndpoint(String apiEndpoint) {
            this.apiEndpoint = apiEndpoint;
            return this;
        }

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
            return this;
        }

        public Builder client(Client client) {
            this.client = client;
            return this;
        }

        public GoogleGenAiFiles build() {
            return new GoogleGenAiFiles(this);
        }
    }
}

