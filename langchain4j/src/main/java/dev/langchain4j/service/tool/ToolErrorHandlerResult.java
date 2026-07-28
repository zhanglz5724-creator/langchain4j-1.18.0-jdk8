/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.service.tool;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Objects;

public class ToolErrorHandlerResult {
    private final String text;

    public ToolErrorHandlerResult(String text) {
        this.text = (String)ValidationUtils.ensureNotNull((Object)text, (String)"text");
    }

    public String text() {
        return this.text;
    }

    public static ToolErrorHandlerResult text(String text) {
        return new ToolErrorHandlerResult(text);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        ToolErrorHandlerResult that = (ToolErrorHandlerResult)object;
        return Objects.equals(this.text, that.text);
    }

    public int hashCode() {
        return Objects.hashCode(this.text);
    }

    public String toString() {
        return "ToolErrorHandlerResult{text=" + Utils.quoted((Object)this.text) + '}';
    }
}

