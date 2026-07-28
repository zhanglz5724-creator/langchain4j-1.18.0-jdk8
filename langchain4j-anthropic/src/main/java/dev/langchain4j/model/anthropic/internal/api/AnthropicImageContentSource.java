/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.anthropic.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicImageContentSource {
    public String type;
    public String mediaType;
    public String data;
    public String url;

    public AnthropicImageContentSource(String type, String mediaType, String data, String url) {
        this.type = type;
        this.mediaType = mediaType;
        this.data = data;
        this.url = url;
    }

    public AnthropicImageContentSource(String type, String mediaType, String data) {
        this.type = type;
        this.mediaType = mediaType;
        this.data = data;
    }

    public static AnthropicImageContentSource fromBase64(String mediaType, String data) {
        return new AnthropicImageContentSource("base64", mediaType, data, null);
    }

    public static AnthropicImageContentSource fromUrl(String url) {
        return new AnthropicImageContentSource("url", null, null, url);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AnthropicImageContentSource that = (AnthropicImageContentSource)o;
        return Objects.equals(this.type, that.type) && Objects.equals(this.mediaType, that.mediaType) && Objects.equals(this.data, that.data) && Objects.equals(this.url, that.url);
    }

    public int hashCode() {
        return Objects.hash(this.type, this.mediaType, this.data, this.url);
    }

    public String toString() {
        return "AnthropicImageContentSource{type='" + this.type + '\'' + ", mediaType='" + this.mediaType + '\'' + ", data='" + this.data + '\'' + ", url='" + this.url + '\'' + '}';
    }
}

