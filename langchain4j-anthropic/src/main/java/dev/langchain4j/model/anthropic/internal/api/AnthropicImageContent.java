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
import dev.langchain4j.model.anthropic.internal.api.AnthropicImageContentSource;
import dev.langchain4j.model.anthropic.internal.api.AnthropicMessageContent;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicImageContent
extends AnthropicMessageContent {
    public AnthropicImageContentSource source;

    public AnthropicImageContent(AnthropicImageContentSource source) {
        super("image");
        this.source = source;
    }

    public AnthropicImageContent(String mediaType, String data) {
        super("image");
        this.source = new AnthropicImageContentSource("base64", mediaType, data);
    }

    public static AnthropicImageContent fromBase64(String mediaType, String data) {
        return new AnthropicImageContent(AnthropicImageContentSource.fromBase64(mediaType, data));
    }

    public static AnthropicImageContent fromUrl(String url) {
        return new AnthropicImageContent(AnthropicImageContentSource.fromUrl(url));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AnthropicImageContent that = (AnthropicImageContent)o;
        return Objects.equals(this.source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.source);
    }

    public String toString() {
        return "AnthropicImageContent{source=" + this.source + ", type='" + this.type + '\'' + ", cacheControl=" + this.cacheControl + '}';
    }
}

