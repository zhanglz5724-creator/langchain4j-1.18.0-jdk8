/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.model.moderation;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

public class ModerationRequest {
    private final List<String> texts;
    private final @Nullable String modelName;

    private ModerationRequest(Builder builder) {
        this.texts = Utils.copy(ValidationUtils.ensureNotEmpty(builder.texts, "texts"));
        this.modelName = builder.modelName;
    }

    public List<String> texts() {
        return this.texts;
    }

    public @Nullable String modelName() {
        return this.modelName;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ModerationRequest that = (ModerationRequest)o;
        return Objects.equals(this.texts, that.texts) && Objects.equals(this.modelName, that.modelName);
    }

    public int hashCode() {
        return Objects.hash(this.texts, this.modelName);
    }

    public String toString() {
        return "ModerationRequest{texts=" + this.texts + ", modelName='" + this.modelName + '\'' + '}';
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> texts;
        private String modelName;

        private Builder() {
        }

        private Builder(ModerationRequest request) {
            this.texts = request.texts;
            this.modelName = request.modelName;
        }

        public Builder texts(List<String> texts) {
            this.texts = texts;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public ModerationRequest build() {
            return new ModerationRequest(this);
        }
    }
}

