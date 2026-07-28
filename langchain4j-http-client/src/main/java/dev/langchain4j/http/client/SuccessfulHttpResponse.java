/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.http.client;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class SuccessfulHttpResponse {
    private final int statusCode;
    private final Map<String, List<String>> headers;
    private final byte[] body;

    public SuccessfulHttpResponse(Builder builder) {
        this.statusCode = ValidationUtils.ensureBetween((Integer)builder.statusCode, (int)200, (int)299, (String)"statusCode");
        this.headers = Utils.copy((Map)builder.headers);
        this.body = builder.body;
    }

    public int statusCode() {
        return this.statusCode;
    }

    public Map<String, List<String>> headers() {
        return this.headers;
    }

    public String contentType() {
        if (this.headers == null) {
            return null;
        }
        return this.headers.entrySet().stream().filter(entry -> "content-type".equalsIgnoreCase((String)entry.getKey())).map(Map.Entry::getValue).filter(values -> values != null && !values.isEmpty()).map(values -> (String)values.get(0)).findFirst().orElse(null);
    }

    public String body() {
        return this.body == null ? null : new String(this.body, this.charset());
    }

    public byte[] bodyBytes() {
        return this.body;
    }

    private Charset charset() {
        String contentType = this.contentType();
        if (contentType == null) {
            return StandardCharsets.UTF_8;
        }
        for (String part : contentType.split(";")) {
            String trimmed = part.trim();
            if (!trimmed.regionMatches(true, 0, "charset=", 0, "charset=".length())) continue;
            String charsetName = trimmed.substring("charset=".length()).trim().replaceAll("^\"|\"$", "");
            try {
                return Charset.forName(charsetName);
            }
            catch (Exception e) {
                return StandardCharsets.UTF_8;
            }
        }
        return StandardCharsets.UTF_8;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int statusCode;
        private Map<String, List<String>> headers;
        private byte[] body;

        private Builder() {
        }

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder headers(Map<String, List<String>> headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body;
            return this;
        }

        public Builder body(String body) {
            this.body = body == null ? null : body.getBytes(StandardCharsets.UTF_8);
            return this;
        }

        public SuccessfulHttpResponse build() {
            return new SuccessfulHttpResponse(this);
        }
    }
}

