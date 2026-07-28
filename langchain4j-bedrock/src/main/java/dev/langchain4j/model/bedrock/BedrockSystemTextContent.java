/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.bedrock.BedrockSystemContent;
import dev.langchain4j.model.bedrock.BedrockSystemContentType;
import java.util.Objects;

public final class BedrockSystemTextContent
implements BedrockSystemContent {
    public static final int MAX_TEXT_LENGTH = 1000000;
    private static final int MAX_TOSTRING_LENGTH = 200;
    private final String text;
    private final boolean cachePoint;

    public BedrockSystemTextContent(String text, boolean cachePoint) {
        this.text = ValidationUtils.ensureNotBlank((String)text, (String)"text");
        ValidationUtils.ensureBetween((Integer)text.length(), (int)1, (int)1000000, (String)"text length");
        this.cachePoint = cachePoint;
    }

    public BedrockSystemTextContent(String text) {
        this(text, false);
    }

    public String text() {
        return this.text;
    }

    @Override
    public boolean hasCachePoint() {
        return this.cachePoint;
    }

    @Override
    public BedrockSystemContentType type() {
        return BedrockSystemContentType.TEXT;
    }

    public static BedrockSystemTextContent from(String text) {
        return new BedrockSystemTextContent(text, false);
    }

    public static BedrockSystemTextContent withCachePoint(String text) {
        return new BedrockSystemTextContent(text, true);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        BedrockSystemTextContent that = (BedrockSystemTextContent)o;
        return this.cachePoint == that.cachePoint && Objects.equals(this.text, that.text);
    }

    public int hashCode() {
        return Objects.hash(this.text, this.cachePoint);
    }

    public String toString() {
        String truncatedText = this.text.length() > 200 ? this.text.substring(0, 200) + "...[" + this.text.length() + " chars]" : this.text;
        return "BedrockSystemTextContent { text = " + Utils.quoted((Object)truncatedText) + ", cachePoint = " + this.cachePoint + " }";
    }
}

