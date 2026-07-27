/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.moderation;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.moderation.Moderation;
import java.util.Map;
import java.util.Objects;

public class ModerationResponse {
    private final Moderation moderation;
    private final Map<String, Object> metadata;

    private ModerationResponse(Builder builder) {
        this.moderation = ValidationUtils.ensureNotNull(builder.moderation, "moderation");
        this.metadata = Utils.copy(builder.metadata);
    }

    public Moderation moderation() {
        return this.moderation;
    }

    public Map<String, Object> metadata() {
        return this.metadata;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ModerationResponse that = (ModerationResponse)o;
        return Objects.equals(this.moderation, that.moderation) && Objects.equals(this.metadata, that.metadata);
    }

    public int hashCode() {
        return Objects.hash(this.moderation, this.metadata);
    }

    public String toString() {
        return "ModerationResponse{moderation=" + this.moderation + ", metadata=" + this.metadata + '}';
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Moderation moderation;
        private Map<String, Object> metadata;

        private Builder() {
        }

        private Builder(ModerationResponse response) {
            this.moderation = response.moderation;
            this.metadata = response.metadata;
        }

        public Builder moderation(Moderation moderation) {
            this.moderation = moderation;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public ModerationResponse build() {
            return new ModerationResponse(this);
        }
    }
}

