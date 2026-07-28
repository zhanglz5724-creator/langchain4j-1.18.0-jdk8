/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  dev.langchain4j.http.client.HttpClient
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.http.client.HttpClientBuilderLoader
 *  dev.langchain4j.http.client.HttpMethod
 *  dev.langchain4j.http.client.HttpRequest
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.http.client.HttpClientBuilderLoader;
import dev.langchain4j.http.client.HttpMethod;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.googleai.Json;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

public final class GeminiFiles {
    private static final String BASE_URL = "https://generativelanguage.googleapis.com";
    private static final String DELETE_FILE_PATH = "/v1beta";
    private static final String GET_FILE_PATH = "/v1beta";
    private static final String LIST_FILES_PATH = "/v1beta/files";
    private static final String UPLOAD_PATH = "/upload/v1beta/files";
    private static final String API_KEY_HEADER_NAME = "x-goog-api-key";
    private static final String UPLOAD_URL_HEADER = "x-goog-upload-url";
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String apiKey;

    private GeminiFiles(Builder builder) {
        this.apiKey = builder.apiKey;
        HttpClientBuilder httpClientBuilder = (HttpClientBuilder)Utils.firstNotNull((String)"httpClientBuilder", (Object[])new HttpClientBuilder[]{builder.httpClientBuilder, HttpClientBuilderLoader.loadHttpClientBuilder()});
        this.httpClient = httpClientBuilder.build();
        this.baseUrl = (String)Utils.firstNotNull((String)"baseUrl", (Object[])new String[]{builder.baseUrl, BASE_URL});
    }

    public static Builder builder() {
        return new Builder();
    }

    public GeminiFile uploadFile(Path filePath, @Nullable String displayName) throws IOException, InterruptedException {
        ValidationUtils.ensureNotNull((Object)filePath, (String)"filePath");
        return this.uploadFile(Files.readAllBytes(filePath), this.detectMimeType(filePath), displayName != null ? displayName : filePath.getFileName().toString());
    }

    public GeminiFile uploadFile(byte[] fileBytes, String mimeType, String name) throws InterruptedException {
        ValidationUtils.ensureNotNull((Object)fileBytes, (String)"fileBytes");
        ValidationUtils.ensureNotNull((Object)mimeType, (String)"mimeType");
        ValidationUtils.ensureNotNull((Object)name, (String)"name");
        String uploadUrl = this.initiateResumableUpload(fileBytes.length, mimeType, name);
        GeminiFileResponse response = this.uploadFileBytes(uploadUrl, fileBytes);
        return response.file();
    }

    public GeminiFile getMetadata(String name) throws IOException, InterruptedException {
        ValidationUtils.ensureNotBlank((String)name, (String)"name");
        String url = this.baseUrl + "/v1beta" + "/" + name;
        HttpRequest request = HttpRequest.builder().addHeader(API_KEY_HEADER_NAME, new String[]{this.apiKey}).method(HttpMethod.GET).url(url).build();
        SuccessfulHttpResponse response = this.httpClient.execute(request);
        if (response.statusCode() != 200) {
            throw new GeminiUploadFailureException("Failed to retrieve metadata for file: " + name + ". Status code: " + response.statusCode());
        }
        return Json.fromJson(response.body(), GeminiFile.class);
    }

    public List<GeminiFile> listFiles() throws IOException, InterruptedException {
        String url = this.baseUrl + LIST_FILES_PATH;
        HttpRequest request = HttpRequest.builder().addHeader(API_KEY_HEADER_NAME, new String[]{this.apiKey}).method(HttpMethod.GET).url(url).build();
        SuccessfulHttpResponse response = this.httpClient.execute(request);
        GeminiFilesListResponse listResponse = Json.fromJson(response.body(), GeminiFilesListResponse.class);
        return listResponse.files() != null ? listResponse.files() : Collections.emptyList();
    }

    public void deleteFile(String name) throws IOException, InterruptedException {
        ValidationUtils.ensureNotBlank((String)name, (String)"name");
        String url = this.baseUrl + "/v1beta" + "/" + name;
        HttpRequest request = HttpRequest.builder().addHeader(API_KEY_HEADER_NAME, new String[]{this.apiKey}).method(HttpMethod.DELETE).url(url).build();
        SuccessfulHttpResponse response = this.httpClient.execute(request);
        if (!Arrays.asList(200, 204).contains(response.statusCode())) {
            throw new GeminiUploadFailureException("Failed to delete file: " + name + ". Status code: " + response.statusCode());
        }
    }

