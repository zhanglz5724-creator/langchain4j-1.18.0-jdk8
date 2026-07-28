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
package dev.langchain4j.model.huggingface.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Options {
    private final Boolean waitForModel;
    private final Boolean useCache;

    Options(Builder builder) {
        this.waitForModel = builder.waitForModel;
        this.useCache = builder.useCache;
    }

    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof Options && this.equalTo((Options)another);
    }

    private boolean equalTo(Options another) {
        return Objects.equals(this.waitForModel, another.waitForModel) && Objects.equals(this.useCache, another.useCache);
    }

    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.waitForModel);
        h += (h << 5) + Objects.hashCode(this.useCache);
        return h;
    }

    public String toString() {
        return "TextGenerationRequest { waitForModel = " + this.waitForModel + ", useCache = " + this.useCache + " }";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Boolean waitForModel = true;
        private Boolean useCache;

        public Builder waitForModel(Boolean waitForModel) {
            if (waitForModel != null) {
                this.waitForModel = waitForModel;
            }
            return this;
        }

        public Builder useCache(Boolean useCache) {
            this.useCache = useCache;
            return this;
        }

        public Options build() {
            return new Options(this);
        }
    }
}

