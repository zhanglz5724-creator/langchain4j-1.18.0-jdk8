/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.internal.Exceptions
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.http.client;

import dev.langchain4j.Experimental;
import dev.langchain4j.http.client.FormDataFile;
import dev.langchain4j.http.client.HttpMethod;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequest {
    private final HttpMethod method;
    private final String url;
    private final Map<String, List<String>> headers;
    private final Map<String, String> formDataFields;
    private final Map<String, FormDataFile> formDataFiles;
    private final String body;

    public HttpRequest(Builder builder) {
        HttpRequest.validate(builder);
        this.method = (HttpMethod)((Object)ValidationUtils.ensureNotNull((Object)((Object)builder.method), (String)"method"));
        this.url = HttpRequest.buildUrl(builder);
        this.headers = Utils.copy((Map)builder.headers);
        this.formDataFields = Utils.copy((Map)builder.formDataFields);
        this.formDataFiles = Utils.copy((Map)builder.formDataFiles);
        this.body = builder.body;
    }

    private static void validate(Builder builder) {
        boolean hasFormDataFiles;
        boolean hasBody = builder.body != null;
        boolean hasFormDataFields = builder.formDataFields != null && !builder.formDataFields.isEmpty();
        boolean bl = hasFormDataFiles = builder.formDataFiles != null && !builder.formDataFiles.isEmpty();
        if (hasBody && hasFormDataFields) {
            throw Exceptions.illegalArgument((String)"Cannot specify both body and formDataFields", (Object[])new Object[0]);
        }
        if (hasBody && hasFormDataFiles) {
            throw Exceptions.illegalArgument((String)"Cannot specify both body and formDataFiles", (Object[])new Object[0]);
        }
    }

    private static String buildUrl(Builder builder) {
        String url = ValidationUtils.ensureNotBlank((String)builder.url, (String)"url");
        if (Utils.isNullOrEmpty((Map)builder.queryParams)) {
            return url;
        }
        String queryString = HttpRequest.buildQueryString(builder.queryParams);
        String separator = url.contains("?") ? "&" : "?";
        return url + separator + queryString;
    }

    private static String buildQueryString(Map<String, String> queryParams) {
        return queryParams.entrySet().stream().map(entry -> HttpRequest.encodeQueryParam((String)entry.getKey()) + "=" + HttpRequest.encodeQueryParam((String)entry.getValue())).collect(Collectors.joining("&"));
    }

    private static String encodeQueryParam(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpMethod method() {
        return this.method;
    }

    public String url() {
        return this.url;
    }

    public Map<String, List<String>> headers() {
        return this.headers;
    }

    @Experimental
    public Map<String, String> formDataFields() {
        return this.formDataFields;
    }

    @Experimental
    public Map<String, FormDataFile> formDataFiles() {
        return this.formDataFiles;
    }

    public String body() {
        return this.body;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HttpMethod method;
        private String url;
        private Map<String, List<String>> headers;
        private Map<String, String> queryParams;
        private Map<String, String> formDataFields;
        private Map<String, FormDataFile> formDataFiles;
        private String body;

        private Builder() {
        }

        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder url(String baseUrl, String path) {
            ValidationUtils.ensureNotBlank((String)baseUrl, (String)"baseUrl");
            ValidationUtils.ensureNotBlank((String)path, (String)"path");
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            this.url = baseUrl + "/" + path;
            return this;
        }

        public Builder addHeader(String name, String ... values) {
            ValidationUtils.ensureNotBlank((String)name, (String)"name");
            ValidationUtils.ensureNotEmpty((Object[])values, (String)"values");
            if (this.headers == null) {
                this.headers = new HashMap<String, List<String>>();
            }
            this.headers.put(name, Arrays.asList(values));
            return this;
        }

        public Builder addHeaders(Map<String, String> headers) {
            if (Utils.isNullOrEmpty(headers)) {
                return this;
            }
            if (this.headers == null) {
                this.headers = new HashMap<String, List<String>>();
            }
            headers.forEach((name, value) -> this.headers.put((String)name, Collections.singletonList(value)));
            return this;
        }

        public Builder headers(Map<String, List<String>> headers) {
            this.headers = headers;
            return this;
        }

        public Builder addQueryParam(String name, String value) {
            ValidationUtils.ensureNotBlank((String)name, (String)"name");
            ValidationUtils.ensureNotBlank((String)value, (String)"value");
            if (this.queryParams == null) {
                this.queryParams = new LinkedHashMap<String, String>();
            }
            this.queryParams.put(name, value);
            return this;
        }

        public Builder addQueryParams(Map<String, String> queryParams) {
            if (Utils.isNullOrEmpty(queryParams)) {
                return this;
            }
            if (this.queryParams == null) {
                this.queryParams = new LinkedHashMap<String, String>();
            }
            this.queryParams.putAll(queryParams);
            return this;
        }

        public Builder queryParams(Map<String, String> queryParams) {
            this.queryParams = queryParams == null ? null : new LinkedHashMap<String, String>(queryParams);
            return this;
        }

        @Experimental
        public Builder addFormDataField(String name, String value) {
            ValidationUtils.ensureNotBlank((String)name, (String)"name");
            ValidationUtils.ensureNotNull((Object)value, (String)"value");
            if (this.formDataFields == null) {
                this.formDataFields = new LinkedHashMap<String, String>();
            }
            this.formDataFields.put(name, value);
            return this;
        }

        @Experimental
        public Builder formDataFields(Map<String, String> formDataFields) {
            this.formDataFields = formDataFields == null ? null : new LinkedHashMap<String, String>(formDataFields);
            return this;
        }

        @Experimental
        public Builder addFormDataFile(String name, String fileName, String contentType, byte[] content) {
            if (content.length == 0) {
                return this;
            }
            if (this.formDataFiles == null) {
                this.formDataFiles = new LinkedHashMap<String, FormDataFile>();
            }
            this.formDataFiles.put(name, new FormDataFile(fileName, contentType, content));
            return this;
        }

        @Experimental
        public Builder formDataFiles(Map<String, FormDataFile> formDataFiles) {
            this.formDataFiles = formDataFiles == null ? null : new LinkedHashMap<String, FormDataFile>(formDataFiles);
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}

