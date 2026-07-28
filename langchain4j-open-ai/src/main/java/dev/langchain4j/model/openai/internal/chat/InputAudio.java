/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  dev.langchain4j.internal.JacocoIgnoreCoverageGenerated
 */
package dev.langchain4j.model.openai.internal.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import java.util.Objects;

@JsonDeserialize(builder=Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InputAudio {
    private final String data;
    private final String format;

    public InputAudio(Builder builder) {
        this.data = builder.data;
        this.format = builder.format;
    }

    public String getData() {
        return this.data;
    }

    public String getFormat() {
        return this.format;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof InputAudio && this.equalTo((InputAudio)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(InputAudio another) {
        return Objects.equals(this.data, another.data) && Objects.equals(this.format, another.format);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.data);
        h += (h << 5) + Objects.hashCode(this.format);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "InputAudio{data=" + this.data + ", format=" + this.format + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String data;
        private String format;

        public Builder data(String data) {
            this.data = data;
            return this;
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }

        public static Builder builder() {
            return new Builder();
        }

        public InputAudio build() {
            return new InputAudio(this);
        }
    }
}

