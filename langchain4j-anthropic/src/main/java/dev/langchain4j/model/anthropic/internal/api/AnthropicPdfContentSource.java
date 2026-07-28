/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.model.anthropic.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.langchain4j.internal.Utils;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicPdfContentSource {
    public String type;
    public String mediaType;
    public String data;
    public String url;

    public AnthropicPdfContentSource(String type, String mediaType, String data, String url) {
        this.type = type;
        this.mediaType = mediaType;
        this.data = data;
        this.url = url;
    }

    public AnthropicPdfContentSource(String type, String mediaType, String data) {
        this(type, mediaType, data, null);
    }

    public static AnthropicPdfContentSource fromBase64(String mediaType, String data) {
        return new AnthropicPdfContentSource("base64", mediaType, data, null);
    }

    public static AnthropicPdfContentSource fromUrl(String url) {
        return new AnthropicPdfContentSource("url", null, null, url);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AnthropicPdfContentSource that = (AnthropicPdfContentSource)o;
        return Objects.equals(this.type, that.type) && Objects.equals(this.mediaType, that.mediaType) && Objects.equals(this.data, that.data) && Objects.equals(this.url, that.url);
    }

    public int hashCode() {
        return Objects.hash(this.type, this.mediaType, this.data, this.url);
    }

    public String toString() {
        return "AnthropicPdfContentSource { type = " + Utils.quoted((Object)this.type) + ", mediaType = " + Utils.quoted((Object)this.mediaType) + ", data = " + Utils.quoted((Object)this.data) + ", url = " + Utils.quoted((Object)this.url) + " }";
    }
}

