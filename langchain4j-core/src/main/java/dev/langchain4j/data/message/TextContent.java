/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.message;

import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Objects;

public class TextContent
implements Content {
    private final String text;

    public TextContent(String text) {
        this.text = ValidationUtils.ensureNotNull(text, "text");
    }

    public String text() {
        return this.text;
    }

    @Override
    public ContentType type() {
        return ContentType.TEXT;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        TextContent that = (TextContent)o;
        return Objects.equals(this.text, that.text);
    }

    public int hashCode() {
        return Objects.hash(this.text);
    }

    public String toString() {
        return "TextContent { text = " + Utils.quoted(this.text) + " }";
    }

    public static TextContent from(String text) {
        return new TextContent(text);
    }
}