    private String initiateResumableUpload(long contentLength, String mimeType, String displayName) throws InterruptedException {
        String url = this.baseUrl + UPLOAD_PATH;
        GeminiFileMetadata metadata = new GeminiFileMetadata(new GeminiFileMetadata.FileInfo(displayName));
        String jsonBody = Json.toJson(metadata);
        try {
            List uploadUrlList;
            HttpRequest request = HttpRequest.builder().addHeader("Content-Type", new String[]{"application/json"}).addHeader("User-Agent", new String[]{"LangChain4j"}).addHeader(API_KEY_HEADER_NAME, new String[]{this.apiKey}).addHeader("X-Goog-Upload-Protocol", new String[]{"resumable"}).addHeader("X-Goog-Upload-Command", new String[]{"start"}).addHeader("X-Goog-Upload-Header-Content-Length", new String[]{String.valueOf(contentLength)}).addHeader("X-Goog-Upload-Header-Content-Type", new String[]{mimeType}).method(HttpMethod.POST).url(url).body(jsonBody).build();
            SuccessfulHttpResponse response = this.httpClient.execute(request);
            Map responseHeaders = response.headers();
            List list = uploadUrlList = responseHeaders != null ? (List)responseHeaders.get(UPLOAD_URL_HEADER) : null;
            if (uploadUrlList == null || uploadUrlList.isEmpty() || uploadUrlList.get(0) == null || ((String)uploadUrlList.get(0)).isEmpty()) {
                throw new IllegalStateException("Upload URL not found in response headers");
            }
            return ((String)uploadUrlList.get(0)).trim();
        }
        catch (RuntimeException e) {
            throw new GeminiUploadFailureException("Failed to initiate resumable upload", e);
        }
    }

