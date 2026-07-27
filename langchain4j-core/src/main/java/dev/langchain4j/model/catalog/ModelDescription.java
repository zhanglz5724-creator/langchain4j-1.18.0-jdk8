/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.catalog;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.catalog.ModelType;
import java.time.Instant;
import java.util.Objects;

@Experimental
public class ModelDescription {
    private final String name;
    private final String displayName;
    private final String description;
    private final ModelProvider provider;
    private final ModelType type;
    private final Integer maxInputTokens;
    private final Integer maxOutputTokens;
    private final Instant createdAt;
    private final String owner;

    private ModelDescription(Builder builder) {
        this.name = ValidationUtils.ensureNotNull(builder.name, "name");
        this.displayName = builder.displayName;
        this.provider = ValidationUtils.ensureNotNull(builder.provider, "provider");
        this.description = builder.description;
        this.type = builder.type;
        this.maxInputTokens = builder.maxInputTokens;
        this.maxOutputTokens = builder.maxOutputTokens;
        this.createdAt = builder.createdAt;
        this.owner = builder.owner;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String name() {
        return this.name;
    }

    public String displayName() {
        return this.displayName == null ? this.name : this.displayName;
    }

    public String description() {
        return this.description;
    }

    public ModelProvider provider() {
        return this.provider;
    }

    public ModelType type() {
        return this.type;
    }

    public Integer maxInputTokens() {
        return this.maxInputTokens;
    }

    public Integer maxOutputTokens() {
        return this.maxOutputTokens;
    }

    public Instant createdAt() {
        return this.createdAt;
    }

    public String owner() {
        return this.owner;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModelDescription)) {
            return false;
        }
        ModelDescription that = (ModelDescription)o;
        return Objects.equals(this.name, that.name) && Objects.equals((Object)this.provider, (Object)that.provider);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.name, this.provider});
    }

    public String toString() {
        return "ModelDescription{name='" + this.name + '\'' + ", displayName='" + this.displayName + '\'' + ", description='" + this.description + '\'' + ", provider=" + (Object)((Object)this.provider) + ", type=" + (Object)((Object)this.type) + ", maxInputTokens=" + this.maxInputTokens + ", maxOutputTokens=" + this.maxOutputTokens + ", createdAt=" + this.createdAt + ", owner='" + this.owner + '\'' + '}';
    }

    public static class Builder {
        private String name;
        private String displayName;
        private String description;
        private ModelProvider provider;
        private ModelType type;
        private Integer maxInputTokens;
        private Integer maxOutputTokens;
        private Instant createdAt;
        private String owner;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder displayName(String name) {
            this.displayName = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder provider(ModelProvider provider) {
            this.provider = provider;
            return this;
        }

        public Builder type(ModelType type) {
            this.type = type;
            return this;
        }

        public Builder maxInputTokens(Integer maxInputTokens) {
            this.maxInputTokens = maxInputTokens;
            return this;
        }

        public Builder maxOutputTokens(Integer maxOutputTokens) {
            this.maxOutputTokens = maxOutputTokens;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder owner(String owner) {
            this.owner = owner;
            return this;
        }

        public ModelDescription build() {
            return new ModelDescription(this);
        }
    }
}

