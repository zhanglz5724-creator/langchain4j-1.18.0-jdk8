/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.response;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Objects;

@Experimental
@JacocoIgnoreCoverageGenerated
public class PartialResponse {
    private final String text;

    public PartialResponse(String text) {
        this.text = ValidationUtils.ensureNotEmpty(text, "text");
    }

    public String text() {
        return this.text;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        PartialResponse that = (PartialResponse)object;
        return Objects.equals(this.text, that.text);
    }

    public int hashCode() {
        return Objects.hashCode(this.text);
    }

    public String toString() {
        return "PartialResponse{text='" + this.text + '\'' + '}';
    }
}