    private GeminiFileResponse uploadFileBytes(String uploadUrl, byte[] fileBytes) throws InterruptedException {
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("X-Goog-Upload-Offset", "0");
            connection.setRequestProperty("X-Goog-Upload-Command", "upload, finalize");
            connection.setDoOutput(true);
            connection.setFixedLengthStreamingMode(fileBytes.length);
            OutputStream os = connection.getOutputStream();
            os.write(fileBytes);
            os.flush();
            os.close();
            int statusCode = connection.getResponseCode();
            String responseBody = statusCode >= 200 && statusCode < 300 ? new String(GeminiFiles.readAllBytes(connection.getInputStream()), StandardCharsets.UTF_8) : new String(GeminiFiles.readAllBytes(connection.getErrorStream()), StandardCharsets.UTF_8);
            return Json.fromJson(responseBody, GeminiFileResponse.class);
        }
        catch (IOException e) {
            throw new GeminiUploadFailureException("Failed to upload file bytes", e);
        }
    }

    private static byte[] readAllBytes(InputStream in) throws IOException {
        int n;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[8192];
        while ((n = in.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, n);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

    private String detectMimeType(Path filePath) throws IOException {
        String mimeType = Files.probeContentType(filePath);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        return mimeType;
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static final class GeminiFile {
        private final String name;
        private final @Nullable String displayName;
        private final String mimeType;
        private final Long sizeBytes;
        private final String createTime;
        private final String updateTime;
        private final String expirationTime;
        private final String sha256Hash;
        private final String uri;
        private final String state;

        @JsonCreator
        public GeminiFile(@JsonProperty(value="name") String name, @JsonProperty(value="displayName") @Nullable String displayName, @JsonProperty(value="mimeType") String mimeType, @JsonProperty(value="sizeBytes") Long sizeBytes, @JsonProperty(value="createTime") String createTime, @JsonProperty(value="updateTime") String updateTime, @JsonProperty(value="expirationTime") String expirationTime, @JsonProperty(value="sha256Hash") String sha256Hash, @JsonProperty(value="uri") String uri, @JsonProperty(value="state") String state) {
            this.name = name;
            this.displayName = displayName;
            this.mimeType = mimeType;
            this.sizeBytes = sizeBytes;
            this.createTime = createTime;
            this.updateTime = updateTime;
            this.expirationTime = expirationTime;
            this.sha256Hash = sha256Hash;
            this.uri = uri;
            this.state = state;
        }

        public String name() {
            return this.name;
        }

        public @Nullable String displayName() {
            return this.displayName;
        }

        public String mimeType() {
            return this.mimeType;
        }

        public Long sizeBytes() {
            return this.sizeBytes;
        }

        public String createTime() {
            return this.createTime;
        }

        public String updateTime() {
            return this.updateTime;
        }

        public String expirationTime() {
            return this.expirationTime;
        }

        public String sha256Hash() {
            return this.sha256Hash;
        }

        public String uri() {
            return this.uri;
        }

        public String state() {
            return this.state;
        }

        public boolean isActive() {
            return "ACTIVE".equals(this.state);
        }

        public boolean isProcessing() {
            return "PROCESSING".equals(this.state);
        }

        public boolean isFailed() {
            return "FAILED".equals(this.state);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GeminiFile)) {
                return false;
            }
            GeminiFile that = (GeminiFile)o;
            return Objects.equals(this.name, that.name) && Objects.equals(this.displayName, that.displayName) && Objects.equals(this.mimeType, that.mimeType) && Objects.equals(this.sizeBytes, that.sizeBytes) && Objects.equals(this.createTime, that.createTime) && Objects.equals(this.updateTime, that.updateTime) && Objects.equals(this.expirationTime, that.expirationTime) && Objects.equals(this.sha256Hash, that.sha256Hash) && Objects.equals(this.uri, that.uri) && Objects.equals(this.state, that.state);
        }

        public int hashCode() {
            return Objects.hash(this.name, this.displayName, this.mimeType, this.sizeBytes, this.createTime, this.updateTime, this.expirationTime, this.sha256Hash, this.uri, this.state);
        }

        public String toString() {
            return "GeminiFile[name=" + this.name + ", displayName=" + this.displayName + ", mimeType=" + this.mimeType + ", sizeBytes=" + this.sizeBytes + ", createTime=" + this.createTime + ", updateTime=" + this.updateTime + ", expirationTime=" + this.expirationTime + ", sha256Hash=" + this.sha256Hash + ", uri=" + this.uri + ", state=" + this.state + "]";
        }
    }

    public static class Builder {
        HttpClientBuilder httpClientBuilder;
        String apiKey;
        String baseUrl;

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public GeminiFiles build() {
            return new GeminiFiles(this);
        }
    }

    static class GeminiUploadFailureException
    extends RuntimeException {
        GeminiUploadFailureException(String message, Throwable cause) {
            super(message, cause);
        }

        GeminiUploadFailureException(String message) {
            super(message);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    static final class GeminiFilesListResponse {
        private final List<GeminiFile> files;

        @JsonCreator
        GeminiFilesListResponse(@JsonProperty(value="files") List<GeminiFile> files) {
            this.files = files;
        }

        List<GeminiFile> files() {
            return this.files;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GeminiFilesListResponse)) {
                return false;
            }
            GeminiFilesListResponse that = (GeminiFilesListResponse)o;
            return Objects.equals(this.files, that.files);
        }

        public int hashCode() {
            return Objects.hash(this.files);
        }

        public String toString() {
            return "GeminiFilesListResponse[files=" + this.files + "]";
        }
    }

    static final class GeminiFileResponse {
        private final GeminiFile file;

        @JsonCreator
        GeminiFileResponse(@JsonProperty(value="file") GeminiFile file) {
            this.file = file;
        }

        GeminiFile file() {
            return this.file;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GeminiFileResponse)) {
                return false;
            }
            GeminiFileResponse that = (GeminiFileResponse)o;
            return Objects.equals(this.file, that.file);
        }

        public int hashCode() {
            return Objects.hash(this.file);
        }

        public String toString() {
            return "GeminiFileResponse[file=" + this.file + "]";
        }
    }

    static final class GeminiFileMetadata {
        private final FileInfo file;

        @JsonCreator
        GeminiFileMetadata(@JsonProperty(value="file") FileInfo file) {
            this.file = file;
        }

        FileInfo file() {
            return this.file;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GeminiFileMetadata)) {
                return false;
            }
            GeminiFileMetadata that = (GeminiFileMetadata)o;
            return Objects.equals(this.file, that.file);
        }

        public int hashCode() {
            return Objects.hash(this.file);
        }

        public String toString() {
            return "GeminiFileMetadata[file=" + this.file + "]";
        }

        static final class FileInfo {
            private final String displayName;

            @JsonCreator
            FileInfo(@JsonProperty(value="display_name") String displayName) {
                this.displayName = displayName;
            }

            String displayName() {
                return this.displayName;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof FileInfo)) {
                    return false;
                }
                FileInfo that = (FileInfo)o;
                return Objects.equals(this.displayName, that.displayName);
            }

            public int hashCode() {
                return Objects.hash(this.displayName);
            }

            public String toString() {
                return "FileInfo[displayName=" + this.displayName + "]";
            }
        }
    }
}

