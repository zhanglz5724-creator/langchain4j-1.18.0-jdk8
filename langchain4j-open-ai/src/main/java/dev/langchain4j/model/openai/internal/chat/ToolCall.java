/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 *  dev.langchain4j.internal.JacocoIgnoreCoverageGenerated
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.model.openai.internal.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.openai.internal.chat.FunctionCall;
import dev.langchain4j.model.openai.internal.chat.ToolType;
import java.util.Locale;
import java.util.Objects;

@JsonDeserialize(builder=ToolCall.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ToolCall {
    @JsonProperty
    private final String id;
    @JsonProperty
    private final Integer index;
    @JsonProperty
    private final ToolType type;
    @JsonProperty
    private final FunctionCall function;

    public ToolCall(Builder builder) {
        this.id = builder.id;
        this.index = builder.index;
        this.type = builder.type;
        this.function = builder.function;
    }

    public String id() {
        return this.id;
    }

    public Integer index() {
        return this.index;
    }

    public ToolType type() {
        return this.type;
    }

    public FunctionCall function() {
        return this.function;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof ToolCall && this.equalTo((ToolCall)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(ToolCall another) {
        return Objects.equals(this.id, another.id) && Objects.equals(this.index, another.index) && Objects.equals((Object)this.type, (Object)another.type) && Objects.equals(this.function, another.function);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.id);
        h += (h << 5) + Objects.hashCode(this.index);
        h += (h << 5) + Objects.hashCode((Object)this.type);
        h += (h << 5) + Objects.hashCode(this.function);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "ToolCall{id=" + this.id + ", index=" + this.index + ", type=" + (Object)((Object)this.type) + ", function=" + this.function + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private String id;
        private Integer index;
        private ToolType type;
        private FunctionCall function;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder index(Integer index) {
            this.index = index;
            return this;
        }

        public Builder type(String type) {
            if (Utils.isNotNullOrBlank((String)type)) {
                this.type = ToolType.valueOf(type.toUpperCase(Locale.ROOT));
            }
            return this;
        }

        public Builder type(ToolType type) {
            this.type = type;
            return this;
        }

        public Builder function(FunctionCall function) {
            this.function = function;
            return this;
        }

        public ToolCall build() {
            return new ToolCall(this);
        }
    }
}

